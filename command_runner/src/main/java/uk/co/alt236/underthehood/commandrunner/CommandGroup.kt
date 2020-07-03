package uk.co.alt236.underthehood.commandrunner

import android.content.res.Resources
import androidx.annotation.StringRes
import uk.co.alt236.underthehood.commandrunner.core.TerminalOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutput
import uk.co.alt236.underthehood.commandrunner.model.CommandOutputGroup

internal abstract class CommandGroup(private val res: Resources) {
    private val commandRunner = Cli()

    abstract fun execute(rooted: Boolean): List<CommandOutputGroup>

    protected fun getString(@StringRes resId: Int): String {
        return res.getString(resId)
    }

    protected fun execute(@StringRes command: Int,
                          isRooted: Boolean): CommandOutput {
        val commandString = getString(command)
        return execute(commandString, isRooted)
    }

    protected fun execute(command: String,
                          isRooted: Boolean): CommandOutput {

        val result = if (isRooted) {
            commandRunner.executeAsRoot(command)
        } else {
            commandRunner.execute(command)
        }

        val normalised = normaliseOutput(result)

        return CommandOutput(
                asRoot = isRooted,
                successful = !normalised.isError,
                command = command,
                output = normalised.output)
    }

    private fun normaliseOutput(result: TerminalOutput): NormalisedOutput {
        val commandHadOutput = result.stdOut.trim().isNotBlank()
        val commandHadError = result.stdErr.trim().isNotBlank()

        return when {
            commandHadOutput -> NormalisedOutput(result.stdOut.trim())
            result.exitValue == 0 -> NormalisedOutput("[Command produced no output]")
            commandHadError -> NormalisedOutput(result.stdErr.trim(), isError = true)
            else -> {
                NormalisedOutput("[Command failed with no output. Exit code: ${result.exitValue}]", isError = true)
            }
        }

    }

    private data class NormalisedOutput(val output: String, val isError: Boolean = false)
}