package com.example.piano.Workers

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.io.File
import java.io.FileOutputStream

class FileWorker(appContext:Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val content = this.inputData.getString("fileContent").toString()
        val filename= this.inputData.getString("fileName").toString()
        val file = saveFile(filename, content)
        return Result.success(workDataOf("fileUri" to file.toUri().toString()))
    }

    private fun saveFile(filename:String, content:String):File {
        val filePath = this.applicationContext.getExternalFilesDir(null)
        val file = File(filePath, "$filename.music")
        FileOutputStream(file, true).bufferedWriter().use { writer ->
            writer.write(content)
        }
        return file
    }
}