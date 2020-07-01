package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import java.util.*
import kotlin.collections.ArrayList

internal class IpRouteCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(action_list: Hashtable<CharSequence, Boolean>,
                         rooted: Boolean): ArrayList<String> {


        val list = ArrayList<String>()

        list.add(getString(R.string.seperator_identifier) + "IPv4")

        list.addAll(execute(R.string.shell_ip_addr, action_list, rooted))

        list.addAll(execute(R.string.shell_ip_neigh, action_list, rooted))

        list.addAll(execute(R.string.shell_ip_route_show, action_list, rooted))

        list.addAll(execute(R.string.shell_route_n, action_list, rooted))



        list.add(getString(R.string.seperator_identifier) + "IPv6")

        list.addAll(execute(R.string.shell_ip_inet6_addr, action_list, rooted))

        list.addAll(execute(R.string.shell_ip_inet6_route_show, action_list, rooted))

        list.addAll(execute(R.string.shell_route_inet6, action_list, rooted))

        return list
    }
}