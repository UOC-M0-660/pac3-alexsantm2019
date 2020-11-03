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

    fun isUserAvailable(): Boolean {
        // TODO: Implement
        return false
    }

    fun getAccessToken(): String? {
        // TODO: Implement
        var name = "accessToken"
        if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null
        return null
    }

    fun saveAccessToken(accessToken: String) {
        // TODO("Save Access Token")
        val saveToken = sharedPreferences!!.edit()
        saveToken.putString("accessToken", accessToken)
        saveToken.apply()

    }

    fun clearAccessToken() {
        TODO("Clear Access Token")
    }

    fun getRefreshToken(): String? {
        // TODO("Get Refresh Token")
        var name = "refreshToken"
        if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null
    }

    fun saveRefreshToken(refreshToken: String) {
        // TODO("Save Refresh Token")
        val saveRefreshToken = sharedPreferences!!.edit()
        saveRefreshToken.putString("refreshToken", refreshToken)
        saveRefreshToken.apply()
    }

    fun clearRefreshToken() {
       // TODO("Clear Refresh Token")
        sharedPreferences = context.getSharedPreferences("refreshToken", 0).edit().clear().commit()
        sharedPreferences.edit().remove("refreshToken").commit();
    }

}