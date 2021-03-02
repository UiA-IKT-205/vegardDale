package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentNaturalTonePianoKeyBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_natural_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*

class NaturalTonePianoKeyFragment : Fragment() {

    private lateinit var note:String
    private var _binding: FragmentNaturalTonePianoKeyBinding? = null
    private val binding get() = _binding!!
    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNaturalTonePianoKeyBinding.inflate(layoutInflater)
        val view = binding.root

        view.naturalTonePianoKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@NaturalTonePianoKeyFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP -> this@NaturalTonePianoKeyFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note:String) =
            NaturalTonePianoKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}