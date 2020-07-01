package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import java.util.*

internal class ProcCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(action_list: Hashtable<CharSequence, Boolean>,
                         isRooted: Boolean): ArrayList<String> {
        val list = ArrayList<String>()

        list.add(getString(R.string.seperator_identifier) + "General Info")
        list.addAll(execute(R.string.shell_cat_proc_version, action_list, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_meminfo, action_list, isRooted))

        list.add(getString(R.string.seperator_identifier) + "Hardware Info")
        list.addAll(execute(R.string.shell_cat_proc_devices, action_list, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_mounts, action_list, isRooted))

        list.add(getString(R.string.seperator_identifier) + "Network Info")
        list.addAll(execute(R.string.shell_cat_proc_net_arp, action_list, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_route, action_list, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_wireless, action_list, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_if_inet6, action_list, isRooted))
        list.addAll(execute(R.string.shell_cat_proc_net_ipv6_route, action_list, isRooted))

        return list
    }
}