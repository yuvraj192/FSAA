package com.example.fsa

import android.content.Context
import android.graphics.Bitmap
import android.net.wifi.WifiManager
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_activity2.*


class activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity2)

        val loginIntent = intent
        val user: String?

        user = loginIntent.getStringExtra("user");

        if(webView != null){
            val webSettings  = webView!!.settings
            webView.settings.javaScriptEnabled = true

            webView!!.webViewClient = WebViewClient()
            webView!!.webChromeClient = WebChromeClient()

            webView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick () {
                    finish()
                }
            } , "close" ) ;

            webView!!.loadUrl("file:///android_asset/recieve.html")

            webView!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    webView.loadUrl("javascript:updateUser('$user') ")
                }
            }
        }



    }


    override fun onBackPressed() {
        if(webView!!.canGoBack()){
            finish()
        }else{
            super.onBackPressed()
        }
    }
}