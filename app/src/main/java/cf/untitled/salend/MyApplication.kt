package cf.untitled.salend

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import cf.untitled.salend.model.UserData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import io.reactivex.disposables.CompositeDisposable

class MyApplication : MultiDexApplication() {       //파이어베이스 관련을 처리하는 클래스
    companion object {

        private var status: Boolean = false

        val auth = Firebase.auth

        @SuppressLint("StaticFieldLeak")
        lateinit var db: FirebaseFirestore
        var disposables = CompositeDisposable()
        var email: String? = null
        lateinit var sharedPref: SharedPreferences
        lateinit var edit: SharedPreferences.Editor
        var current_user_email: String? = null
        private var storeFavorite = ArrayList<String?>()
        private var productFavorite = ArrayList<String?>()
        private var payList = ArrayList<String?>()

        fun getStatus(): Boolean {
            return status
        }

        fun setStatus(): Boolean {      //로그인 상태 저장
            this.status = checkAuth()
            if (this.status)
                this.current_user_email = auth.currentUser?.email
            return this.status
        }

        fun checkAuth(): Boolean {     //이메일 인증 완료해야만 true 반환
            val currentUser = auth.currentUser
            return currentUser?.let {  //유저가 저장되어있는지
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }

        fun checkLogIn(): Boolean {     //로그인 체크 용
            return (current_user_email != null && current_user_email != "")
        }


        @JvmName("getStoreFavorite1")
        fun getStoreFavorite(): ArrayList<String?> {        //가게 찜 리스트 가져옴
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")

            val task = docRef.whereEqualTo("u_id", current_user_email!!).get()
            var asd = Tasks.await(task)
            val uStore = asd.documents[0].data?.get("u_store_favorite") as ArrayList<String?>?
            if (uStore != null)
                storeFavorite = uStore
            return storeFavorite
        }

        @JvmName("getProductFavorite1")
        fun getProductFavorite(): ArrayList<String?> {      //상품 찜 리스트 가져옴
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")
            val task = docRef.whereEqualTo("u_id", current_user_email!!).get()
            var asd = Tasks.await(task)
            val uProduct = asd.documents[0].data?.get("u_item_favorite") as ArrayList<String?>?
            if (uProduct != null)
                productFavorite = uProduct

            return productFavorite
        }

        fun setStoreFavorite(userId: String, storeId: String) {     //가게 찜 추가
            db = FirebaseFirestore.getInstance()
            var lastStoreFavorite = getStoreFavorite()
            lastStoreFavorite.add(storeId)
            updateStoreFavorite(userId, lastStoreFavorite)
        }

        fun setProductFavorite(userId: String, productId: String) {     //상품 찜 추가
            db = FirebaseFirestore.getInstance()
            var lastProductFavorite = getProductFavorite()
            lastProductFavorite.add(productId)
            updateProductFavorite(userId, lastProductFavorite)
        }

        fun getPayList(userId: String): ArrayList<String?> {    //결제 리스트 받아옴
            db = FirebaseFirestore.getInstance()
            val docRef = db.collection("profile")

            val task = docRef.whereEqualTo("u_id", userId).get()
            var asd = Tasks.await(task)
            val uPay = asd.documents[0].data?.get("u_pay_list") as ArrayList<String?>?
            if (uPay != null)
                payList = uPay
            return payList
        }

        fun setPayList(userId: String, payId: String) {     //결제 리스트 추가
            db = FirebaseFirestore.getInstance()
            var lastPayList = getPayList(userId)
            lastPayList.add(payId)
            updatePayList(userId, lastPayList)
        }

        private fun updatePayList(userId: String, payList: ArrayList<String?>) {        //결제 상태 수정
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_pay_list", payList)
        }

        private fun updateStoreFavorite(userId: String, storeFavorite: ArrayList<String?>) {        //가게 찜 상태 수정
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_store_favorite", storeFavorite)
        }

        private fun updateProductFavorite(userId: String, productFavorite: ArrayList<String?>) {        //상품 찜 상태 수정
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userId).update("u_item_favorite", productFavorite)
        }

        fun delStoreFavorite(userId: String, storeId: String) {     //가게 찜 제거
            val lastStoreFavorite = this.getStoreFavorite()
            lastStoreFavorite.removeAll(listOf(storeId).toSet())
            updateStoreFavorite(userId, lastStoreFavorite)
        }

        fun delProductFavorite(userId: String, productId: String) {     //상품 찜 제거
            val lastProductFavorite = this.getProductFavorite()
            lastProductFavorite.removeAll(listOf(productId).toSet())
            updateProductFavorite(userId, lastProductFavorite)
        }

        fun checkStoreFavorite(storeId: String): Boolean {      //유저의 가게 찜 상태 체크
            return this.getStoreFavorite().contains(storeId)
        }

        fun checkProductFavorite(productId: String): Boolean {      //유저의 상품 찜 상태 체크
            return this.getProductFavorite().contains(productId)
        }

        fun saveUser(userData: UserData) {      //firebase firestore에 유저정보 저장
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userData.u_id!!).set(userData)
        }
    }
}