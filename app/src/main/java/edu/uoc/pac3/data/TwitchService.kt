package edu.uoc.pac3.data

import android.net.Uri
import android.util.Log
import android.widget.Toast
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.oauth.OAuthActivity
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    //Extraigo codigo de autorizacion de OAuthActivity
//    var mApp = OAuthActivity()
//    var authCode = mApp.authorizationCode

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

        // TODO("Get Streams from Twitch")
        var url = Endpoints.liveStreamsTwitch
        val response = httpClient.request<StreamsResponse>(url.toString()) {
                method = HttpMethod.Get
                headers {
                    append("Authorization", "Bearer "+ cursor)
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
        return response

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