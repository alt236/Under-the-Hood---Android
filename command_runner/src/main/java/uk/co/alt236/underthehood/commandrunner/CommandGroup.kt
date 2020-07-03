package uk.co.alt236.underthehood.commandrunner

import android.content.res.Resources
import androidx.annotation.StringRes
import uk.co.alt236.underthehood.commandrunner.core.TerminalOutput

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

        val commandOutput = normaliseOutput(result)

        list.add(commandOutput)

        return list
    }

    private fun normaliseOutput(result: TerminalOutput): String {
        val commandHadOutput = result.stdOut.trim().isNotBlank()
        val commandHadError = result.stdErr.trim().isNotBlank()

        return when {
            commandHadOutput -> result.stdOut.trim()
            result.exitValue == 0 -> "[Command produced no output]"
            commandHadError -> result.stdErr.trim()
            else -> {
                "[Command failed with no output. Exit code: ${result.exitValue}]"
            }
        }

    }

}