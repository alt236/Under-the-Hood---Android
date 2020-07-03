package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R

internal class SysPropCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(rooted: Boolean): ArrayList<String> {
        val list = ArrayList<String>()

        list.addAll(execute(R.string.shell_getprop_product_version, false))

        list.addAll(execute(R.string.shell_getprop_build_fingerprint, false))

        list.addAll(execute(R.string.shell_getprop_gsm_baseband, false))

        list.addAll(execute(R.string.shell_getprop_vm_execution_mode, false))

        list.addAll(execute(R.string.shell_getprop_vm_heapsize, false))

        list.addAll(execute(R.string.shell_getprop_lcd_density, false))

        return list
    }
}