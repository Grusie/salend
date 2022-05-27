package cf.untitled.salend

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.multidex.MultiDexApplication
import cf.untitled.salend.fragment.HomeFragment
import cf.untitled.salend.model.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
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
        var storeFavorite = ArrayList<String>()
        var productFavorite = ArrayList<String>()

        fun checkAuth(): Boolean {     //이메일 인증 완료해야만 true 반환
            val currentUser = auth.currentUser
            return currentUser?.let {  //유저가 저장되어있는지
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let{
                false
            }
        }

        fun initStoreFavorite(){
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")
            docRef.whereEqualTo("u_id", current_user_email).get()
                .addOnSuccessListener { document ->
                    for(fields in document){
                        storeFavorite = fields["u_store_favorite"] as ArrayList<String>
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("grusie", "get failed with ", exception)
                }
        }

        @JvmName("getStoreFavorite1")
        fun getStoreFavorite(): ArrayList<String>{
            initStoreFavorite()
            return storeFavorite
        }

        fun setStoreFavorite(userId : String, productId : String) {
            db = FirebaseFirestore.getInstance()
            var lastStoreFavorite = getStoreFavorite()
            lastStoreFavorite.add(productId)
            db.collection("profile").document(userId).update("u_store_favorite", lastStoreFavorite)
        }

        fun updateStoreFavorite(userId: String, storeFavorite: ArrayList<String>){
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_store_favorite",storeFavorite)
        }

        fun updateProductFavorite(userId: String, productFavorite: ArrayList<String>){
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_product_favorite", productFavorite)
        }

        fun initProductFavorite(){
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")
            docRef.whereEqualTo("u_id", current_user_email).get()
                .addOnSuccessListener { document ->
                    for(fields in document){
                        productFavorite = fields["u_item_favorite"] as ArrayList<String>
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("grusie", "get failed with ", exception)
                }
        }

        @JvmName("getProductFavorite1")
        fun getProductFavorite(): ArrayList<String>{
            initStoreFavorite()
            return storeFavorite
        }

        fun setProductFavorite(userId : String, productId : String) {
            db = FirebaseFirestore.getInstance()
            var lastProductFavorite = getStoreFavorite()
            lastProductFavorite.add(productId)
            db.collection("profile").document(userId).update("u_item_favorite", lastProductFavorite)
        }

        fun delStoreFavorite(userId: String, storeId: String) {
            val lastStoreFavorite = getStoreFavorite()
            lastStoreFavorite.removeAll(listOf(storeId))
            updateStoreFavorite(userId, lastStoreFavorite)
        }

        fun delProductFavorite(userId : String, productId: String){
            val lastProductFavorite = getProductFavorite()
            lastProductFavorite.removeAll(listOf(productId))
            updateStoreFavorite(userId, lastProductFavorite)
        }

        fun saveUser(userData: UserData) {
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userData.u_id!!).set(userData)
        }

    }
}