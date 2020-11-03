package edu.uoc.pac3.data.oauth

import java.util.*

/**
 * Created by alex on 07/09/2020.
 */
object OAuthConstants {

    // TODO: Set OAuth2 Variables
    var clientID = "5oguujunmfy2vqgxjiwujhq4c6r2wy"
    var secretClientID = "wg982f962q5puw0hb9ujzt4ucqwbe0"
    var redirectUri = "http://localhost"
    var uniqueState = UUID.randomUUID().toString()
    var authorizationUrl ="https://id.twitch.tv/oauth2/authorize"
    var scopes: List<String> = listOf("user:read:email", "user:edit")

}