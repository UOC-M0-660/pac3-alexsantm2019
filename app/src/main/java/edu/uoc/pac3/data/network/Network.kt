package edu.uoc.pac3.data.network

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.oauth.LoginActivity
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * Created by alex on 07/09/2020.
 */
object Network {

    private const val TAG = "Network"
    fun createHttpClient(context: Context): HttpClient {
        //val context = context
        val accessToken = SessionManager(context).getAccessToken()

        return HttpClient(OkHttp) {
            // Setup HttpClient
            install(JsonFeature){
                serializer = KotlinxSerializer(json)
            }
            // Logging
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Ktor", message)
                    }
                }
                level = LogLevel.ALL
            }
            // Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = 15000L
                connectTimeoutMillis = 15000L
                socketTimeoutMillis = 15000L
            }
            // Apply to All Requests
            defaultRequest {
                // Content Type
                if (this.method != HttpMethod.Get) contentType(ContentType.Application.Json)
                if (accessToken != null) {
                    if (accessToken.isNotEmpty()) header("Authorization", "Bearer $accessToken")
                }
                accept(ContentType.Application.Json)
            }
            HttpResponseValidator {
                validateResponse { response ->
                    val statusCode = response.status.value
//                    when (statusCode) {
//                        in 400..499 -> throw ClientRequestException(response)
//                    }
                    if (statusCode == 401) {
                        //throw ResponseException(response)
                        renewToken(context)
                    }
                }
            }
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = false
    }

    private suspend fun renewToken(context: Context){
        val instanceClient = createHttpClient(context)
        val service = TwitchApiService(instanceClient)
        val sessionManager = SessionManager(context )
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
        }catch (e: ClientRequestException){
            // En caso de no poder obtener accessToken hago LogOut para que el usuario haga Login de nuevo
            logout(context)
        }
    }

    private fun logout(context: Context){
        val sessionManager = SessionManager(context)
        sessionManager.clearAccessToken()
        sessionManager.clearRefreshToken()
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}