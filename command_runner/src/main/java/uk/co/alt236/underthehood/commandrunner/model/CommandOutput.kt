package uk.co.alt236.underthehood.commandrunner.model

class CommandOutput(
        val successful: Boolean,
        val asRoot: Boolean,
        val command: String,
        val output: String) {


    fun getPromptSymbol(): String {
        return if (asRoot) "#" else "$"
    }

    val commandWithPrompt = getPromptSymbol() + command
}