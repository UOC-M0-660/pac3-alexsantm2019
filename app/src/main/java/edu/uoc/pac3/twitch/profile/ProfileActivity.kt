package edu.uoc.pac3.twitch.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
            service.getUser()?.let { userResponse->

                userResponse.data?.let { userInfo->

                    val userName: TextView = findViewById(R.id.userNameTextView)
                    val userViewCount: TextView = findViewById(R.id.viewsText)
                    val userDescription: TextInputEditText = findViewById(R.id.userDescriptionEditText)
                    val imageThumbnail: ImageView = findViewById(R.id.userImageView)

                    userName.text = userInfo[0].display_name
                    userViewCount.text = userInfo[0].view_count.toString()
                    userDescription.setText(userInfo[0].description)

                    val profileImg = userInfo[0].profile_image_url
                    if (profileImg !== null) {
                        Glide.with(this@ProfileActivity)
                            .load(profileImg)
                            .into(imageThumbnail)
                    }
                }

            }
        }
    }

    fun updateUserDescription(view:View) {
        val serviceUpdate = TwitchApiService(Network.createHttpClient(this))

        //Traigo el valor actual de Description
        val userDescriptionText: TextInputEditText = findViewById(R.id.userDescriptionEditText)
        val userDescription = userDescriptionText.text.toString()

        // get reference to button
        val updateDescriptionButton = findViewById<Button>(R.id.updateDescriptionButton)

        // set on-click listener
        updateDescriptionButton.setOnClickListener {

            if (!userDescription.isNullOrBlank()){

                lifecycleScope.launch {
                    serviceUpdate.updateUserDescription(userDescription)?.let { userResponse->

                        userResponse?.let { userInfo->
                            Snackbar.make(view, "Descripcion actualizada correctamente",
                                Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                        }
                    }
                }

            }else{
                Snackbar.make(view, "No ha ingresado descripcion",
                    Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
        }

    }

    fun logOut(view:View) {
        val sharedPreference = SessionManager(this@ProfileActivity)
        sharedPreference.clearAccessToken()
        sharedPreference.clearRefreshToken()

        val twitchService = TwitchApiService(Network.createHttpClient(this))
        lifecycleScope.launch {
            val accessToken = SessionManager(this@ProfileActivity).getAccessToken()
            if (accessToken != null) {

                twitchService.revoke(accessToken)
//                twitchService.revoke(accessToken)?.let { userResponse->
//                    userResponse?.let { userInfo->
//                        Snackbar.make(view, "Revokado",
//                            Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show()
//                    }
//                }
            }
        }

        val intent = Intent(this, LaunchActivity::class.java)
        startActivity(intent)
    }

}