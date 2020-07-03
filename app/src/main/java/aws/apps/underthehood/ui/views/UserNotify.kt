package aws.apps.underthehood.ui.views

import android.content.Context
import android.widget.Toast

class UserNotify(private val context: Context) {

    fun notify(text: CharSequence) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}