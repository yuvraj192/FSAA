package com.example.fsa

import com.example.fsa.WifiDirectBroadcastReceiver
import android.Manifest
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager.PeerListListener
import android.os.Bundle
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_activity2.*
import java.util.*

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pManager
import android.widget.Toast

class WifiDirectBroadcastReceiver(
    private val mManager: WifiP2pManager?,
    private val mChannel: WifiP2pManager.Channel,
    private val mActivity: SendActivity
) :
    BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION == action) {
            val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "WiFi is ON", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "WiFi is OFF", Toast.LENGTH_SHORT).show()
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION == action) {
            if (mManager != null) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                } else {
                    mManager.requestPeers(mChannel, mActivity.peerListListener)
                }
            }
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION == action) {
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION == action) {
        }
    }

}

class SendActivity : AppCompatActivity() {
    var wifiManager: WifiManager? = null

    var mManager: WifiP2pManager? = null
    var mChannel: WifiP2pManager.Channel? = null

    var mReceiver: BroadcastReceiver? = null
    var mIntentFilter: IntentFilter? = null

    private val peers = mutableListOf<WifiP2pDevice>()
    protected lateinit var deviceNameArray: Array<String>
    protected lateinit var deviceArray: Array<WifiP2pDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        mManager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        mChannel = mManager!!.initialize(this, mainLooper, null)

        mReceiver = WifiDirectBroadcastReceiver(mManager,  mManager!!.initialize(this, mainLooper, null), this)

        mIntentFilter = IntentFilter()
        mIntentFilter!!.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        mIntentFilter!!.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        mIntentFilter!!.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        mIntentFilter!!.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)


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
                    //Toast.makeText(this@MainActivity, strl, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } , "close" ) ;
            webView.addJavascriptInterface(object : Any() {
                @JavascriptInterface
                fun performClick () {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(applicationContext, "Permission Error", Toast.LENGTH_SHORT).show()
                    } else {
                        mManager?.discoverPeers(mChannel, object : WifiP2pManager.ActionListener {
                            override fun onSuccess() {
                                Toast.makeText(applicationContext, "Discovery Started", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(i: Int) {
                                Toast.makeText(applicationContext, "Discovery Failed", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            } , "discover" ) ;

            webView!!.loadUrl("file:///android_asset/send.html")

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
    var peerListListener =
        PeerListListener { peerList ->
            if (peerList.deviceList != peers) {
                peers.clear()
                peers.addAll(peerList.deviceList)
                //deviceNameArray = Array<String>(peerList.deviceList.size)
                //deviceArray = Array<WifiP2pDevice>(peerList.deviceList.size)
                var index = 0
                for (device in peerList.deviceList) {
                    addReceiver(device.deviceName)
                    //Toast.makeText(applicationContext, device.deviceAddress, Toast.LENGTH_SHORT).show()
                    index++
                }
            }
            if (peers.size == 0) {
                Toast.makeText(applicationContext, "No Device Found", Toast.LENGTH_LONG).show()
            }
        }

    override fun onBackPressed() {
        if(webView!!.canGoBack()){
            finish()
        }else{
            super.onBackPressed()
        }
    }

    private fun addReceiver(user: String?){
        webView.loadUrl("javascript:addReciever('$user')")
    }
    override fun onResume() {
        super.onResume()
        registerReceiver(mReceiver, mIntentFilter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mReceiver)
    }
}

