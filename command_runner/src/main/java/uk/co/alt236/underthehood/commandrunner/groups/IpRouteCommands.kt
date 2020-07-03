package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class IpRouteCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        return listOf(
                runIpV4Commands(rooted),
                runIpV6Commands(rooted)
        )
    }

    private fun runIpV4Commands(rooted: Boolean): CommandOutputGroup {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_ip_addr, rooted))
        list.add(execute(R.string.shell_ip_neigh, rooted))
        list.add(execute(R.string.shell_ip_route_show, rooted))
        list.add(execute(R.string.shell_route_n, rooted))

        return CommandOutputGroup(
                name = "IPv4",
                commandOutputs = list
        )
    }

    private fun runIpV6Commands(rooted: Boolean): CommandOutputGroup {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_ip_inet6_addr, rooted))
        list.add(execute(R.string.shell_ip_inet6_route_show, rooted))
        list.add(execute(R.string.shell_route_inet6, rooted))

        return CommandOutputGroup(
                name = "IPv6",
                commandOutputs = list
        )
    }
}