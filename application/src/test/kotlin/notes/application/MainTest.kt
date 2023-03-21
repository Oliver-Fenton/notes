package notes.application

import notes.shared.model.Model
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainTest {

    @Test
    fun simpleTest() {
        assertEquals(1, 1)
    }

  //  @Test
//    fun addNotes() {
//        var model = Model()
//        assertEquals(model.notes.size, model.notes.size)
//        val tempSize = model.notes.size
//        for (i in 0..100) {
//            model.createNote()
//            // Check that the note body is blank
//            assertEquals(model.notes[i + tempSize].getHTML(), "")
//            // Check that the note is active
//            assertEquals(model.notes[i + tempSize].isActive, true)
//        }
//        // Check that we have added exactly 101 new notes
//        assertEquals(model.notes.size, tempSize + 101)
//    }
}