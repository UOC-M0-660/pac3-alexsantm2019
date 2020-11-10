package edu.uoc.pac3.data

import android.content.Context
import android.net.Uri
import android.util.Log
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    //Extraigo accessToken desde SharedPreferences:
    var context: Context? = null

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
     suspend fun getStreams(cursor: String? = null): StreamsResponse? {
        Log.d(TAG, "************* ENTRE A GETSTREAMS*************** ")

        // TODO("Get Streams from Twitch")
        if(cursor == null){
            Log.d(TAG, "************* CURSOR NULO*************** ")
            var url = Endpoints.liveStreamsTwitch
            val response = httpClient.get<StreamsResponse>(url) {
                headers {
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
            Log.d(TAG, "************* FIN CURSOR NULO*************** ")

            return response
        }else{
            // TODO("Support Pagination")
            Log.d(TAG, "************* CURSOR NO NULO*************** ")
            var urlPagination = Endpoints.liveStreamsTwitch
            val response = httpClient.get<StreamsResponse>(urlPagination) {
                headers {
                    append("first", OAuthConstants.FIRST)
                    append("after", "Bearer $cursor")
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
            Log.d(TAG, "************* FIN CURSOR NO NULO*************** ")
            return response
        }

    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): UserResponse? {
        // TODO("Get User from Twitch")
        var url = Endpoints.userTwitch
        val response = httpClient.get<UserResponse>(url) {
            headers {
                append("Client-Id", OAuthConstants.clientID)
            }
        }
        return response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): UserResponse? {
        // TODO("Update User Description on Twitch")
        //var url = Endpoints.userTwitch

        var url = Uri.parse(Endpoints.userTwitch)
            .buildUpon()
            .appendQueryParameter("description", description)
            .build()

        val response = httpClient.put<UserResponse>(url.toString()) {
            headers {
                append("Client-Id", OAuthConstants.clientID)
            }
        }

//        val response = httpClient.request<UserResponse>(url.toString()) {
//            method = HttpMethod.Put
//            headers {
//                append("Client-Id", OAuthConstants.clientID)
//            }
//        }

        return response
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun revoke(accessToken: String){
        // TODO("Update User Description on Twitch")
        var url = Uri.parse(Endpoints.revokeTokensTwitch)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientID)
            .appendQueryParameter("token", accessToken)
            .build()

        httpClient.post<UserResponse>(url.toString())
    }

    @Throws(ClientRequestException::class)
    suspend fun getRefreshToken(refreshToken: String): OAuthTokensResponse? {
        var url = Uri.parse(Endpoints.tokensTwitch)
            .buildUpon()
            .appendQueryParameter("client_id", OAuthConstants.clientID)
            .appendQueryParameter("client_secret", OAuthConstants.secretClientID)
            .appendQueryParameter("refresh_token", refreshToken)
            .appendQueryParameter("grant_type", "refresh_token")
            .build()

        val response = httpClient.post<OAuthTokensResponse>(url.toString())
        return response
    }

}