package aws.apps.underthehood.ui.sharing

import android.content.res.Resources
import androidx.annotation.StringRes
import aws.apps.underthehood.R
import uk.co.alt236.underthehood.commandrunner.Result

class SharePayloadFactory(private val resources: Resources) {

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
        sb.append(subject)
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
        sb.append(getString(sectionLabel) + "\n")
        sb.append(payload + "\n")

    }

    private fun appendData(sb: StringBuilder, @StringRes sectionLabel: Int, payload: List<String>) {
        if (payload.isEmpty()) {
            return
        }
        sb.append(getString(sectionLabel) + "\n")
        for (line in payload) {
            sb.append("$line\n")
        }
    }


    private fun getString(@StringRes id: Int): String {
        return resources.getString(id)
    }
}