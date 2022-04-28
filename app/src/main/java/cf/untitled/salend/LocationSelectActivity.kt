package cf.untitled.salend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cf.untitled.salend.databinding.ActivityLocationSelectBinding

class LocationSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLocationSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.locationTv.text = intent.getStringExtra("location")
    }
}