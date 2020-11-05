package edu.uoc.pac3.data

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.system.Os.remove
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by alex on 06/09/2020.
 */

class SessionManager(context: Context) {

    val context: Context = context
    private var sharedPreferences: SharedPreferences? = null
    private var ACCESS_TOKEN =  "accessToken"
    private var REFRESH_TOKEN =  "refreshToken"

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Implement
        return if (sharedPreferences!!.contains(ACCESS_TOKEN)) sharedPreferences!!.getString(ACCESS_TOKEN, "") else null
        //return null
    }

    fun saveAccessToken(accessToken: String) {
        // TODO("Save Access Token")
//        val saveToken = sharedPreferences!!.edit()
//        saveToken.putString(ACCESS_TOKEN, accessToken)
//        saveToken.commit()
        //saveToken.apply()

        val sharedPref: SharedPreferences = context.getSharedPreferences(ACCESS_TOKEN, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(ACCESS_TOKEN, accessToken)
        editor.commit()
    }

    fun clearAccessToken() {
        // TODO("Clear Access Token")
        //context.getSharedPreferences(ACCESS_TOKEN, 0).edit().clear().apply()
        context.getSharedPreferences(ACCESS_TOKEN, 0).edit().clear().commit()
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        //return sharedPref.getString(KEY_NAME, null)
        return if (sharedPreferences!!.contains(REFRESH_TOKEN)) sharedPreferences!!.getString(REFRESH_TOKEN, "") else null
        //return null
    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
//        val saveRefreshToken = sharedPreferences!!.edit()
//        saveRefreshToken.putString(REFRESH_TOKEN, refreshToken)
//        saveRefreshToken.commit()

        val sharedPref: SharedPreferences = context.getSharedPreferences(REFRESH_TOKEN, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(REFRESH_TOKEN, refreshToken)
        editor.commit()
    }

    fun clearRefreshToken() {
       // TODO("Clear Refresh Token")
        //context.getSharedPreferences(REFRESH_TOKEN, 0).edit().clear().apply()
        context.getSharedPreferences(REFRESH_TOKEN, 0).edit().clear().commit()
    }

}