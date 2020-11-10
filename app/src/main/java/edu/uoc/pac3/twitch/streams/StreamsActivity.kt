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
import com.google.android.material.snackbar.Snackbar
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.streams.Stream
import edu.uoc.pac3.data.streams.StreamAdapter
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.oauth.LoginActivity
import edu.uoc.pac3.twitch.profile.ProfileActivity
import io.ktor.client.features.*
import kotlinx.android.synthetic.main.activity_streams.*
import kotlinx.coroutines.launch
import org.json.JSONObject

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

    private fun getStreams(cursor: String? = null){
        lifecycleScope.launch {
            // Envio cursor
            val streamsResponse: StreamsResponse?
            if(cursor == null){
                streamsResponse = loadStreams()
            }else{
                streamsResponse = loadStreams(cursor)
            }
            streamsResponse?.let {
                streamsResponse.data?.let {streams->
                    adapter.addStreams(streams)     // Añado mas items a los ya existentes
                }
                streamsResponse.pagination?.cursor?.let {
                    this@StreamsActivity.cursor = it
                }
            }?: run {
                Toast.makeText(this@StreamsActivity, getString(R.string.error_streams), Toast.LENGTH_SHORT).show()
            }
        }
        isLoading = false
    }

    private suspend fun loadStreams(cursor: String? = null): StreamsResponse?{
        val instanceClient = Network.createHttpClient(this)
        val service = TwitchApiService(instanceClient)
        var streamsResponse: StreamsResponse? = null
        try {
            streamsResponse = service.getStreams(cursor)
        } catch (e: ClientRequestException) {
            refreshToken(service)
            val secondInstanceClient = Network.createHttpClient(this@StreamsActivity)
            val service2 = TwitchApiService(secondInstanceClient)
            try {
                 streamsResponse = service2.getStreams(cursor)
            } catch (e: ClientRequestException) {
                Toast.makeText( applicationContext, "Es necesario hacer logout", Toast.LENGTH_SHORT).show()
            } finally {
                secondInstanceClient.close()
            }
        } finally {
            instanceClient.close()
        }
        return streamsResponse
    }

    private fun setupRecycler(){
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading) {
                    val visibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = adapter.itemCount
                    if (visibleItemPosition == totalItemCount - 1) {
                        isLoading = true
                        recyclerView.post {
                            //loadMoreStreams()
                            getStreams()
                        }
                    }
                }
            }
        })
    }

    private fun refreshStreams(){
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
                //intent.putExtra("accessToken",accessToken)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun refreshToken(service: TwitchApiService){
        val sessionManager = SessionManager(this)
        sessionManager.clearAccessToken()
        try {
            sessionManager.getRefreshToken()?.let {
                service.getRefreshToken(it)?.let { tokensResponse ->
                    Log.i(TAG, "refreshToken: nuevo access token ${tokensResponse.accessToken}")
                    sessionManager.saveAccessToken(tokensResponse.accessToken)
                    tokensResponse.refreshToken?.let {
                        sessionManager.saveRefreshToken(it)
                    }
                }
            }
        }catch (e: ClientRequestException){
            Toast.makeText( applicationContext, "Ocurrio un error al momento de refrescar Token", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}


//    private fun getStreams() { NUEVO
//        lifecycleScope.launch {
//            //val streamsResponse = loadStreams()
//            val streamsResponse: StreamsResponse? = loadStreams()
//
//            streamsResponse?.let {
//                streamsResponse.data?.let {streams->
//                    adapter.setStreams(streams as MutableList<Stream>)
//                }
//                streamsResponse.pagination?.cursor?.let {
//                    cursor = it
//                }
//            }?: run {
//                Toast.makeText(this@StreamsActivity, getString(R.string.error_streams), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private fun getStreams() { ANTIGUOL
//        val service = TwitchApiService(Network.createHttpClient(this))
//        lifecycleScope.launch {
//            service.getStreams()?.let { streamsResponse->
//                Log.d(TAG, "************* DATOS: *************** " + streamsResponse.pagination)
//                streamsResponse.data?.let { streams->
//                    adapter.setStreams(streams)
//                }
//            }
//        }
//    }

//    private fun setUpSteams(){
//        lifecycleScope.launch {
//            val streamsResponse = loadStreams()
//            streamsResponse?.let {
//                streamsResponse.data?.let {streams->
//                    adapter.setStreams(streams)
//                }
//                streamsResponse.pagination?.cursor?.let {
//                    cursor = it
//                }
//            }?: run {
//                Toast.makeText(this@StreamsActivity, getString(R.string.error_streams), Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private fun loadMoreStreams(){
//        lifecycleScope.launch {
//            val streamsResponse: StreamsResponse? = loadStreams(cursor)     // Envio cursor
//            streamsResponse?.let {
//                streamsResponse.data?.let {streams->
//                    adapter.addStreams(streams)     // Añado mas items a los ya existentes
//                }
//                streamsResponse.pagination?.cursor?.let {
//                    cursor = it
//                }
//            }?: run {
//                Toast.makeText(this@StreamsActivity, getString(R.string.error_streams), Toast.LENGTH_SHORT).show()
//            }
//        }
//        isLoading = false
//    }



//        val httpClient = HttpClient(OkHttp) {
//            install(JsonFeature) {
//                //serializer = KotlinxSerializer()
//                //serializer = KotlinxSerializer(KotlinJson { ignoreUnknownKeys = true })
//                serializer = KotlinxSerializer(KotlinJson {
//                    isLenient = true
//                    ignoreUnknownKeys = true
//                })
//                acceptContentTypes += ContentType("application", "json+hal")
//            }
//        }



//        val twitchService = TwitchApiService(httpClient)
//
//        GlobalScope.launch(Dispatchers.Main) {
//            Log.d(TAG, "************* STREAMS (*) ******************* ")
//            //Log.d(TAG, "STREAMS: " + twitchService.getStreams(accessToken))
//
//            Log.d(TAG, "************* A ENVIAR: *************** " + accessToken)
//            var cursor = null
//            var streams = twitchService.getStreams(accessToken)
//            //streamsList = twitchService.getStreams(accessToken)
//
//            if (streams != null) {
//                Log.d(TAG, "STREAMS: " + streams.data)
//                Log.d(TAG, "STREAMS PAGINATION: " + streams.pagination)
//
//                // Envio streams al Adaptador
//                streams.data?.let { adapter.setStreams(it) }
//            }
//        }