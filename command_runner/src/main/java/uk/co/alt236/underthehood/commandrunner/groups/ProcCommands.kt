package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup
import java.util.*

internal class ProcCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        return listOf(
                collectGeneralInfo(rooted),
                collectHardwareInfo(rooted),
                collectNetworkInfo(rooted)
        )
    }

    private fun collectGeneralInfo(rooted: Boolean): CommandOutputGroup {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_cat_proc_version, rooted))
        list.add(execute(R.string.shell_cat_proc_meminfo, rooted))

        return CommandOutputGroup(
                name = "General Info",
                commandOutputs = list
        )
    }

    private fun collectHardwareInfo(rooted: Boolean): CommandOutputGroup {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_cat_proc_devices, rooted))
        list.add(execute(R.string.shell_cat_proc_mounts, rooted))

        return CommandOutputGroup(
                name = "Hardware Info",
                commandOutputs = list
        )
    }

    private fun collectNetworkInfo(rooted: Boolean): CommandOutputGroup {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_cat_proc_net_arp, rooted))
        list.add(execute(R.string.shell_cat_proc_net_route, rooted))
        list.add(execute(R.string.shell_cat_proc_net_wireless, rooted))
        list.add(execute(R.string.shell_cat_proc_net_if_inet6, rooted))
        list.add(execute(R.string.shell_cat_proc_net_ipv6_route, rooted))

        return CommandOutputGroup(
                name = "Network Info",
                commandOutputs = list
        )
    }
}