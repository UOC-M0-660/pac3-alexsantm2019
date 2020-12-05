package edu.uoc.pac3.twitch.streams

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.streams.StreamAdapter
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.twitch.profile.ProfileActivity
import io.ktor.client.features.*
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var adapter: StreamAdapter
    private var cursor = ""
    private var isLoading = false
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)

        // Init RecyclerView
        initRecyclerView()

        // TODO: Get Streams
        getStreams()

        setupRecycler()
        refreshStreams()

    }

    private fun initRecyclerView() {
        // TODO: Implement
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = StreamAdapter(mutableListOf())
        layoutManager = LinearLayoutManager(this)

        recyclerView.let {
            it.setHasFixedSize(true)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    private fun getStreams(cursor: String? = null) {
        lifecycleScope.launch {
            // Envio cursor
            val streamsResponse: StreamsResponse?
            if (cursor == null) {                     // En caso de que cursor sea nulo
                streamsResponse = loadStreams()
            } else {                                  // En caso contrario, lo envio como parametro a funcion loadStreams
                streamsResponse = loadStreams(cursor)
            }
            streamsResponse?.let {
                streamsResponse.data?.let { streams ->
                    adapter.addStreams(streams)     // Añado mas items a los ya existentes
                }
                streamsResponse.pagination?.cursor?.let {
                    this@StreamsActivity.cursor = it
                }
            } ?: run {
                Toast.makeText(
                    this@StreamsActivity,
                    getString(R.string.error_streams),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        isLoading = false
    }

    private suspend fun loadStreams(cursor: String? = null): StreamsResponse? {
        val instanceClient = Network.createHttpClient(this)
        val service = TwitchApiService(instanceClient)
        var streamsResponse: StreamsResponse? = null
        try {
            streamsResponse = service.getStreams(cursor)
        } catch (e: ClientRequestException) {
            if (e is ClientRequestException && e.response?.status?.value == 401) {
                renewToken(service)
                val newInstanceClient = Network.createHttpClient(this)
                val newService = TwitchApiService(newInstanceClient)
                try {
                    streamsResponse = newService.getStreams(cursor)
                } catch (e: ClientRequestException) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.login_again),
                        Toast.LENGTH_SHORT
                    ).show()
                } finally {
                    newInstanceClient.close()
                }
            }
        } finally {
            instanceClient.close()
        }
        return streamsResponse
    }

    private fun setupRecycler() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    val visibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = adapter.itemCount
                    if (visibleItemPosition == totalItemCount - 1) {
                        isLoading = true
                        recyclerView.post {
                            getStreams()
                        }
                    }
                }
            }
        })
    }

    private fun refreshStreams() {
        swipeRefreshLayout.setOnRefreshListener {
            getStreams()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    // Opciones de Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun renewToken(service: TwitchApiService) {
        val sessionManager = SessionManager(this)
        sessionManager.clearAccessToken()
        try {
            sessionManager.getRefreshToken()?.let {
                service.getRefreshToken(it)?.let { tokensResponse ->
                    sessionManager.saveAccessToken(tokensResponse.accessToken)
                    tokensResponse.refreshToken?.let {
                        sessionManager.saveRefreshToken(it)
                    }
                }
            }
        } catch (e: ClientRequestException) {
            // En caso de no poder obtener accessToken hago LogOut para que el usuario haga Login de nuevo
            Toast.makeText(
                applicationContext,
                getString(R.string.error_refresh_token),
                Toast.LENGTH_SHORT
            ).show()
            logout()
        }
    }

    private fun logout() {
        val sessionManager = SessionManager(this)
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}
