package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class NetstatCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        val commands = getStringArray(R.array.commands_netstat)
        val list = execute(commands, rooted)

        return listOf(
                CommandOutputGroup(
                        name = "",
                        commandOutputs = list
                )
        )
    }
}