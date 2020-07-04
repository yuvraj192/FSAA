package com.example.fsa

import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat


import android.Manifest

import android.content.BroadcastReceiver

import android.content.Context
import android.content.Intent

import android.content.IntentFilter

import android.content.pm.PackageManager
import android.graphics.Bitmap

import android.net.wifi.p2p.WifiP2pDevice

import android.net.wifi.p2p.WifiP2pDeviceList

import android.net.wifi.p2p.WifiP2pManager

import android.os.Bundle

import android.view.View

import android.widget.ArrayAdapter

import android.widget.Button

import android.widget.EditText

import android.widget.ListView

import android.widget.TextView

import android.net.wifi.WifiManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(loginView != null){
            val webSettings  = loginView!!.settings
            loginView.settings.javaScriptEnabled = true
            loginView!!.webViewClient = WebViewClient()
            loginView!!.webChromeClient = WebChromeClient()

            loginView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick (strl: String?) {
                    //Toast.makeText(this@MainActivity, strl, Toast.LENGTH_SHORT).show()
                    goHome(strl)
                }
            } , "valid" ) ;

            loginView!!.loadUrl("file:///android_asset/login.html")


            loginView!!.webViewClient = object: WebViewClient(){
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                }
            }
        }
    }

    private fun goHome(usr: String?){
        val intent = Intent(this, home::class.java)
        intent.putExtra("user", usr)
        startActivity(intent);
        finish()
    }

    override fun onBackPressed() {
        if(loginView!!.canGoBack()){
            loginView!!.goBack()
        }else{
            super.onBackPressed()
        }
    }

}