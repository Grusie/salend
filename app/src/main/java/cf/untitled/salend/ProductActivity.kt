package cf.untitled.salend

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get
import cf.untitled.salend.databinding.ActivityProductBinding
import cf.untitled.salend.model.ProductData
import cf.untitled.salend.retrofit.RetrofitClass
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URISyntaxException
import kotlin.concurrent.thread

class ProductActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductBinding
    lateinit var productId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = intent.getStringExtra("product_id")!!
        binding = ActivityProductBinding.inflate(layoutInflater)
        setSupportActionBar(binding.productToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding.root)
        var client: WebViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.e("grusie", "$url")
                if (url != null && url.startsWith("intent://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        val existPackage =
                            packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                        if (existPackage != null) {
                            startActivity(intent)
                        } else {
                            val marketIntent = Intent(Intent.ACTION_VIEW)
                            marketIntent.data =
                                Uri.parse("market://details?id=" + intent.getPackage()!!)
                            startActivity(marketIntent)
                        }
                        return true
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else if (url != null && url.startsWith("market://")) {
                    try {
                        val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                        if (intent != null) {
                            startActivity(intent)
                        }
                        return true
                    } catch (e: URISyntaxException) {
                        e.printStackTrace()
                    }

                }
                view?.loadUrl(url!!)
                return false
            }

        }

        class WebViewData {
            @JavascriptInterface
            fun getResult(
                success: Boolean,
                productName: String?,
                payId: String?,
                price: Int?,
                err: String?
            ) {
                try {
                    CoroutineScope(Dispatchers.Default).launch {
                        withContext(CoroutineScope(Dispatchers.Main).coroutineContext) {
                            if(success){
                                Log.e("PA", "getResult: 결제 완료" )
                                Toast.makeText(baseContext, "결제가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                binding.payWebView.visibility = View.GONE
                                // 여기다 구현하시면 됩니다 ^^
                                thread(start=true) {
                                    MyApplication.setPayList(
                                        MyApplication.current_user_email!!,
                                        payId!!
                                    )
                                    Log.d("grusie", "결제 완료 : $payId")
                                }
                                // finish()
                            } else {
                                Log.e("PA", "getResult: 결제 실패" )
                                binding.payWebView.visibility = View.GONE
                                // finish()
                            }

                        }
                    }
                } catch (e: Exception) {
                    Log.d("locationSelectError", "$e")
                }
            }
        }

        initProduct(productId)
        binding.payWebView.apply {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            addJavascriptInterface(WebViewData(), "Leaf")
            settings.setDomStorageEnabled(true)
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
                    val newWebView = WebView(this@ProductActivity)
                    newWebView.settings.javaScriptEnabled = true
                    val dialog = Dialog(this@ProductActivity).apply {
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

        binding.payButton.setOnClickListener {
            binding.payWebView.loadUrl("https://api.salend.tk/pay/debug/${productId}")
            Log.d("grusie", "url ${binding.payWebView.url}")
            binding.payWebView.visibility = View.VISIBLE
        }

    }

    private fun initProduct(productId: String?) {
        RetrofitClass.service.getSingleProductDataPage("$productId").enqueue(object :
            Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                if (response.isSuccessful) {
                    var saleRate = 0
                    // 정상적으로 통신이 성공된 경우
                    var result: ProductData? = response.body()
                    saleRate =
                        100 - ((result?.i_now_price!!.toDouble() / result?.i_price!!.toDouble()) * 100).toInt()
                    binding.apply {
                        Glide.with(this@ProductActivity)
                            .load(result?.i_image)
                            .error(R.drawable.ic_no_image_svgrepo_com)
                            .into(productInfoImg)
                        productInfoName.text = result?.i_name
                        productInfoPrice.text = result?.i_price.toString() + "￦"
                        productInfoPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        productInfoNowPrice.text = result?.i_now_price.toString() + "￦"
                        productInfoRate.text = "${saleRate}% 할인 가격"
                        productInfoExp.text = "유통기한 : ${result?.i_exp}"
                        productInfoExp.text = result?.i_store_name + " / 유통기한 : ${result?.i_exp}"
                    }
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("retrofit", "${response.code()}")
                    Log.d("retrofit", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("retrofit", "onFailure 에러: " + t.message.toString());
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        if (MyApplication.current_user_email != null && MyApplication.current_user_email != "") {
            thread(start = true) {
                val flag = MyApplication.checkProductFavorite(productId)
                runOnUiThread {
                    if (flag) {
                        menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
                        menu?.getItem(0)?.setChecked(true)
                    } else {
                        menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(this, R.drawable.ic_star)
                        menu?.getItem(0)?.setChecked(false)
                    }
                }
            }
            menu?.getItem(0)?.setVisible(true)
        } else menu?.getItem(0)?.setVisible(false)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.favorite_menu) {
            if (item.isChecked) {
                changeFavorite(item, false)
            } else {
                changeFavorite(item, true)
            }
        }
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun changeFavorite(item: MenuItem, flag: Boolean) {
        if (flag) {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
            thread(start = true) {
                MyApplication.setProductFavorite(MyApplication.current_user_email!!, productId)
            }
            item.setChecked(true)
        } else {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
            thread(start = true) {
                MyApplication.delProductFavorite(MyApplication.current_user_email!!, productId)
            }
            item.setChecked(false)
        }
    }
}