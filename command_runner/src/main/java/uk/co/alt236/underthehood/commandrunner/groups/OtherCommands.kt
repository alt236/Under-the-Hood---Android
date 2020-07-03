package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class OtherCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_uname_a, rooted))
        list.add(execute(R.string.shell_df_ah, rooted))
        list.add(execute(R.string.shell_lsmod, rooted))

        return listOf(
                CommandOutputGroup(
                        name = "System info",
                        commandOutputs = list
                )
        )
    }
}