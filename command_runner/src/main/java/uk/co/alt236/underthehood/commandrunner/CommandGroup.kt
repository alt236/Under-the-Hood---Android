package uk.co.alt236.underthehood.commandrunner

import android.content.res.Resources
import androidx.annotation.StringRes
import java.util.*
import kotlin.collections.ArrayList

internal abstract class CommandGroup(private val res: Resources) {
    private val commandRunner = CommandRunner()

    abstract fun execute(
            action_list: Hashtable<CharSequence, Boolean>,
            rooted: Boolean): ArrayList<String>

    protected fun getPromptSymbol(root: Boolean): String {
        return if (root) "#" else "$"
    }

    protected fun getString(@StringRes resId: Int): String {
        return res.getString(resId)
    }

    protected fun execute(@StringRes command: Int,
                          action_list: Hashtable<CharSequence, Boolean>,
                          isRooted: Boolean): List<String> {
        val commandString = getString(command)
        return execute(commandString, action_list, isRooted);
    }

    protected fun execute(command: String,
                          action_list: Hashtable<CharSequence, Boolean>,
                          isRooted: Boolean): List<String> {

        val result = if (action_list[command]!!) {
            if (isRooted) {
                commandRunner.executeAsRoot(command)
            } else {
                commandRunner.execute(command)
            }
        } else {
            "User Interrupt"
        }

        val list = ArrayList<String>(2)
        list.add(getPromptSymbol(isRooted) + command)
        list.add(result.trim { it <= ' ' })

        return list;
    }

}