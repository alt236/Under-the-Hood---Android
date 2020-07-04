package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class IpRouteCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        return listOf(
                runIpV4Commands(rooted),
                runIpV6Commands(rooted),
                runConfigCommands(rooted)
        )
    }

    private fun runIpV4Commands(rooted: Boolean): CommandOutputGroup {
        val commands = getStringArray(R.array.commands_ip_v4)
        val list = execute(commands, rooted)

        return CommandOutputGroup(
                name = "IPv4",
                commandOutputs = list
        )
    }

    private fun runIpV6Commands(rooted: Boolean): CommandOutputGroup {
        val commands = getStringArray(R.array.commands_ip_v6)
        val list = execute(commands, rooted)

        return CommandOutputGroup(
                name = "IPv6",
                commandOutputs = list
        )
    }

    private fun runConfigCommands(rooted: Boolean): CommandOutputGroup {
        val commands = getStringArray(R.array.commands_ip_config)
        val list = execute(commands, rooted)

        return CommandOutputGroup(
                name = "Config",
                commandOutputs = list
        )
    }
}