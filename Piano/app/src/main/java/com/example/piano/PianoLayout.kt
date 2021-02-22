package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentPianoLayoutBinding
import com.example.piano.util.setSharpPianoKeyPosition
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoLayoutBinding? = null
    private val binding get() = _binding!!
    private val fullTonePianoKeys = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2","A2","B2")
    private val sharpPianoKeys = listOf("C#","D#","F#","G#","A#","C2#","D2#","F2#","G2#","A2#")
    private val sharpPianoKeyPositions = mutableListOf(1,2,4,5,6,8,9,11,12,13)

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
            val naturalTonePianoKey = NaturalTonePianoKeyFragment.newInstance(it)
            naturalTonePianoKey.onKeyDown = { println(it)}
            naturalTonePianoKey.onKeyUp = { println(it)}
            ft.add(view.naturalTonePianoKeys.id, naturalTonePianoKey, "note_$it")
        }

        sharpPianoKeys.forEach {
            val sharpPianoKeyPosition = setSharpPianoKeyPosition(50f, 440f,sharpPianoKeyPositions.removeAt(0), 1f, 30f)
            val sharpPianoKey = SharpPianoKeyFragment.newInstance(it, sharpPianoKeyPosition)
            sharpPianoKey.onKeyDown = {println(it)}
            sharpPianoKey.onKeyUp = { println(it)}
            ft.add(view.sharpTonePianoKeys.id, sharpPianoKey, "note_$it")
        }

        ft.commit()

        return view
    }
}