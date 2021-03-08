package com.example.piano.util


//px = dp * (dpi / 160)
fun setSharpPianoKeyPosition(fullToneKeyWidth:Float, dpi:Float, position:Int, padding:Float, sharpToneKeywidth:Float):Float {
    val paddingInDp = padding*dpi/160
    val pos = fullToneKeyWidth*position-sharpToneKeywidth/2 + (paddingInDp/2)*position
    return pos*dpi/160
}
