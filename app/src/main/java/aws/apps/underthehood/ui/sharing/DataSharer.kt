package aws.apps.underthehood.ui.sharing

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import aws.apps.underthehood.R

class DataSharer(private val context: Context) {

    fun shareData(payload: SharePayload) {
        val intent = Intent(Intent.ACTION_SEND)

        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, payload.text)
        intent.putExtra(Intent.EXTRA_SUBJECT, payload.subject)
        intent.addCategory(Intent.CATEGORY_DEFAULT)

        val chooser = Intent.createChooser(
                intent,
                context.getString(R.string.label_share_dialogue_title))

        ContextCompat.startActivity(context, chooser, null)
    }

}