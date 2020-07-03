package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import java.util.*

internal class ProcCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(isRooted: Boolean): ArrayList<String> {
        val list = ArrayList<String>()

        list.add(getString(R.string.seperator_identifier) + "General Info")
        list.addAll(execute(R.string.shell_cat_proc_version, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_meminfo, isRooted))

        list.add(getString(R.string.seperator_identifier) + "Hardware Info")
        list.addAll(execute(R.string.shell_cat_proc_devices, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_mounts, isRooted))

        list.add(getString(R.string.seperator_identifier) + "Network Info")
        list.addAll(execute(R.string.shell_cat_proc_net_arp, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_route, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_wireless, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_if_inet6, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_ipv6_route, isRooted))

        return list
    }
}