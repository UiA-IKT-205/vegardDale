package com.example.piano.Workers

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage


class FirebaseWorker(appcontext: Context, workerParams: WorkerParameters): Worker(appcontext, workerParams) {
    private val auth:FirebaseAuth = Firebase.auth
    private val Tag:String = "piano:MainActivity"

    override fun doWork(): Result {
        signInAnonymously()
        val file = this.inputData.getString("fileUri").toString()
        upload(file.toUri())
        return Result.success()
    }

    private fun upload(file: Uri){
        Log.d(Tag, "upload file $file")
        val ref = FirebaseStorage.getInstance().reference.child("melodies/${file.lastPathSegment}")
        val uploadTask = ref.putFile(file)

        uploadTask.addOnSuccessListener {
            Log.d(Tag, "saved file to fb ${it.toString()}")
        }.addOnFailureListener{
            Log.e(Tag, "failed to save file to fb ${it.toString()}")
        }
    }

    private fun signInAnonymously(){
        auth.signInAnonymously().addOnSuccessListener {
            Log.d(Tag, "login sucess ${it?.user.toString()}")
        }.addOnFailureListener{
            Log.e(Tag, "login failed", it)
        }
    }

}