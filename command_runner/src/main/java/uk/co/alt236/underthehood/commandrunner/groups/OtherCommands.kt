package uk.co.alt236.underthehood.commandrunner.groups

import android.content.res.Resources
import uk.co.alt236.underthehood.commandrunner.CommandGroup
import uk.co.alt236.underthehood.commandrunner.R
import java.util.*
import kotlin.collections.ArrayList

internal class OtherCommands internal constructor(res: Resources) : CommandGroup(res) {

    override fun execute(action_list: Hashtable<CharSequence, Boolean>,
                         rooted: Boolean): ArrayList<String> {
        val list = ArrayList<String>()

        list.add(getString(R.string.seperator_identifier) + "System info")

        list.addAll(execute(R.string.shell_df_ah, action_list, rooted))

        list.addAll(execute(R.string.shell_uname_a, action_list, rooted))

        list.addAll(execute(R.string.shell_lsmod, action_list, rooted))

        return list
    }
}