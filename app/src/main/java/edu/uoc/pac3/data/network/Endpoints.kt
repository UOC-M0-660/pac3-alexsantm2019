package edu.uoc.pac3.data.network

/**
 * Created by alex on 07/09/2020.
 */
object Endpoints {

    // OAuth2 API Endpoints
    private const val oauthBaseUrl = "https://id.twitch.tv/oauth2"

    // TODO: Add all remaining endpoints
    const val tokensTwitch = oauthBaseUrl + "/token"

    //Revoke User tokens:
    const val revokeTokensTwitch = oauthBaseUrl + "/revoke"

    // **************** Twitch API Endpoints  ****************
    private const val twitchBaseUrl = "https://api.twitch.tv/helix"

    // TODO: Add all remaining endpoints
    //Live Streams:
    const val liveStreamsTwitch = twitchBaseUrl + "/streams"

    //Get Info User:
    const val userTwitch = twitchBaseUrl + "/users"

}