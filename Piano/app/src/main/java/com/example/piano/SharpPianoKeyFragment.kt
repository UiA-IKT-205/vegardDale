package com.example.piano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.piano.databinding.FragmentSharpPianoKeysBinding
import kotlinx.android.synthetic.main.fragment_sharp_piano_keys.view.*

class SharpPianoKeyFragment : Fragment() {

    private lateinit var note:String
    private var _binding:FragmentSharpPianoKeysBinding? = null
    private val binding get() = _binding!!

    var xPos:Float = 0f
    var onKeyDown:((note:String) -> Unit)? = null
    var onKeyUp:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
            xPos = it.getFloat("XPOS")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSharpPianoKeysBinding.inflate(layoutInflater)
        val view = binding.root
        view.sharpPianoKeyLayout.translationX = xPos

        view.sharpPianoKey.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@SharpPianoKeyFragment.onKeyDown?.invoke(note)
                    MotionEvent.ACTION_UP-> this@SharpPianoKeyFragment.onKeyUp?.invoke(note)
                }
                return true
            }
        })

        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(note:String, xPos:Float) =
                SharpPianoKeyFragment().apply {
                    arguments = Bundle().apply {
                        putString("NOTE", note)
                        putFloat("XPOS", xPos)
                    }
                }
    }
}