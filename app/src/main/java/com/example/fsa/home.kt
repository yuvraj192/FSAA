package com.example.fsa

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
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

        profilePicture.setOnClickListener{
            openActivity2()
        }

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

    private fun openActivity2(){
        val intent = Intent(this, activity2::class.java)
        startActivity(intent)
    }
}