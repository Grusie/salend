package cf.untitled.salend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        moveMain(1)

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)
    }

    private fun moveMain(sec: Int) {
        Handler().postDelayed(Runnable {
            run(){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, (1000 * sec).toLong())
    }
}