package edu.uoc.pac3.data

import android.net.Uri
import android.util.Log
import android.widget.Toast
import edu.uoc.pac3.data.network.Endpoints
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.data.oauth.OAuthTokensResponse
import edu.uoc.pac3.data.oauth.UnauthorizedException
import edu.uoc.pac3.data.streams.StreamsResponse
import edu.uoc.pac3.data.user.User
import edu.uoc.pac3.data.user.UserResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by alex on 24/10/2020.
 */

class TwitchApiService(private val httpClient: HttpClient) {
    private val TAG = "TwitchApiService"

    /// Gets Access and Refresh Tokens on Twitch
        @Throws(ClientRequestException::class)
        suspend fun getTokens(authorizationCode: String): OAuthTokensResponse? {
        // TODO("Get Tokens from Twitch")

        val response = httpClient.post<OAuthTokensResponse>(Endpoints.liveStreamsTwitch) {
            parameter("client_id", OAuthConstants.clientID)
            parameter("client_secret", OAuthConstants.secretClientID)
            parameter("code", authorizationCode)
            parameter("grant_type", "authorization_code")
            parameter("redirect_uri", OAuthConstants.redirectUri)
        }
        return response
    }

    /// Gets Streams on Twitch
    @Throws(UnauthorizedException::class)
     suspend fun getStreams(cursor: String? = null): StreamsResponse? {

        // TODO("Get Streams from Twitch")
        if(cursor == null){
            var url = Endpoints.liveStreamsTwitch
            val response = httpClient.get<StreamsResponse>(url) {
                headers {
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
            return response
        }else{
            // TODO("Support Pagination")
            var urlPagination = Endpoints.liveStreamsTwitch
            val response = httpClient.get<StreamsResponse>(urlPagination) {
                headers {
                    append("first", OAuthConstants.FIRST)
                    append("after", "$cursor")
                    append("Client-Id", OAuthConstants.clientID)
                }
            }
            return response
        }
    }

    // Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun getUser(): UserResponse?  = with (Dispatchers.IO){
        // TODO("Get User from Twitch")
        var url = Endpoints.userTwitch
        val response = httpClient.get<UserResponse>(url) {
            headers {
                append("Client-Id", OAuthConstants.clientID)
            }
        }
        return response
    }

    @Throws(UnauthorizedException::class)
    suspend fun updateUserDescription(description: String): User? {
        val users = withContext(Dispatchers.IO){
            return@withContext try {
                httpClient.put<UserResponse>(Endpoints.userTwitch) {
                    parameter("description", description)
                    headers {
                        append("Client-Id", OAuthConstants.clientID)
                    }
                }
            }catch (e: Exception){
                e.printStackTrace()
                if (e is ClientRequestException && e.response?.status?.value == 401){
                    throw UnauthorizedException
                }
                null
            }
        }
        users?.data?.let {
            return it[0]
        }
        return null
    }

    /// Gets Current Authorized User on Twitch
    @Throws(UnauthorizedException::class)
    suspend fun revoke(accessToken: String){
        // TODO("Update User Description on Twitch")
        httpClient.post<UserResponse>(Endpoints.revokeTokensTwitch) {
            header("client_id", OAuthConstants.clientID)
            parameter("token", accessToken)
        }
    }

    @Throws(ClientRequestException::class)
    suspend fun getRefreshToken(refreshToken: String): OAuthTokensResponse? {
        val response = httpClient.post<OAuthTokensResponse>(Endpoints.tokensTwitch) {
            parameter("client_id", OAuthConstants.clientID)
            parameter("client_secret", OAuthConstants.secretClientID)
            parameter("refresh_token", refreshToken)
            parameter("grant_type", "refresh_token")
        }
        return response
    }

}