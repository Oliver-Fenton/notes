// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteTest {
    @Test
    fun noteTimeUpdateOnTitleChange() {
        val note1 = NoteData(1, "Note 1")
        Thread.sleep(1_000)
        note1.setNoteTitle("Hello World!")
        val dateEdited = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy '@' h:mm a")

        Assertions.assertEquals(dateEdited.format(formatter), note1.dateEdited.format(formatter))
    }

    @Test
    fun undoAWord() {
        val note1 = NoteData(1, "Note 1")
        note1.addToUndoStack(TextChange.INSERT)
        Assertions.assertEquals(note1.undoStack.size, 2)
    }

    @Test
    fun redoAWord() {
        val note1 = NoteData(1, "Note 1")
        note1.addToUndoStack(TextChange.INSERT)
        note1.redoStack.add(note1.undoStack.last())
        note1.undoStack.removeLast()
        Assertions.assertEquals(note1.undoStack.size, 1)
        Assertions.assertEquals(note1.redoStack.size, 1)
    }
}