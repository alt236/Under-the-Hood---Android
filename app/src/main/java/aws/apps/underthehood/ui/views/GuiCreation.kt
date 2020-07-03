/*******************************************************************************
 * Copyright 2010 Alexandros Schillings
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aws.apps.underthehood.ui.views

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.StringRes
import aws.apps.underthehood.R

class GuiCreation(private val mContext: Context) {
    private val userNotify: UserNotify = UserNotify(mContext.applicationContext)
    private val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val textViewCopyClickListener = CopyOnClickListener(userNotify)

    fun createDataRow(text: String?): TableRow {
        val tr = inflater.inflate(R.layout.table_row_data, null) as TableRow
        val textview = tr.findViewById<TextView>(R.id.text)
        textview.isClickable = true
        textview.setOnClickListener(textViewCopyClickListener)
        textview.text = text
        return tr
    }

    fun createSeperatorRow(text: String): TableRow {
        val tr = inflater.inflate(R.layout.table_row_section, null) as TableRow
        val textView = tr.findViewById<TextView>(R.id.text)
        textView.text = text
        return tr
    }

    fun createTitleRow(text: String): TableRow {
        val tr = inflater.inflate(R.layout.table_row_title, null) as TableRow
        val textView = tr.findViewById<TextView>(R.id.text)
        textView.text = text
        return tr
    }

    fun createScrollableTable(): View {
        return inflater.inflate(R.layout.page_scrollable_table, null)
    }

    fun createScrollableTextView(): View {
        return inflater.inflate(R.layout.page_scrollable_textview, null)
    }

    fun getString(@StringRes resId: Int): String {
        return mContext.getString(resId)
    }

    private class CopyOnClickListener constructor(private val userNotify: UserNotify) : View.OnClickListener {
        override fun onClick(view: View) {
            val t = view as TextView
            val text = t.text.toString()

            if (text.isEmpty()) {
                return
            }

            val context = view.getContext()
            val msgtext = getMessageText(text)

            val message = context.getString(R.string.template_text_copied, msgtext)

            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clipData = ClipData.newPlainText("Copied data", text)
            clipboardManager.setPrimaryClip(clipData)

            userNotify.notify(message)
        }

        private fun getMessageText(original: String): String {
            return if (original.length > 150) {
                original.substring(0, 150).trim { it <= ' ' } + "..."
            } else {
                original
            }
        }
    }
}