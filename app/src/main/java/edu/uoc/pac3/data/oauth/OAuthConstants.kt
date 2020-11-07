package edu.uoc.pac3.data.oauth

import java.util.*

/**
 * Created by alex on 07/09/2020.
 */
object OAuthConstants {

    // TODO: Set OAuth2 Variables
    const val clientID = "5oguujunmfy2vqgxjiwujhq4c6r2wy"
    const val  secretClientID = "wg982f962q5puw0hb9ujzt4ucqwbe0"
    const val  redirectUri = "http://localhost"
    const val  authorizationUrl ="https://id.twitch.tv/oauth2/authorize"
    val  scopes: List<String> = listOf("user:read:email", "user:edit")
    val  uniqueState = UUID.randomUUID().toString()

    const val SCOPE = "scope"
    const val STATE = "state"
    const val CODE = "code"
    const val FIRST = 100.toString()

}