package aws.apps.underthehood.ui.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import aws.apps.underthehood.R

class AlertDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val mode = arguments!!.getInt(EXTRA_MODE)
        val title: String?
        val message: String?

        if (mode == MODE_RES) {
            title = getString(arguments!!.getInt(EXTRA_TITLE))
            message = getString(arguments!!.getInt(EXTRA_MESSAGE))
        } else {
            title = arguments!!.getString(EXTRA_TITLE)
            message = arguments!!.getString(EXTRA_MESSAGE)
        }

        return createDialog(title, message)
    }

    private fun createDialog(title: String?, message: String?): Dialog {
        val context = context
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_textview, null)
        val textView = view.findViewById<TextView>(R.id.text)

        val spannableMessage = SpannableString(message)

        textView.text = spannableMessage
        textView.autoLinkMask = Activity.RESULT_OK
        textView.movementMethod = LinkMovementMethod.getInstance()

        Linkify.addLinks(spannableMessage, Linkify.ALL)
        val listener = DialogInterface.OnClickListener { dialog: DialogInterface?, id: Int -> }
        return AlertDialog.Builder(context!!)
                .setTitle(title)
                .setPositiveButton(R.string.ok, listener)
                .setView(view)
                .create()
    }

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    companion object {
        private val EXTRA_TITLE = AlertDialogFragment::class.java.name + ".EXTRA_TITLE"
        private val EXTRA_MESSAGE = AlertDialogFragment::class.java.name + ".EXTRA_MESSAGE"
        private val EXTRA_MODE = AlertDialogFragment::class.java.name + ".EXTRA_MODE"

        private const val MODE_RES = 0
        private const val MODE_STRING = 1

        protected fun newInstance(@StringRes title: Int,
                                  @StringRes message: Int): DialogFragment {
            val frag: DialogFragment = AlertDialogFragment()
            val args = Bundle()
            args.putInt(EXTRA_TITLE, title)
            args.putInt(EXTRA_MESSAGE, message)
            args.putInt(EXTRA_MODE, MODE_RES)
            frag.arguments = args
            return frag
        }

        fun newInstance(title: String?,
                        message: String?): DialogFragment {
            val frag: DialogFragment = AlertDialogFragment()
            val args = Bundle()
            args.putString(EXTRA_TITLE, title)
            args.putString(EXTRA_MESSAGE, message)
            args.putInt(EXTRA_MODE, MODE_STRING)
            frag.arguments = args
            return frag
        }
    }
}