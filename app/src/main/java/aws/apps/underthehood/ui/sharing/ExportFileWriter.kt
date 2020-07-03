package aws.apps.underthehood.ui.sharing

import android.content.Context
import android.os.Environment
import android.util.Log
import aws.apps.underthehood.ui.views.UserNotify
import java.io.File
import java.io.FileOutputStream

class ExportFileWriter(context: Context) {
    private val userNotify = UserNotify(context.applicationContext)

    fun exportData(sharePayload: SharePayload) {
        val directory = Environment.getExternalStorageDirectory()
        val filename = sharePayload.fileName
        val contents = sharePayload.text

        if (directory == null) {
            Log.w(TAG, "Unable to write to SD. Folder was null!")
        } else {
            val fullPath = File(directory, filename)
            writeToFile(fullPath, contents)
        }
    }

    private fun writeToFile(file: File, contents: String) {
        try {
            FileOutputStream(file).use { output ->
                output.write(contents.toByteArray(Charsets.UTF_8))
            }
            Log.d(TAG, "Wrote $file")
            userNotify.notify("Wrote '$file'")
        } catch (e: Exception) {
            Log.e(TAG, "Error writing file: " + e.message)
            userNotify.notify("Could not write file: ${e.message}")
        }
    }

    private companion object {
        val TAG = ExportFileWriter::class.java.simpleName
    }
}