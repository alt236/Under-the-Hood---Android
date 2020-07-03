package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R

internal class IpConfigCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): ArrayList<String> {

        val list = ArrayList<String>()

        list.addAll(execute(R.string.shell_netcfg, rooted))

        list.addAll(execute(R.string.shell_ifconfig_dash_a, rooted))

        return list
    }
}