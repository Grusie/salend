package cf.untitled.salend

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {        //splash 이미지를 띄워주기 위한 액티비티
    override fun onCreate(savedInstanceState: Bundle?) {
        moveMain(1)

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
    }

    private fun moveMain(sec: Int) {
        Handler().postDelayed(Runnable {
            run {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, (1000 * sec).toLong())
    }
}