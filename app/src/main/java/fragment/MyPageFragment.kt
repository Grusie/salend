package fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cf.untitled.salend.AuthActivity
import cf.untitled.salend.LoginActivity
import cf.untitled.salend.MyApplication
import cf.untitled.salend.R
import cf.untitled.salend.databinding.FragmentMyPageBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyPageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyPageFragment : Fragment() {
    lateinit var binding: FragmentMyPageBinding
    var current_user_email : String? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment auth_text_view
        binding = FragmentMyPageBinding.inflate(layoutInflater)

        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            UserApiClient.rx.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.i("grusie", "로그아웃 성공. SDK에서 토큰 삭제 됨")
                }, { error ->
                    Log.e("grusie", "로그아웃 실패. SDK에서 토큰 삭제 됨", error)
                }).addTo(MyApplication.disposables)
            changeLoginStatus("LogOut")
        }

        binding.authTextView.setOnClickListener {
            val intent = Intent(this.context, LoginActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        if(MyApplication.checkAuth()) changeLoginStatus("LogIn")
        else changeLoginStatus("LogOut")

        if (AuthApiClient.instance.hasToken()) {
            UserApiClient.rx.me()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { user ->
                    if (user.id != null) {
                        changeLoginStatus("kakao")
                    }
                }
        }
    }

    private fun changeLoginStatus(status: String) {
        if(status == "LogIn"){
            binding.apply{
                if(MyApplication.auth.currentUser?.email != null)
                    current_user_email = MyApplication.auth.currentUser?.email!!

                MyApplication.db = FirebaseFirestore.getInstance()
                profileImg.visibility = View.VISIBLE
                authInfo.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                authTextView.visibility = View.GONE

                profileImg.setImageResource(R.drawable.ic_search)
                val docRef = MyApplication.db.collection("profile")
                docRef.whereEqualTo("email", current_user_email).get()
                    .addOnSuccessListener { document ->
                        for(fields in document){
                            authInfo.text = fields["name"] as String + "님 반갑습니다."
                        }
                        Log.d("grusie","asasd")
                    }
                    .addOnFailureListener { exception ->
                        Log.d("grusie", "get failed with ", exception)
                    }
            }
        }
        else if(status == "LogOut") {
            binding.apply {
                profileImg.visibility = View.GONE
                authInfo.visibility = View.GONE
                logoutBtn.visibility = View.GONE
                authTextView.visibility = View.VISIBLE

                authInfo.text = ""
            }
        }
        else if(status == "kakao"){
            binding.apply {
                MyApplication.db = FirebaseFirestore.getInstance()
                profileImg.visibility = View.VISIBLE
                authInfo.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                authTextView.visibility = View.GONE

                // 사용자 정보 요청 (기본)
                UserApiClient.rx.me()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ user ->
                        Log.i(
                            "name", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                        )
                        //current_user_email = user.kakaoAccount?.email
                        current_user_email = user.kakaoAccount?.profile?.nickname
                        val docRef = MyApplication.db.collection("profile")
                        docRef.whereEqualTo("email", current_user_email).get()
                            .addOnSuccessListener { document ->
                                for (fields in document) {
                                    authInfo.text = fields["name"] as String + "님 반갑습니다."
                                }
                            }.addOnFailureListener{ exception ->
                                Log.d("grusie", "get failed with ", exception)
                            }
                        profileImg.setImageResource(R.drawable.ic_search)
                    }, { error ->
                        Log.e("name", "사용자 정보 요청 실패", error)
                    })
                    .addTo(MyApplication.disposables)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}