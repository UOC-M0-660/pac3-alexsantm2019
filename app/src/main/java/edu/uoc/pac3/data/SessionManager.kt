package edu.uoc.pac3.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import kotlinx.coroutines.CoroutineScope

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    val context: Context = context

    private var ACCESS_TOKEN =  "accessToken"
    private var REFRESH_TOKEN =  "refreshToken"
    private var TOKEN =  "token"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Implement
        val accessToken = sharedPreferences?.getString(ACCESS_TOKEN, null) //second parameter default value.
        return accessToken
    }

    fun saveAccessToken(accessToken: String) {
        // TODO("Save Access Token")
        sharedPreferences?.edit()?.putString(ACCESS_TOKEN, accessToken)?.apply()
    }

    fun clearAccessToken() {
        // TODO("Clear Access Token")
        context.getSharedPreferences(ACCESS_TOKEN, 0).edit().clear().apply()
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        val refreshToken = sharedPreferences?.getString(REFRESH_TOKEN, null) //second parameter default value.
        return refreshToken

    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
        sharedPreferences?.edit()?.putString(REFRESH_TOKEN, refreshToken)?.apply()
    }

    fun clearRefreshToken() {
       // TODO("Clear Refresh Token")
        context.getSharedPreferences(REFRESH_TOKEN, 0).edit().clear().apply()
    }

}