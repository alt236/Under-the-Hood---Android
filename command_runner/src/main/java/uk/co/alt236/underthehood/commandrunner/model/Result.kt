package uk.co.alt236.underthehood.commandrunner.model

class Result(val error: Boolean = false) {
    var isRooted: Boolean = false
        internal set

    var timestamp: Long = 0
        internal set

    var deviceinfo: String = ""
        internal set

    var otherData: List<CommandOutputGroup> = emptyList()
        internal set

    var ipRouteData: List<CommandOutputGroup> = emptyList()
        internal set

    var hardwareData: List<CommandOutputGroup> = emptyList()
        internal set

    var psData: List<CommandOutputGroup> = emptyList()
        internal set

    var sysPropData: List<CommandOutputGroup> = emptyList()
        internal set

    var procData: List<CommandOutputGroup> = emptyList()
        internal set

    var netstatData: List<CommandOutputGroup> = emptyList()
        internal set


    internal companion object {
        val ERROR = Result(error = true)
    }
}