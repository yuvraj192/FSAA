package com.example.fsa

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_activity2.*


class activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity2)

        if(webView != null){
            val webSettings  = webView!!.settings
            webView.settings.javaScriptEnabled = true

            webView!!.webViewClient = WebViewClient()
            webView!!.webChromeClient = WebChromeClient()
            webView!!.loadUrl("file:///android_asset/recieve.html")

            webView!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
        }
    }

    override fun onBackPressed() {
        if(webView!!.canGoBack()){
            webView!!.goBack()
        }else{
            super.onBackPressed()
        }
    }
}