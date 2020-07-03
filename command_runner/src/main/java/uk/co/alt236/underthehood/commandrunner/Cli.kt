package uk.co.alt236.underthehood.commandrunner

import android.util.Log
import uk.co.alt236.underthehood.commandrunner.core.ExecTerminal
import uk.co.alt236.underthehood.commandrunner.core.TerminalOutput

internal class Cli {
    private val terminal = ExecTerminal()

    fun execute(cmd: String): TerminalOutput {
        return terminal.exec(cmd)
    }

    fun executeAsRoot(cmd: String): TerminalOutput {
        return terminal.execSu(cmd)
    }

    fun checkIfSu(): Boolean {
        val res = terminal.execSu("su && echo 1")
        println(res)
        return if (res.stdOut.trim { it <= ' ' } == "1") {
            Log.d(TAG, "Device CAN do SU")
            true
        } else {
            Log.d(TAG, "Device CAN NOT do SU")
            false
        }
    }

    private companion object {
        private val TAG = Cli::class.java.simpleName
    }
}