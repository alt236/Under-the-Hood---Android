package aws.apps.underthehood.ui.dialogs

import android.content.Context
import androidx.fragment.app.DialogFragment
import aws.apps.underthehood.BuildConfig
import aws.apps.underthehood.R

object DialogFactory {

    fun createProgressDialog(): DialogFragment {
        return ProgressDialogFragment.newInstance()
    }

    fun createAboutDialog(context: Context): DialogFragment {
        val title = context.getString(R.string.app_name) + " v" + BuildConfig.VERSION_NAME
        val message = getAboutText(context)
        return AlertDialogFragment.newInstance(title, message)
    }

    private fun getAboutText(context: Context): String {
        var text = ""
        text += context.getString(R.string.app_changelog)
        text += "\n\n"
        text += context.getString(R.string.app_notes)
        text += "\n\n"
        text += context.getString(R.string.app_acknowledgements)
        text += "\n\n"
        text += context.getString(R.string.app_copyright)
        return text
    }

}