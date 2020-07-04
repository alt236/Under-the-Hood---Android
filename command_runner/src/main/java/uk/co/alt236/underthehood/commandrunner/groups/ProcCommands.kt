package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class ProcCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        return listOf(
                collectGeneralInfo(rooted),
                collectHardwareInfo(rooted),
                collectNetworkInfo(rooted)
        )
    }

    private fun collectGeneralInfo(rooted: Boolean): CommandOutputGroup {
        val commands = getStringArray(R.array.commands_proc_general)
        val list = execute(commands, rooted)

        return CommandOutputGroup(
                name = "General Info",
                commandOutputs = list
        )
    }

    private fun collectHardwareInfo(rooted: Boolean): CommandOutputGroup {
        val commands = getStringArray(R.array.commands_proc_hardware)
        val list = execute(commands, rooted)

        return CommandOutputGroup(
                name = "Hardware Info",
                commandOutputs = list
        )
    }

    private fun collectNetworkInfo(rooted: Boolean): CommandOutputGroup {
        val commands = getStringArray(R.array.commands_proc_network)
        val list = execute(commands, rooted)

        return CommandOutputGroup(
                name = "Network Info",
                commandOutputs = list
        )
    }
}