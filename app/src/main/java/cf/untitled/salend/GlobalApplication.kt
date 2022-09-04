package cf.untitled.salend

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

//카카오 소셜 로그인을 사용하기 위한, 앱 기본으로 있는 KakaoSdk 초기화
class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "3bbcda37653f4ec196075c15eca1dfaa")
    }
}