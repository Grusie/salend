package cf.untitled.salend

import android.content.Context
import android.util.AttributeSet
import android.view.View

class RectView2: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?,attrs: AttributeSet?) : super(context,attrs)
    constructor(context: Context?,attrs: AttributeSet?,defStyleAttr : Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}