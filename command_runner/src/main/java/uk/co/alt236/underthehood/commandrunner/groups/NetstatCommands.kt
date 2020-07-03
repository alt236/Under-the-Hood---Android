package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R

internal class NetstatCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): ArrayList<String> {

        val list = ArrayList<String>()

        list.addAll(execute(R.string.shell_netstat_r, rooted))

        list.addAll(execute(R.string.shell_netstat_a, rooted))

        return list
    }
}