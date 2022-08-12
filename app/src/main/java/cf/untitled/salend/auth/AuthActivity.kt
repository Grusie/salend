package cf.untitled.salend.auth

import cf.untitled.salend.databinding.ActivityAuthBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import cf.untitled.salend.MyApplication
import cf.untitled.salend.model.UserData

class AuthActivity : AppCompatActivity() {      //회원가입 액티비티
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.loginToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.authBtn.setOnClickListener {
            //이메일,비밀번호 회원가입........................
            val email: String = binding.authEmail.text.toString()
            val password: String = binding.authPassword.text.toString()
            val passwordRetry: String = binding.authPasswordRetry.text.toString()
            if (email == "" || email == null) {
                Toast.makeText(this, "이메일을 입력해 주세요", Toast.LENGTH_SHORT).show()
            }
            else if (password != passwordRetry){
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
                MyApplication.auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        binding.authEmail.text.clear()
                        binding.authPassword.text.clear()
                        if (task.isSuccessful) {     //파이어베이스 등록
                            MyApplication.auth.currentUser?.sendEmailVerification()
                                ?.addOnCompleteListener { sendTask ->
                                    if (sendTask.isSuccessful) {      //이메일 전송
                                        Toast.makeText(
                                            this,
                                            "회원가입에 성공했습니다. 전송된 메일을 확인해 주세요.",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        //회원가입 시 firebase에 새로운 ProfileData 추가 -> 이메일 인증 성공을 전제
                                        val userEmail = MyApplication.auth.currentUser?.email!!
                                        val userData = UserData(
                                            userEmail,
                                            userEmail,
                                            ArrayList(),
                                            ArrayList(),
                                            ArrayList()
                                        )
                                        MyApplication.saveUser(userData)
                                        finish()
                                    } else {
                                        Toast.makeText(this, "메일 전송 실패", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                    Log.d("grusie", "실패")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}