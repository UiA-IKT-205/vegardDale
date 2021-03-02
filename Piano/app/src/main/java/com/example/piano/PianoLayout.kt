package com.example.piano

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.storage.StorageManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import com.example.piano.data.Note
import com.example.piano.databinding.FragmentPianoLayoutBinding
import com.example.piano.util.convertCurrentTimeToPlayTime
import com.example.piano.util.setSharpPianoKeyPosition
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*
import java.io.File
import java.io.FileOutputStream



class PianoLayout : Fragment() {

    private var _binding:FragmentPianoLayoutBinding? = null
    private val binding get() = _binding!!
    private val fullTonePianoKeys = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2","A2","B2")
    private val sharpPianoKeys = listOf("C#","D#","F#","G#","A#","C2#","D2#","F2#","G2#","A2#")
    private val sharpPianoKeyPositions = mutableListOf(1,2,4,5,6,8,9,11,12,13)
    var score:MutableList<Note> = mutableListOf<Note>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoLayoutBinding.inflate(layoutInflater)

        val view = binding.root
        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullTonePianoKeys.forEach {
            var pianoKeyDownStartTime:Long = 0L
            val naturalTonePianoKey = NaturalTonePianoKeyFragment.newInstance(it)
            naturalTonePianoKey.onKeyDown = {
                pianoKeyDownStartTime = System.currentTimeMillis()
                println(it)
            }
            naturalTonePianoKey.onKeyUp = {
                val pianoKeyUpTime:Long = System.currentTimeMillis()
                val playTime = convertCurrentTimeToPlayTime(pianoKeyDownStartTime, pianoKeyUpTime)
                val note = Note(it, playTime)
                score.add(note)
                println(note)

            }
            ft.add(view.naturalTonePianoKeys.id, naturalTonePianoKey, "note_$it")
        }

        sharpPianoKeys.forEach {
            var pianoKeyDownStartTime:Long = 0L
            val sharpPianoKeyPosition = setSharpPianoKeyPosition(50f, 440f,sharpPianoKeyPositions.removeAt(0), 1f, 30f)
            val sharpPianoKey = SharpPianoKeyFragment.newInstance(it, sharpPianoKeyPosition)
            sharpPianoKey.onKeyDown = {
                pianoKeyDownStartTime = System.currentTimeMillis()
                println(it)
            }
            sharpPianoKey.onKeyUp = {
                val pianoKeyUpTime:Long = System.currentTimeMillis()
                val playTime = convertCurrentTimeToPlayTime(pianoKeyDownStartTime, pianoKeyUpTime)
                val note = Note(it, playTime)
                score.add(note)
                println(note)
            }
            ft.add(view.sharpTonePianoKeys.id, sharpPianoKey, "note_$it")
        }

        ft.commit()
        view.saveScore.setOnClickListener(){
            val fileName = view.fileNameInput.text.toString()
            val filePath = this.activity?.getExternalFilesDir(null)
            val file = File(filePath, "$fileName.music")

            if(checkFileCredentials(score, fileName, filePath)){
                if(checkFileAvailability(file)){
                    FileOutputStream(file, true).bufferedWriter().use { writer ->
                        score.forEach {
                            writer.write(it.toString())
                        }
                    }
                }
            }

        }

        return view
    }
    private fun checkFileAvailability(file:File):Boolean{
        if (!file.exists()){
            Toast.makeText(this.activity, "File already exists", Toast.LENGTH_SHORT).show()
            return false
        }
        if(!file.canWrite()){
            Toast.makeText(this.activity, "the file is used by another process", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkFileCredentials(score:MutableList<Note>, filename:String, filepath:File?):Boolean{
        if (score.count() == 0){
            Toast.makeText(this.activity, "score is empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (filename.isEmpty()){
            Toast.makeText(this.activity, "filename is empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (filepath == null){
            Toast.makeText(this.activity, "something went wrong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }




}