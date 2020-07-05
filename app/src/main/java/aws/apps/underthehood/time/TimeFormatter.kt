package aws.apps.underthehood.time

import java.text.SimpleDateFormat
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
class TimeFormatter {

    private val isoFormatter: SimpleDateFormat = UtcDateFormatter(ISO_FORMAT, Locale.US)
    private val isoFormatterFileSafe: SimpleDateFormat = UtcDateFormatter(ISO_FORMAT_FILE_SAFE, Locale.US)

    fun getIsoDateTime(date: Date): String {
        return isoFormatter.format(date)
    }

    fun getIsoDateTime(millis: Long): String {
        return getIsoDateTime(Date(millis))
    }

    fun getIsoDateTimeFileSafe(date: Date): String {
        return isoFormatterFileSafe.format(date)
    }

    fun getIsoDateTimeFileSafe(millis: Long): String {
        return getIsoDateTimeFileSafe(Date(millis))
    }

    private companion object {
        private const val ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS zzz"
        private const val ISO_FORMAT_FILE_SAFE = "yyyy-MM-dd-HH-mm-ss-SSS-zzz"
    }
}