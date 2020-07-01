package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import java.util.*
import kotlin.collections.ArrayList

internal class IpConfigCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(action_list: Hashtable<CharSequence, Boolean>,
                         rooted: Boolean): ArrayList<String> {

        val list = ArrayList<String>()

        list.addAll(execute(R.string.shell_netcfg, action_list, rooted))

        list.addAll(execute(R.string.shell_ifconfig_dash_a, action_list, rooted))

        return list
    }
}