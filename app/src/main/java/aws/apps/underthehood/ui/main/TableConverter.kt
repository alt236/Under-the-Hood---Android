package aws.apps.underthehood.ui.main

import android.graphics.Color
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import aws.apps.underthehood.R
import aws.apps.underthehood.ui.views.GuiCreation
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

class TableConverter(private val gui: GuiCreation) {
    private val TAG = this.javaClass.name
    private val separator = gui.getString(R.string.seperator_identifier)
    private val lp = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

    fun listToTable(t: TableLayout, groups: List<CommandOutputGroup>) {
        try {
            if (groups.isEmpty()) {
                return
            }

            for (group in groups) {
                if (group.commandOutputs.isEmpty()) {
                    continue
                }

                if (group.name.isNotEmpty()) {
                    t.addView(gui.createSeperatorRow(separator + group.name), lp)
                }

                for (output in group.commandOutputs) {
                    t.addView(gui.createTitleRow(output.commandWithPrompt), lp)
                    t.addView(createDataRow(output), lp)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "^ listToTable() Exception: " + e.message)
        }
    }

    private fun createDataRow(output: CommandOutput): TableRow {
        val dataRow = gui.createDataRow(output.output)
        if (!output.successful) {
            dataRow.findViewById<TextView>(R.id.text).setTextColor(Color.RED)
        }
        return dataRow
    }
}