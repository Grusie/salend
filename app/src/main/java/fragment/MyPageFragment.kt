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
    }

    private fun changeLoginStatus(status: String) {
        if(status == "LogIn"){
            binding.apply{
                val current_user_email = MyApplication.auth.currentUser?.email
                MyApplication.db = FirebaseFirestore.getInstance()
                profileImg.visibility = View.VISIBLE
                authInfo.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                authTextView.visibility = View.GONE

                profileImg.setImageResource(R.drawable.ic_search)
                val docRef = MyApplication.db.collection("profile")
                    .whereEqualTo("email", current_user_email)
                docRef.get()
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
        else {
            binding.apply {
                profileImg.visibility = View.GONE
                authInfo.visibility = View.GONE
                logoutBtn.visibility = View.GONE
                authTextView.visibility = View.VISIBLE
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