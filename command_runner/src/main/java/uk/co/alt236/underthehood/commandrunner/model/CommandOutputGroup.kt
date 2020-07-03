package uk.co.alt236.underthehood.commandrunner.model

data class CommandOutputGroup(
        val name: String,
        val commandOutputs: List<CommandOutput>)