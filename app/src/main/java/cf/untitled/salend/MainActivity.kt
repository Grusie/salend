package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cf.untitled.salend.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}