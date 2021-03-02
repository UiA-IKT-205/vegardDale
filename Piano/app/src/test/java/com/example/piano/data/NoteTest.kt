package com.example.piano.data
import org.junit.Test
import org.junit.Assert.*




class NoteTest {
    @Test
    fun test_note_return_value()
    {
        assertEquals("A, 123\n", Note("A", 123).toString())
        assertEquals("F#, 402\n", Note("F#", 402).toString())
        assertEquals("C, 1\n", Note("C", 1).toString())
    }
}