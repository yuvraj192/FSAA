package com.example.fsa

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_activity2.*

class SendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        if(webView != null){
            val webSettings  = webView!!.settings
            webView.settings.javaScriptEnabled = true

            webView!!.webViewClient = WebViewClient()
            webView!!.webChromeClient = WebChromeClient()

            webView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick () {
                    //Toast.makeText(this@MainActivity, strl, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } , "close" ) ;

            webView!!.loadUrl("file:///android_asset/send.html")

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
            finish()
        }else{
            super.onBackPressed()
        }
    }
}