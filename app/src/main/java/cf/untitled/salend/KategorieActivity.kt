package cf.untitled.salend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cf.untitled.salend.adapter.KategorieChoiceAdapter
import cf.untitled.salend.databinding.KategorieChoiceBinding
import cf.untitled.salend.model.KategoriStoreData

class KategorieActivity: AppCompatActivity() {

    lateinit var binding: KategorieChoiceBinding
    var kategorieList = arrayListOf<KategoriStoreData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = KategorieChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storeName = intent.getStringExtra("storeName")
        binding.kategorieChoiceToolbarText.setText(storeName)

        kategorieCoice(storeName)


        binding.kategorieRv.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        binding.kategorieRv.setHasFixedSize(true)
        binding.kategorieRv.adapter = KategorieChoiceAdapter(kategorieList)
    }

    private fun kategorieCoice(storeName: String?) {

        when(storeName) {
            "한식" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 4동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 5동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 1동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 2동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 4동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 5동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 1동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 2동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 4동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 5동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 1동점"),
                    KategoriStoreData(R.drawable.shopping_selector, "한식 면목 2동점")
                )
            }
            "양식" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "양식 면목 4점")
                )
            }
            "중식" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "중식 면목 4점")
                )
            }

            "일식" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "일식 면목 4점")
                )
            }
            "분식" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "분식 면목 4점")
                )
            }
            "카페" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "카페 면목 4점")
                )
            }
            "편의점" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "GS25 면목 4점")
                )
            }
            "베이커리" -> {
                kategorieList = arrayListOf(
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 4점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 1점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 2점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 3점"),
                    KategoriStoreData(R.drawable.shopping_selector, "파리바게트 면목 4점")
                )
            }
        }
    }
}