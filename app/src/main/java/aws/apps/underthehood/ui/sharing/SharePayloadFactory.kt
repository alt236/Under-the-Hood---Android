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
//        sb.append("---------------------------------\n\n")
//        sb.append(tableConverter.tableToString(tableDeviceInfo));
//        sb.append(getString(R.string.label_ipconfig_info) + "\n");
//        sb.append(tableConverter.tableToString(tableIpconfig));
//        sb.append(getString(R.string.label_ip_route_info) + "\n");
//        sb.append(tableConverter.tableToString(tableIpRoute));
//        sb.append(getString(R.string.label_proc_info) + "\n");
//        sb.append(tableConverter.tableToString(tableProc));
//        sb.append(getString(R.string.label_netlist_info) + "\n");
//        sb.append(tableConverter.tableToString(tableNetstat));
//        sb.append(getString(R.string.label_ps_info) + "\n");
//        sb.append(tableConverter.tableToString(tablePs));
//        sb.append(getString(R.string.label_sys_prop) + "\n");
//        sb.append(tableConverter.tableToString(tableSysProp));
//        sb.append(getString(R.string.label_other_info) + "\n");
//        sb.append(tableConverter.tableToString(tableOther));
        sb.append("\n\n---------------------------------")
        return sb.toString().trim { it <= ' ' }
    }

    private fun getString(@StringRes id: Int): String {
        return resources.getString(id)
    }
}