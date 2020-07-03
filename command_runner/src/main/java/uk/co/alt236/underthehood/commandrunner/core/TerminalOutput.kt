package uk.co.alt236.underthehood.commandrunner.core

data class TerminalOutput(
        val asRoot: Boolean,
        val command: String,
        val exitValue: Int,
        val stdOut: String,
        val stdErr: String)