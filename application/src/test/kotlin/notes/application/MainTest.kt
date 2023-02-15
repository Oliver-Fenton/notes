package notes.application

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun simpleTest() {
        assertEquals(1, 1)
    }

    @Test
    fun addNotes() {
        var m = Main()
        assertEquals(m.notesModel.notes.size, m.notesModel.notes.size)
        val tempSize = m.notesModel.notes.size
        for (i in 0..100) {
            m.notesModel.createNote()
            // Check that the note body is blank
            assertEquals(m.notesModel.notes[i + tempSize].getBody(), "")
            // Check that the note is active
            assertEquals(m.notesModel.notes[i + tempSize].isActive, true)
        }
        // Check that we have added exactly 101 new notes
        assertEquals(m.notesModel.notes.size, tempSize + 101)
    }
}