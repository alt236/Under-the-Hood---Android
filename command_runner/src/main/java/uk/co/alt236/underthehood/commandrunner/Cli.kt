package uk.co.alt236.underthehood.commandrunner

import android.util.Log
import uk.co.alt236.underthehood.commandrunner.core.ExecTerminal

internal class Cli {

    fun execute(cmd: String): String {
        val et = ExecTerminal()
        val res = et.exec(cmd)
        return sanitise(res)
    }

    fun executeAsRoot(cmd: String): String {
        val et = ExecTerminal()
        val res = et.execSu(cmd)
        return sanitise(res)
    }

    fun checkIfSu(): Boolean {
        val et = ExecTerminal()
        val res = et.execSu("su && echo 1")
        return if (res.trim { it <= ' ' } == "1") {
            Log.d(TAG, "Device CAN do SU")
            true
        } else {
            Log.d(TAG, "Device CAN NOT do SU")
            false
        }
    }

    private fun sanitise(result: String): String {
        return if (result.trim { it <= ' ' } == "") {
            NO_OUTPUT
        } else {
            result
        }
    }

    private companion object {
        private const val NO_OUTPUT = "result was blank"
        private val TAG = Cli::class.java.simpleName
    }
}