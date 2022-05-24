package cf.untitled.salend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import cf.untitled.salend.databinding.ActivityStoreChoiceBinding

class StoreChoiceActivity : AppCompatActivity(), View.OnClickListener {
    var star = false
    val binding by lazy { ActivityStoreChoiceBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val KategoriName = intent.getStringExtra("kategori")
        binding.kategorieChoiceToolbarText.text = KategoriName

        binding.kategorieChoiceFavoriteButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v == binding.kategorieChoiceFavoriteButton) {
            Toast.makeText(this,binding.kategorieChoiceFavoriteButton.background.toString(),Toast.LENGTH_LONG).show()
            if(star) {
                binding.kategorieChoiceFavoriteButton.setBackgroundResource(R.drawable.favorite_on)
                star = false
            } else {
                binding.kategorieChoiceFavoriteButton.setBackgroundResource(R.drawable.favorite_off)
                star = true
            }
        }
    }
}