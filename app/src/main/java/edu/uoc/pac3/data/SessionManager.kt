package edu.uoc.pac3.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    val context: Context = context

    private var ACCESS_TOKEN =  "accessToken"
    private var REFRESH_TOKEN =  "refreshToken"
    private var TOKEN =  "token"
    //private var sharedPreferences: SharedPreferences? = null
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Implement
//        val preferences: SharedPreferences = context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
//        val accessToken = preferences.getString(ACCESS_TOKEN, null) //second parameter default value.
//        return accessToken


        val accessToken = sharedPreferences?.getString(ACCESS_TOKEN, null) //second parameter default value.
        return accessToken
    }

    fun saveAccessToken(accessToken: String) {
        // TODO("Save Access Token")
//        val preferences: SharedPreferences = context.getSharedPreferences(
//            ACCESS_TOKEN,
//            Context.MODE_PRIVATE
//        )
//        preferences.edit().putString(ACCESS_TOKEN, accessToken).apply()


        sharedPreferences?.edit()?.putString(ACCESS_TOKEN, accessToken)?.apply()
    }

    fun clearAccessToken() {
        // TODO("Clear Access Token")
        context.getSharedPreferences(ACCESS_TOKEN, 0).edit().clear().apply()
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        //return sharedPref.getString(KEY_NAME, null)
//        return if (sharedPreferences!!.contains(REFRESH_TOKEN)) sharedPreferences!!.getString(
//            REFRESH_TOKEN,
//            ""
//        ) else null

//        val preferences: SharedPreferences = context.getSharedPreferences(REFRESH_TOKEN, Context.MODE_PRIVATE)
//        val refreshToken = preferences.getString(REFRESH_TOKEN, null) //second parameter default value.
//        return refreshToken

        val refreshToken = sharedPreferences?.getString(REFRESH_TOKEN, null) //second parameter default value.
        return refreshToken

    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
//        val preferences: SharedPreferences = context.getSharedPreferences(
//            REFRESH_TOKEN,
//            Context.MODE_PRIVATE
//        )
//        preferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()

        sharedPreferences?.edit()?.putString(REFRESH_TOKEN, refreshToken)?.apply()
    }

    fun clearRefreshToken() {
       // TODO("Clear Refresh Token")
        context.getSharedPreferences(REFRESH_TOKEN, 0).edit().clear().apply()

//        val editor: SharedPreferences.Editor = sharedPreferences.edit().remove(REFRESH_TOKEN).apply()
//        editor.remove(REFRESH_TOKEN)
//        editor.commit()
    }

}