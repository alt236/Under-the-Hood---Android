package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class NetstatCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_netstat_r, rooted))
        list.add(execute(R.string.shell_netstat_a, rooted))

        return listOf(
                CommandOutputGroup(
                        name = "",
                        commandOutputs = list
                )
        )
    }
}