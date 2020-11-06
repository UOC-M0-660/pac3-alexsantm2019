package edu.uoc.pac3.twitch.streams

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uoc.pac3.R
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.streams.StreamAdapter
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json as KotlinJson

class StreamsActivity : AppCompatActivity() {

    private val TAG = "StreamsActivity"

    private lateinit var adapter: StreamAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_streams)

        // Obtengo como parámetro de intent desde login
        val accessToken = intent.getStringExtra("accessToken")

        // Init RecyclerView
        initRecyclerView()

        // TODO: Get Streams
        val httpClient = HttpClient(OkHttp) {
            install(JsonFeature) {
                //serializer = KotlinxSerializer()
                //serializer = KotlinxSerializer(KotlinJson { ignoreUnknownKeys = true })
                serializer = KotlinxSerializer(KotlinJson {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
                acceptContentTypes += ContentType("application", "json+hal")
            }
        }

        val twitchService = TwitchApiService(httpClient)

        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "************* STREAMS (*) ******************* ")
            //Log.d(TAG, "STREAMS: " + twitchService.getStreams(accessToken))

            Log.d(TAG, "************* A ENVIAR: *************** " + accessToken)
            var cursor = null
            var streams = twitchService.getStreams(accessToken)
            //streamsList = twitchService.getStreams(accessToken)

            if (streams != null) {
                Log.d(TAG, "STREAMS: " + streams.data)
                Log.d(TAG, "STREAMS PAGINATION: " + streams.pagination)

                // Envio streams al Adaptador
                streams.data?.let { adapter.setStreams(it) }
            }
        }

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

}