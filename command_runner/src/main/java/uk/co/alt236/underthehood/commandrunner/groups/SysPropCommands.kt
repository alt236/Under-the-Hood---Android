package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal class SysPropCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): List<CommandOutputGroup> {
        val list = ArrayList<CommandOutput>()

        list.add(execute(R.string.shell_getprop_build_fingerprint, false))
        list.add(execute(R.string.shell_getprop_gsm_baseband, false))
        list.add(execute(R.string.shell_getprop_lcd_density, false))
        list.add(execute(R.string.shell_getprop_product_version, false))
        list.add(execute(R.string.shell_getprop_vm_execution_mode, false))
        list.add(execute(R.string.shell_getprop_vm_heapsize, false))

        return listOf(
                CommandOutputGroup(name = "", commandOutputs = list)
        )
    }
}