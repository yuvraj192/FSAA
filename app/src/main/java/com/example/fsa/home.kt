package com.example.fsa

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*

class home : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val loginIntent = intent
        val user: String?

        user = loginIntent.getStringExtra("user");

        username.setText(user)

        if(homeContent != null || btmBar != null){
            val homeSettings  = homeContent!!.settings
            val btmBarSettings = btmBar!!.settings

            homeContent.settings.javaScriptEnabled = true
            homeContent!!.webViewClient = WebViewClient()
            homeContent!!.webChromeClient = WebChromeClient()

            btmBar.settings.javaScriptEnabled = true
            btmBar!!.webViewClient = WebViewClient()
            btmBar!!.webChromeClient = WebChromeClient()

            homeContent!!.loadUrl("file:///android_asset/index.html")

            btmBar.addJavascriptInterface(object : Any() {
                    @JavascriptInterface
                    fun performClick () {
                        //Toast.makeText(this@MainActivity, strl, Toast.LENGTH_SHORT).show()
                        openSendWindow(user)
                    }
                } , "send" ) ;

            btmBar.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick () {
                    //Toast.makeText(this@MainActivity, strl, Toast.LENGTH_SHORT).show()
                    openRecieveWindow(user)
                }
            } , "recieve" ) ;

            btmBar!!.loadUrl("file:///android_asset/bottombar.html")


            homeContent!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }

            btmBar!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
        }
    }


    private fun openRecieveWindow(user: String?){
        val intent = Intent(this, activity2::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun openSendWindow(user: String?){
        val intent = Intent(this, SendActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }


}