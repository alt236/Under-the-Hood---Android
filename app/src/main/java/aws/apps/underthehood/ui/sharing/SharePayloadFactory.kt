package aws.apps.underthehood.ui.sharing

import android.content.res.Resources
import androidx.annotation.StringRes
import aws.apps.underthehood.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup
import uk.co.alt236.underthehood.commandrunner.model.Result

class SharePayloadFactory(private val resources: Resources) {
    private val separator = resources.getString(R.string.seperator_identifier)

    fun create(result: Result): SharePayload {
        val timestamp = result.timestamp.toString()

        val fileName = "deviceinfo_$timestamp.txt"

        val subject = "${getString(R.string.text_under_the_hood)} @ $timestamp"
        val text = createText(subject, result)

        return SharePayload(fileName = fileName,
                subject = subject,
                text = text)
    }

    private fun createText(subject: String, result: Result): String {
        val sb = StringBuilder()
        sb.appendLn(subject)

        appendData(sb, R.string.export_section_device_info, result.deviceinfo)
        sb.append("---------------------------------\n\n")

        appendData(sb, R.string.export_section_ipconfig_info, result.ipConfigData)
        appendData(sb, R.string.export_section_ip_route_info, result.ipRouteData)
        appendData(sb, R.string.export_section_proc_info, result.procData)
        appendData(sb, R.string.export_section_ps_info, result.psData)
        appendData(sb, R.string.export_section_netstat_info, result.netstatData)
        appendData(sb, R.string.export_section_sys_prop, result.sysPropData)
        appendData(sb, R.string.export_section_other_info, result.otherData)

        sb.append("\n\n---------------------------------")
        return sb.toString().trim { it <= ' ' }
    }

    private fun appendData(sb: StringBuilder, @StringRes sectionLabel: Int, payload: String) {
        if (payload.isEmpty()) {
            return
        }
        sb.appendLn(getString(sectionLabel))
        sb.appendLn(payload)
    }

    private fun appendData(sb: StringBuilder, @StringRes sectionLabel: Int, payload: List<CommandOutputGroup>) {
        if (payload.isEmpty()) {
            return
        }

        sb.appendLn(getString(sectionLabel))
        for (group in payload) {
            if (group.name.isNotEmpty()) {
                sb.appendLn(separator + group.name)
            }

            for (command in group.commandOutputs) {
                sb.appendLn(command.commandWithPrompt)
                sb.appendLn(command.output)
            }
        }
    }

    private fun getString(@StringRes id: Int): String {
        return resources.getString(id)
    }

    private fun StringBuilder.appendLn(input: String): StringBuilder {
        this.append(input)
        this.append('\n')
        return this
    }
}