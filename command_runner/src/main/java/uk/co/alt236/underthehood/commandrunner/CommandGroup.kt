package uk.co.alt236.underthehood.commandrunner

import android.content.res.Resources
import androidx.annotation.StringRes

internal abstract class CommandGroup(private val res: Resources) {
    private val commandRunner = Cli()

    abstract fun execute(rooted: Boolean): ArrayList<String>

    protected fun getPromptSymbol(root: Boolean): String {
        return if (root) "#" else "$"
    }

    protected fun getString(@StringRes resId: Int): String {
        return res.getString(resId)
    }

    protected fun execute(@StringRes command: Int,
                          isRooted: Boolean): List<String> {
        val commandString = getString(command)
        return execute(commandString, isRooted)
    }

    protected fun execute(command: String,
                          isRooted: Boolean): List<String> {

        val result = if (isRooted) {
            commandRunner.executeAsRoot(command)
        } else {
            commandRunner.execute(command)
        }

        val list = ArrayList<String>(2)
        list.add(getPromptSymbol(isRooted) + command)
        list.add(result.trim { it <= ' ' })

        return list
    }

}