package cf.untitled.salend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cf.untitled.salend.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.authBtn.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
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
    }
}