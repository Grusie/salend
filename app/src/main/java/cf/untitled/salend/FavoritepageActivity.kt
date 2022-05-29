package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cf.untitled.salend.databinding.ActivityFavoritepageBinding

class FavoritepageActivity : AppCompatActivity() {
    lateinit var binding : ActivityFavoritepageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}