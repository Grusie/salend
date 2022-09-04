package cf.untitled.salend.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.cardview.widget.CardView

class CustomCardView :CardView{     //커스텀 카드뷰 (width, height 동일하게 설정)
    constructor(context: Context?) : super(context!!)
    constructor(context: Context?, attrs: AttributeSet?) : super(context!!,attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr : Int) : super(context!!, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}