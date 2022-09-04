package cf.untitled.salend

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
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

//웹뷰를 활용한 위치를 선택하는 액티비티
class LocationSelectActivity : AppCompatActivity() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLocationSelectBinding.inflate(layoutInflater)
        MyApplication.sharedPref = this.getSharedPreferences("location", Context.MODE_PRIVATE)
        MyApplication.edit = MyApplication.sharedPref.edit()

        setContentView(binding.root)
        val client: WebViewClient = object : WebViewClient() {
            @TargetApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }

        }

        class WebViewData {
            @SuppressLint("SetTextI18n")
            @JavascriptInterface
            fun getAddress(
                zoneCode: String,
                roadAddress: String,
                buildingName: String,
                x: String,
                y: String
            ) {
                try {
                    CoroutineScope(Dispatchers.Default).launch {
                        withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                            val list = roadAddress.split(" ")
                            binding.locationTv.text = list[0] + " " + list[1] + " " + list[2]
                            latitude = y.toDouble()
                            longitude = x.toDouble()
                            saveLocation()
                        }
                    }
                } catch (e: Exception) {
                    Log.d("locationSelectError", "$e")
                }
            }

            private fun saveLocation() {
                val locationResult = binding.locationTv.text
                MyApplication.edit.putString("location", locationResult.toString())
                MyApplication.edit.putString("latitude", latitude.toString())
                MyApplication.edit.putString("longitude", longitude.toString())
                MyApplication.edit.apply()

                if (locationResult.isNotEmpty()) {
                    val intent = Intent()
                    intent.putExtra("locationResult", locationResult)
                    setResult(RESULT_OK, intent)
                } else {
                    setResult(RESULT_CANCELED)
                }
                finish()
            }
        }

        binding.locationSearchWebView.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            addJavascriptInterface(WebViewData(), "Leaf")
            settings.setSupportMultipleWindows(true)
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            webViewClient = client
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message
                ): Boolean {
                    val newWebView = WebView(this@LocationSelectActivity)
                    true.also { newWebView.settings.javaScriptEnabled = it }
                    val dialog = Dialog(this@LocationSelectActivity).apply {
                        setContentView(newWebView)
                    }
                    dialog.show()
                    val lp = WindowManager.LayoutParams().apply {
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
                    resultMsg.sendToTarget()
                    return true
                }
            }
        }
        binding.locationSearchWebView.loadUrl("https://salend.tk/map.html")

        binding.locationTv.text = intent.getStringExtra("location")

        binding.locationExitBtn.setOnClickListener {
            finish()
        }
    }
}