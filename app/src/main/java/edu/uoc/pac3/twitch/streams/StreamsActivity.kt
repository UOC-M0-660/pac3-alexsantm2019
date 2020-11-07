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
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.streams.StreamAdapter
import edu.uoc.pac3.twitch.profile.ProfileActivity
import kotlinx.coroutines.launch
import org.json.JSONObject

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var adapter: StreamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)

        // Obtengo como parámetro de intent desde login
        //val accessToken = intent.getStringExtra("accessToken")

        // Init RecyclerView
        initRecyclerView()

        // TODO: Get Streams
        getStreams()

    }

    private fun initRecyclerView() {
        // TODO: Implement
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Set Layout Manager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        // Init Adapter
        adapter = StreamAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun getStreams() {
        val service = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            service.getStreams()?.let { streamsResponse->

                streamsResponse.data?.let { streams->
                    adapter.setStreams(streams)
                }
            }
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

}


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