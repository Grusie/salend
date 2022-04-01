package cf.untitled.salend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import cf.untitled.salend.databinding.ActivityLoginBinding
import cf.untitled.salend.model.UserData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.authBtn.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
        binding.googleLoginBtn.setOnClickListener {
            signIn()
        }

        binding.loginBtn.setOnClickListener {
            val email:String = binding.authEmail.text.toString()
            val password: String = binding.authPassword.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    task -> binding.authEmail.text.clear()
                binding.authPassword.text.clear()
                if(task.isSuccessful){
                    if(MyApplication.checkAuth()){
                        //로그인 성공
                        MyApplication.email = email
                        finish()
                    }else{
                        //발송된 메일로 인증 확인을 안 한 경우
                        Toast.makeText(baseContext,"전송된 메일로 이메일 인증이 되지 않았습니다.", Toast.LENGTH_SHORT).show()
                    }
                }else {     //없는 계정을 입력하는 경우 등
                    Toast.makeText(baseContext,"아이디 혹은 비밀번호가 잘못되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,9001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                val googleSign = GoogleSignIn.getLastSignedInAccount(this)

                val userEmail = googleSign?.email

                val userData = UserData(
                    userEmail!!, userEmail!!,    //TODO 네임 값 변경 고민
                )
                MyApplication.db = FirebaseFirestore.getInstance()
                MyApplication.db.collection("profile").document(userEmail)
                    .set(userData)

                Log.d("grusie", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("grusie", "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        MyApplication.auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("grusie", "signInWithCredential:success")
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("grusie", "signInWithCredential:failure", task.exception)
                }
            }
    }
}