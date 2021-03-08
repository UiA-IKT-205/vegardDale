package com.example.piano.data

data class Note(val value:String, val duration:Long){
    override fun toString(): String {
        return "${value}, ${duration}\n"
    }
}