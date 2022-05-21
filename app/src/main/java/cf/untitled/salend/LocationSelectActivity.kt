package cf.untitled.salend

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.WindowManager
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import cf.untitled.salend.databinding.ActivityLocationSelectBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLocationSelectBinding.inflate(layoutInflater)
        MyApplication.sharedPref = this.getSharedPreferences("location",Context.MODE_PRIVATE)
        MyApplication.edit = MyApplication.sharedPref.edit()

        setContentView(binding.root)
        var client: WebViewClient = object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return true
            }

        }

        class WebViewData {
            @JavascriptInterface
            fun getAddress(zoneCode: String, roadAddress: String, buildingName: String) {
                CoroutineScope(Dispatchers.Default).launch {
                    withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                        binding.locationTv.text = "$roadAddress"
                    }
                }
            }
        }
        binding.locationSearchWebView.apply{
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            addJavascriptInterface(WebViewData(), "Leaf")
            settings.setSupportMultipleWindows(true)
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            webViewClient = client
            webChromeClient = object: WebChromeClient(){
                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message
                ): Boolean {
                    val newWebView = WebView(this@LocationSelectActivity)
                    newWebView.settings.javaScriptEnabled = true
                    val dialog = Dialog(this@LocationSelectActivity).apply{
                        setContentView(newWebView)
                    }
                    dialog.show()
                    val lp = WindowManager.LayoutParams().apply{
                        copyFrom(dialog.window!!.attributes)
                        width = WindowManager.LayoutParams.MATCH_PARENT
                        height = WindowManager.LayoutParams.MATCH_PARENT
                    }
                    dialog.window!!.attributes = lp
                    newWebView.webChromeClient = object :
                        WebChromeClient() {
                        override fun onCloseWindow(window: WebView) {
                            dialog.dismiss()
                        }
                    }
                    (resultMsg.obj as WebView.WebViewTransport).webView = newWebView
                    resultMsg.sendToTarget ()
                    return true
                }
            }
        }
        binding.locationSearchWebView!!.loadUrl("https://salend.tk/map.html")

        binding.locationTv.text = intent.getStringExtra("location")

        binding.locationSaveBtn.setOnClickListener {
            val locationResult = binding.locationTv.text
            MyApplication.edit.putString("location", locationResult.toString())
            MyApplication.edit.apply()
            if(locationResult.isNotEmpty()) {
                val intent = Intent()
                intent.putExtra("locationResult", locationResult)
                setResult(RESULT_OK, intent)
            }else {
                setResult(RESULT_CANCELED)
            }
            finish()
        }

    }
}