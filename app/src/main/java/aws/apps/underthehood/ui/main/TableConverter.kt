package aws.apps.underthehood.ui.main

import android.util.Log
import android.widget.TableLayout
import aws.apps.underthehood.R
import aws.apps.underthehood.ui.views.GuiCreation

class TableConverter(private val gui: GuiCreation) {
    private val TAG = this.javaClass.name

    fun listToTable(lp: TableLayout.LayoutParams, t: TableLayout, lines: List<String>) {
        var chr: String
        val separator = gui.getString(R.string.seperator_identifier)
        try {
            if (lines.isEmpty()) {
                return
            }

            for (line in lines) {
                if (line.isEmpty()) {
                    continue
                }

                chr = line.substring(0, 1)

                if (chr == "#" || chr == "$" || chr == ">") {
                    t.addView(gui.createTitleRow(line), lp)
                } else if (chr == separator) {
                    t.addView(gui.createSeperatorRow(line), lp)
                } else {
                    t.addView(gui.createDataRow(line), lp)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "^ listToTable() Exception: " + e.message)
        }
    }

}