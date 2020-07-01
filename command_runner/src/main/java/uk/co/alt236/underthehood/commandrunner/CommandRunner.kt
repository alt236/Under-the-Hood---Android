package uk.co.alt236.underthehood.commandrunner

import uk.co.alt236.underthehood.commandrunner.core.ExecTerminal

internal class CommandRunner {

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

    private fun sanitise(result: String): String {
        return if (result.trim { it <= ' ' } == "") {
            NO_OUTPUT
        } else {
            result
        }
    }

    private companion object {
        private const val NO_OUTPUT = "result was blank"
    }
}