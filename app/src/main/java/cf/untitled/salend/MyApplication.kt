package cf.untitled.salend

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.multidex.MultiDexApplication
import cf.untitled.salend.model.UserData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.reactivex.disposables.CompositeDisposable

class MyApplication: MultiDexApplication() {
    companion object {

        val auth = Firebase.auth
        @SuppressLint("StaticFieldLeak")
        lateinit var db: FirebaseFirestore
        var disposables = CompositeDisposable()
        var email: String? = null
        lateinit var sharedPref:SharedPreferences
        lateinit var edit : SharedPreferences.Editor
        lateinit var current_user_email : String
        private var storeFavorite = ArrayList<String?>()
        private var productFavorite = ArrayList<String?>()

        fun checkAuth(): Boolean {     //이메일 인증 완료해야만 true 반환
            val currentUser = auth.currentUser
            return currentUser?.let {  //유저가 저장되어있는지
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let{
                false
            }
        }

        @JvmName("getStoreFavorite1")
        fun getStoreFavorite() : ArrayList<String?>{
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")

            val task = docRef.whereEqualTo("u_id", current_user_email).get()
            var asd = Tasks.await(task)
            storeFavorite = asd.documents[0].data?.get("u_store_favorite") as ArrayList<String?>
            return storeFavorite
        }

        @JvmName("getProductFavorite1")
        fun getProductFavorite(): ArrayList<String?>{
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")
            val task = docRef.whereEqualTo("u_id", current_user_email).get()
            var asd = Tasks.await(task)
            storeFavorite = asd.documents[0].data?.get("u_item_favorite") as ArrayList<String?>
            return productFavorite
        }

        fun setStoreFavorite(userId : String, productId : String) {
            db = FirebaseFirestore.getInstance()
            var lastStoreFavorite = this.getStoreFavorite()
            lastStoreFavorite.add(productId)
            db.collection("profile").document(userId).update("u_store_favorite", lastStoreFavorite)
        }

        fun setProductFavorite(userId : String, productId : String) {
            db = FirebaseFirestore.getInstance()
            var lastProductFavorite = this.getStoreFavorite()
            lastProductFavorite.add(productId)
            db.collection("profile").document(userId).update("u_item_favorite", lastProductFavorite)
        }

        fun updateStoreFavorite(userId: String, storeFavorite: ArrayList<String?>){
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_store_favorite",storeFavorite)
        }

        fun updateProductFavorite(userId: String, productFavorite: ArrayList<String?>){
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_product_favorite", productFavorite)
        }

        fun delStoreFavorite(userId: String, storeId: String) {
            val lastStoreFavorite = this.getStoreFavorite()
            lastStoreFavorite.removeAll(listOf(storeId))
            updateStoreFavorite(userId, lastStoreFavorite)
        }

        fun delProductFavorite(userId : String, productId: String){
            val lastProductFavorite = this.getProductFavorite()
            lastProductFavorite.removeAll(listOf(productId))
            updateProductFavorite(userId, lastProductFavorite)
        }

        fun checkStoreFavorite(storeId: String):Boolean {
            return this.getStoreFavorite().contains(storeId)
        }

        fun checkProductFavorite(productId: String):Boolean{
            return this.getProductFavorite().contains(productId)
        }

        fun saveUser(userData: UserData) {
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userData.u_id!!).set(userData)
        }

    }
}