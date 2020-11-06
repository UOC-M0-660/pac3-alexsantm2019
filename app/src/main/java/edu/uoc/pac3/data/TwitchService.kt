package edu.uoc.pac3.data

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    //Extraigo accessToken desde SharedPreferences:
    var context: Context? = null

    //private var sharedPreferences: SharedPreferences? = null

    /// Gets Access and Refresh Tokens on Twitch
        suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        // TODO("Get Tokens from Twitch")

        var url = Uri.parse(Endpoints.tokensTwitch)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientID)
            .appendQueryParameter("client_secret", OAuthConstants.secretClientID)
            .appendQueryParameter("code", authorizationCode)
            .appendQueryParameter("grant_type", "authorization_code")
            .appendQueryParameter("redirect_uri", OAuthConstants.redirectUri)
            .build()

        val response = httpClient.post<OAuthTokensResponse>(url.toString()) {
            headers {
                append("Authorization", "token")
            }
            body = "Command"
        }
        return response
    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
//    suspend fun getStreams(cursor: String? = null): StreamsResponse? {
     suspend fun getStreams(cursor: String? = null, accessToken: String? = null): StreamsResponse? {
        Log.d(TAG, "************* ENTRE A GETSTREAMS*************** ")

       // var accessToken = context?.let { SessionManager(it).getAccessToken() }

        val sharedPreference = context?.let { SessionManager(it) }
        var tokenAccedido = sharedPreference?.getAccessToken()      //====================> arreglar

        //var accessToken = sharedPreferences?.getAccessToken()
//        var accessToken = if (sharedPreferences!!.contains("accessToken")) sharedPreferences!!.getString(
//            "accessToken",
//            ""
//        ) else null
        Log.d(TAG, "************* FIN ENTRE A GETSTREAMS*************** ")
        Log.d(TAG, "ACCESS TOKEN: " + accessToken)

        // TODO("Get Streams from Twitch")
        if(cursor == null){
            var url = Endpoints.liveStreamsTwitch
            val response = httpClient.request<StreamsResponse>(url) {
                method = HttpMethod.Get
                headers {
                    append("Authorization", "Bearer " + accessToken)
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
            return response
        }else{
            var urlPagination = Endpoints.liveStreamsTwitch
            val response = httpClient.request<StreamsResponse>(urlPagination.toString()) {
                method = HttpMethod.Get
                headers {
                    append("first", 20.toString())
                    append("after", "Bearer " + cursor)
                    append("Authorization", "Bearer " + tokenAccedido)
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
            return response
        }
        // TODO("Support Pagination")

    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
//    suspend fun getUser(): User? {
        suspend fun getUser() {
        // TODO("Get User from Twitch")
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
//    suspend fun updateUserDescription(description: String): User? {
        suspend fun updateUserDescription(description: String) {
        // TODO("Update User Description on Twitch")
    }
}