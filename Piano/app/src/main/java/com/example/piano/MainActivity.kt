package com.example.piano

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.work.*
import com.example.piano.Workers.FileWorker
import com.example.piano.Workers.FirebaseWorker
import com.example.piano.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var filter = IntentFilter().apply {
        addAction("UPLOAD_FILE")
    }

    val reciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val fileContent = intent?.extras?.get("com.example.piano.FILE").toString()
            val filename = intent?.extras?.get("com.example.piano.FILE_NAME").toString()
            registerWork(filename, fileContent)
            goAsync()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        registerReceiver(reciever, filter)
    }

    private fun registerWork(fileName:String, fileContent:String){
        val fileWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<FileWorker>()
                .setInputData(workDataOf("fileName" to fileName, "fileContent" to fileContent))
                .build()
        val firebaseWorkRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<FirebaseWorker>().build()

        WorkManager.getInstance(this)
                .beginWith(fileWorkRequest)
                .then(firebaseWorkRequest)
                .enqueue()
    }

}