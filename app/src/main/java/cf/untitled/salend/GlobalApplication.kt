package cf.untitled.salend

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "3bbcda37653f4ec196075c15eca1dfaa")
    }
}