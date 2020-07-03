package uk.co.alt236.underthehood.commandrunner

class Result(val error: Boolean = false) {
    var isRooted: Boolean = false
        internal set

    var timestamp: Long = 0
        internal set

    var deviceinfo: String = ""
        internal set

    var otherData: List<String> = emptyList()
        internal set

    var ipRouteData: List<String> = emptyList()
        internal set

    var ipConfigData: List<String> = emptyList()
        internal set

    var psData: List<String> = emptyList()
        internal set

    var sysPropData: List<String> = emptyList()
        internal set

    var procData: List<String> = emptyList()
        internal set

    var netstatData: List<String> = emptyList()
        internal set


    internal companion object {
        val ERROR = Result(error = true)
    }
}