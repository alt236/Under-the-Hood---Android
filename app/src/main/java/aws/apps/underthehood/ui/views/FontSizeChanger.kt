package aws.apps.underthehood.ui.views

import android.util.Log
import android.util.TypedValue
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView

class FontSizeChanger {
    val TAG = this.javaClass.name

    fun changeFontSize(t: TableLayout, fontSize: Float) {

        for (i in 0 until t.childCount) {
            val row = t.getChildAt(i) as TableRow
            for (j in 0 until row.childCount) {
                val v = row.getChildAt(j)

                try {
                    if (v.javaClass == TextView::class.java) {
                        val textView = v as TextView

                        if (i % 2 == 0) {
                            // DO NOTHING
                        } else {
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "^ changeFontSize: ")
                }
            }
        }
    }
}