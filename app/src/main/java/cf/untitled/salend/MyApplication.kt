package cf.untitled.salend

import android.annotation.SuppressLint
import android.util.Log
import androidx.multidex.MultiDexApplication
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

        fun checkAuth(): Boolean {     //이메일 인증 완료해야만 true 반환
            val currentUser = auth.currentUser
            return currentUser?.let {  //유저가 저장되어있는지
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let{
                false
            }
        }
        fun saveUser(userData: UserData) {
            db = FirebaseFirestore.getInstance()
            db.collection("profile").document(userData.u_id!!).set(userData)
        }
    }
}