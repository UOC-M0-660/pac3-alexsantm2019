package edu.uoc.pac3.oauth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import edu.uoc.pac3.R
import edu.uoc.pac3.data.SessionManager
import edu.uoc.pac3.data.TwitchApiService
import edu.uoc.pac3.data.network.Network
import edu.uoc.pac3.data.oauth.OAuthConstants
import edu.uoc.pac3.twitch.streams.StreamsActivity
import kotlinx.android.synthetic.main.activity_oauth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OAuthActivity : AppCompatActivity() {

    private val TAG = "OAuthActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        launchOAuthAuthorization()
    }

    fun buildOAuthUri(): Uri? {
        // TODO: Create URI
        var url = Uri.parse(OAuthConstants.authorizationUrl)
                .buildUpon()
                .appendQueryParameter("client_id", OAuthConstants.clientID)
                .appendQueryParameter("redirect_uri", OAuthConstants.redirectUri)
                .appendQueryParameter("response_type", OAuthConstants.CODE)
                .appendQueryParameter("scope", OAuthConstants.scopes.joinToString(separator = " "))
                .appendQueryParameter("state", OAuthConstants.uniqueState)
                .build()
       return url
    }

    private fun launchOAuthAuthorization() {
        //  Create URI
        var uri = buildOAuthUri()

        // TODO: Set webView Redirect Listener
        webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    //Toast.makeText( applicationContext, "shouldOverrideUrlLoading", Toast.LENGTH_SHORT).show()
                request?.let {
                    // Check if this url is our OAuth redirect, otherwise ignore it
                    if (request.url.toString().startsWith(OAuthConstants.redirectUri)) {
                        // To prevent CSRF attacks, check that we got the same state value we sent, otherwise ignore it
                        val responseState = request.url.getQueryParameter("state")
//                        if (responseState != null) {
                            if (responseState == OAuthConstants.uniqueState) {
                            // This is our request, obtain the code!
                            request.url.getQueryParameter("code")?.let { code ->
                                // Got it!

                                GlobalScope.launch(Dispatchers.Main) {
                                    onAuthorizationCodeRetrieved(code)

                                    webView.visibility = View.GONE
                                    progressBar.visibility = View.VISIBLE
                                }

                            } ?: run {
                                // User cancelled the login flow
                                // TODO: Handle error
                                Toast.makeText( applicationContext, "No code available", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Log.d("OAuth", "No request")
                        Toast.makeText(applicationContext, "No hay request", Toast.LENGTH_SHORT).show()
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        // Load OAuth Uri
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(uri.toString())

    }

    // Call this method after obtaining the authorization code
    // on the WebView to obtain the tokens
    private suspend fun onAuthorizationCodeRetrieved(authorizationCode: String) {

        // Show Loading Indicator
        progressBar.visibility = View.VISIBLE

        // TODO: Create Twitch Service
        val twitchService = TwitchApiService(Network.createHttpClient(this))

        // TODO: Get Tokens from Twitch
        lifecycleScope.launch {
            var valuesTokens = twitchService.getTokens(authorizationCode)

            // TODO: Save access token and refresh token using the SessionManager class
            var accessToken = valuesTokens?.accessToken
            var refreshToken = valuesTokens?.refreshToken

            val sharedPreference = SessionManager(this@OAuthActivity)
            if (accessToken != null) {
                //Toast.makeText( applicationContext, "accessToken $accessToken", Toast.LENGTH_SHORT).show()
                sharedPreference.saveAccessToken(accessToken)
            }
            if (refreshToken != null) {
                sharedPreference.saveRefreshToken(refreshToken)
            }
            webView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

        goToStreamActivity()
    }

    private fun goToStreamActivity() {
        // Ir a StreamsActivity

        val intent = Intent(this, StreamsActivity::class.java)
        //intent.putExtra("accessToken",accessToken)
        startActivity(intent)
    }
}
