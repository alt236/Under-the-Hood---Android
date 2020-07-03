package aws.apps.underthehood.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import androidx.viewpager.widget.ViewPager

// code by Victor Elias: http://stackoverflow.com/questions/6920137/android-viewpager-and-horizontalscrollview
class CustomViewPager : ViewPager {
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context) : super(context)

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return super.canScroll(v, checkV, dx, x, y) || checkV && customCanScroll(v)
    }


    @Suppress("MemberVisibilityCanBePrivate")
    fun customCanScroll(v: View): Boolean {
        if (v is HorizontalScrollView) {
            val hsvChild = v.getChildAt(0)
            return hsvChild.width > v.getWidth()
        }
        return false
    }
}