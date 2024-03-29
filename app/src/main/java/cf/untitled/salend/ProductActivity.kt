package cf.untitled.salend

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

class ProductActivity : AppCompatActivity() {       //상품 선택 시 띄워줄 액티비티, WebView를 활용한 카카오페이 결제 기능 구현
    lateinit var binding: ActivityProductBinding
    lateinit var productId: String
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = intent.getStringExtra("product_id")!!
        binding = ActivityProductBinding.inflate(layoutInflater)
        setSupportActionBar(binding.productToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setContentView(binding.root)
        val client: WebViewClient = object : WebViewClient() {

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
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
                            if (success) {
                                Toast.makeText(baseContext, "결제가 완료되었습니다.", Toast.LENGTH_SHORT)
                                    .show()
                                binding.payWebView.visibility = View.GONE
                                thread(start = true) {
                                    MyApplication.setPayList(
                                        MyApplication.current_user_email!!,
                                        payId!!
                                    )
                                    Log.d("grusie", "결제 완료 : $payId")
                                }
                            } else {
                                Log.e("PA", "getResult: 결제 실패")
                                binding.payWebView.visibility = View.GONE
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
            settings.domStorageEnabled = true
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
                    true.also { newWebView.settings.javaScriptEnabled = it }
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
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                if (response.isSuccessful) {
                    val saleRate: Int
                    // 정상적으로 통신이 성공된 경우
                    val result: ProductData? = response.body()
                    saleRate =
                        100 - ((result?.i_now_price!!.toDouble() / result.i_price!!.toDouble()) * 100).toInt()
                    binding.apply {
                        if (this@ProductActivity.isFinishing) {
                            return
                        }
                        Glide.with(this@ProductActivity)
                            .load(result.i_image)
                            .error(R.drawable.ic_no_image_svgrepo_com)
                            .into(productInfoImg)
                        productInfoName.text = result.i_name
                        (result.i_price.toString() + "￦").also { productInfoPrice.text = it }
                        productInfoPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        productInfoNowPrice.text = result.i_now_price.toString() + "￦"
                        productInfoRate.text = "${saleRate}% 할인 가격"
                        productInfoExp.text = "유통기한 : ${result.i_exp}"
                        productInfoExp.text = result.i_store_name + " / 유통기한 : ${result.i_exp}"
                    }
                } else {
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Log.d("retrofit", "${response.code()}")
                    Log.d("retrofit", "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("retrofit", "onFailure 에러: " + t.message.toString())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_menu, menu)
        if (MyApplication.checkLogIn()) {
            thread(start = true) {
                val flag = MyApplication.checkProductFavorite(productId)
                runOnUiThread {
                    if (flag) {
                        menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
                        menu?.getItem(0)?.isChecked = true
                    } else {
                        menu?.getItem(0)?.icon =
                            ContextCompat.getDrawable(this, R.drawable.ic_star)
                        menu?.getItem(0)?.isChecked = false
                    }
                }
            }
            menu?.getItem(0)?.isVisible = true
        } else menu?.getItem(0)?.isVisible = false


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
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeFavorite(item: MenuItem, flag: Boolean) {
        if (flag) {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_selected)
            thread(start = true) {
                MyApplication.setProductFavorite(MyApplication.current_user_email!!, productId)
            }
            item.isChecked = true
        } else {
            item.icon = ContextCompat.getDrawable(this, R.drawable.ic_star)
            thread(start = true) {
                MyApplication.delProductFavorite(MyApplication.current_user_email!!, productId)
            }
            item.isChecked = false
        }
    }
}