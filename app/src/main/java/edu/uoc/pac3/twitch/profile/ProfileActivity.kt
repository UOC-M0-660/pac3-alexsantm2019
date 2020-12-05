package edu.uoc.pac3.twitch.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import edu.uoc.pac3.LaunchActivity
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.user.User
import io.ktor.client.features.*
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val TAG = "ProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        getUserInfo()
    }

    private fun getUserInfo() {
        val service = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            service.getUser()?.let { user ->
                updateInterfaz(user)       // Actualizo interfaz
            }
        }
    }

    private fun updateInterfaz(user: User?) {
        val userName: TextView = findViewById(R.id.userNameTextView)
        val userViewCount: TextView = findViewById(R.id.viewsText)
        val userDescription: TextInputEditText = findViewById(R.id.userDescriptionEditText)
        val imageThumbnail: ImageView = findViewById(R.id.userImageView)

        if (user != null) {
            userName.text = user.displayName
        }
        if (user != null) {
            userViewCount.text = user.viewCount.toString()
        }
        if (user != null) {
            userDescription.setText(user.description)
        }

        val profileImg = user?.profileImageUrl
        if (profileImg !== null) {
            Glide.with(this@ProfileActivity)
                .load(profileImg)
                .into(imageThumbnail)
        } else {
            Toast.makeText(
                applicationContext,
                getString(R.string.image_error_load), Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun updateUserDescription(view: View) {
        val serviceUpdate = TwitchApiService(Network.createHttpClient(this))

        //Traigo el valor actual de Description
        val userDescriptionText: TextInputEditText = findViewById(R.id.userDescriptionEditText)
        val userDescription = userDescriptionText.text.toString()

        // get reference to button
        val updateDescriptionButton = findViewById<Button>(R.id.updateDescriptionButton)

        // set on-click listener
        updateDescriptionButton.setOnClickListener {

            if (!userDescription.isNullOrBlank()) {
                try {
                    lifecycleScope.launch {
                        serviceUpdate.updateUserDescription(userDescription)?.let { userResponse ->
                            userResponse?.let { userInfo ->

                                // OK Descripcion actualizada correctamente
                                Snackbar.make(
                                    view, getString(R.string.description_success),
                                    Snackbar.LENGTH_LONG
                                )
                                    .setAction("Action", null).show()
                            }
                        }
                    }
                } catch (e: ClientRequestException) {
                    if (e is ClientRequestException && e.response?.status?.value == 401) {
                        lifecycleScope.launch {
                            refreshToken(serviceUpdate)
                            logOut(view)
                        }
                    }
                }
            } else {
                Snackbar.make(
                    view, getString(R.string.no_description),
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Action", null).show()
            }
        }
    }

    suspend fun refreshToken(service: TwitchApiService) {
        val sessionManager = SessionManager(this)
        sessionManager.clearAccessToken()
        try {
            sessionManager.getRefreshToken()?.let {
                service.getRefreshToken(it)?.let { tokensResponse ->
                    sessionManager.saveAccessToken(tokensResponse.accessToken)
                    tokensResponse.refreshToken?.let {
                        sessionManager.saveRefreshToken(it)
                    }
                }
            }
        } catch (e: ClientRequestException) {
            // En caso de no poder obtener accessToken hago LogOut para que el usuario haga Login de nuevo
            Toast.makeText(
                applicationContext,
                getString(R.string.error_refresh_token),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun logOut(view: View) {
        val sharedPreference = SessionManager(this@ProfileActivity)
        sharedPreference.clearAccessToken()
        sharedPreference.clearRefreshToken()

        val twitchService = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            val accessToken = SessionManager(this@ProfileActivity).getAccessToken()
            if (accessToken != null) {
                twitchService.revoke(accessToken)
            }
        }
        val intent = Intent(this, LaunchActivity::class.java)
        startActivity(intent)
        finish()
    }

}