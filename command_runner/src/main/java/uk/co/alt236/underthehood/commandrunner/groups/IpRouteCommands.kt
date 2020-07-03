package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R

internal class IpRouteCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): ArrayList<String> {


        val list = ArrayList<String>()

        list.add(getString(R.string.seperator_identifier) + "IPv4")

        list.addAll(execute(R.string.shell_ip_addr, rooted))

        list.addAll(execute(R.string.shell_ip_neigh, rooted))

        list.addAll(execute(R.string.shell_ip_route_show, rooted))

        list.addAll(execute(R.string.shell_route_n, rooted))



        list.add(getString(R.string.seperator_identifier) + "IPv6")

        list.addAll(execute(R.string.shell_ip_inet6_addr, rooted))

        list.addAll(execute(R.string.shell_ip_inet6_route_show, rooted))

        list.addAll(execute(R.string.shell_route_inet6, rooted))

        return list
    }
}