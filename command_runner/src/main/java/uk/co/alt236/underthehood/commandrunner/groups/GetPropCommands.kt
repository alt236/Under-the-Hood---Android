package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import java.util.*
import kotlin.collections.ArrayList

internal class GetPropCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(action_list: Hashtable<CharSequence, Boolean>,
                         rooted: Boolean): ArrayList<String> {
        val list = ArrayList<String>()

        list.addAll(execute(R.string.shell_getprop_product_version, action_list, false))

        list.addAll(execute(R.string.shell_getprop_build_fingerprint, action_list, false))

        list.addAll(execute(R.string.shell_getprop_gsm_baseband, action_list, false))

        list.addAll(execute(R.string.shell_getprop_vm_execution_mode, action_list, false))

        list.addAll(execute(R.string.shell_getprop_vm_heapsize, action_list, false))

        list.addAll(execute(R.string.shell_getprop_lcd_density, action_list, false))

        return list
    }
}