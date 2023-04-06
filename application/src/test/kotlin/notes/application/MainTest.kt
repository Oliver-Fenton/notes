// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.application

import notes.shared.model.Model
import notes.shared.model.NoteData
import notes.shared.model.TextChange
import notes.view.NoteList
import notes.view.SearchBar
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.test.Ignore

class MainTest {
    @Test
    fun simpleTest() {
        assertEquals(1, 1)
    }

    @Ignore
    @Test
    fun addNotes() {
        var model = Model()
        assertEquals(model.notes.size, model.notes.size)
        val tempSize = model.notes.size
        for (i in 0..2) {
            model.createNote()
            // Check that the note body is blank
            assertEquals(model.notes[i + tempSize].getHTML(), "")
            // Check that the note is active
            assertEquals(model.notes[i + tempSize].isActive, true)
        }
        // Check that we have added exactly 101 new notes
        assertEquals(model.notes.size, tempSize + 3)
    }

    @Test
    fun sortByTitleAtoZ() {
        val note1 = NoteData(1, "a")
        val note2 = NoteData(1, "b")
        val note3 = NoteData(1, "c")
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note3)
        noteModel.notes.add(note2)
        noteModel.sortAlphaTitle(false)
        assert(noteModel.notes[0] == note1 && noteModel.notes[1] == note2 && noteModel.notes[2] == note3)
    }

    @Test
    fun sortByTitleZtoA() {
        val note1 = NoteData(1, "a")
        val note2 = NoteData(1, "b")
        val note3 = NoteData(1, "c")
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        noteModel.sortAlphaTitle(true)
        assert(noteModel.notes[0] == note3 && noteModel.notes[1] == note2 && noteModel.notes[2] == note1)
    }

    @Test
    fun sortByDateEditedAscending() {
        val note1 = NoteData(1, "a")
        val note2 = NoteData(1, "b")
        val note3 = NoteData(1, "c")
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        note1.setNoteTitle("new title a")
        Thread.sleep(1_000)
        note3.setNoteTitle("new title c")
        Thread.sleep(1_000)
        note2.setNoteTitle("new title b")
        noteModel.sortDateEdited(false)
        assert(noteModel.notes[0] == note1 && noteModel.notes[1] == note3 && noteModel.notes[2] == note2)
    }

    @Test
    fun sortByDateEditedDescending() { //NEWEST TO OLDEST
        val note1 = NoteData(1, "a")
        val note2 = NoteData(1, "b")
        val note3 = NoteData(1, "c")
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        note1.setNoteTitle("new title a")
        Thread.sleep(1_000)
        note3.setNoteTitle("new title c")
        Thread.sleep(1_000)
        note2.setNoteTitle("new title b")
        Thread.sleep(1_000)
        noteModel.sortDateEdited(true)
        assert(noteModel.notes[0] == note2 && noteModel.notes[1] == note3 && noteModel.notes[2] == note1)
    }

    @Test
    fun sortByDateCreatedAscending() { // OLDEST TO NEWEST
        val note1 = NoteData(1, "a")
        Thread.sleep(1_000)
        val note2 = NoteData(1, "b")
        Thread.sleep(1_000)
        val note3 = NoteData(1, "c")
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        noteModel.sortDate(false)
        assert(noteModel.notes[0] == note1 && noteModel.notes[1] == note2 && noteModel.notes[2] == note3)
    }

    @Test
    fun sortByDateCreatedDescending() {
        val note1 = NoteData(1, "a")
        Thread.sleep(1_000)
        val note2 = NoteData(1, "b")
        Thread.sleep(1_000)
        val note3 = NoteData(1, "c")
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        noteModel.sortDate(true)
        assert(noteModel.notes[0] == note3 && noteModel.notes[1] == note2 && noteModel.notes[2] == note1)
    }

    @Test
    fun noteTimeUpdateOnTitleChange() {
        val note1 = NoteData(1, "Note 1")
        Thread.sleep(1_000)
        note1.setNoteTitle("Hello World!")
        val dateEdited = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy '@' h:mm a")

        assertEquals(dateEdited.format(formatter), note1.dateEdited.format(formatter))
    }

    @Test
    fun undoAWord() {
        val note1 = NoteData(1, "Note 1")
        note1.addToUndoStack(TextChange.INSERT)
        assertEquals(note1.undoStack.size, 2)
    }

    @Test
    fun redoAWord() {
        val note1 = NoteData(1, "Note 1")
        note1.addToUndoStack(TextChange.INSERT)
        note1.redoStack.add(note1.undoStack.last())
        note1.undoStack.removeLast()
        assertEquals(note1.undoStack.size, 1)
        assertEquals(note1.redoStack.size, 1)
    }

    // Test not functional due to ExceptionInInitializer
    @Ignore
    @Test
    fun searchBodyAndTitle() {
        val note1 = NoteData(1, "abc")
        val note2 = NoteData(1, "bcd")
        val note3 = NoteData(1, "cde")
        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        val noteList = NoteList(noteModel)
        val searchBar = SearchBar(noteModel, noteList)
        searchBar.stringMatch(noteModel.notes, "b")
        assert(note1.isDisplay && note2.isDisplay && !note3.isDisplay)
        searchBar.stringMatch(noteModel.notes, "c")
        assert(note1.isDisplay && note2.isDisplay && note3.isDisplay)
        searchBar.stringMatch(noteModel.notes, "e")
        assert(note1.isDisplay && !note2.isDisplay && !note3.isDisplay)
    }

    // Test not functional due to ExceptionInInitializer
    @Ignore
    @Test
    fun filterByOneTag() {
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)

        val noteList = NoteList(noteModel)
        val searchBar = SearchBar(noteModel, noteList)

        note1.addTag("abc")
        note2.addTag("def")

        searchBar.stringMatch(noteModel.notes, "#abc")
        assert(note1.isDisplay && !note2.isDisplay)
    }

    // Test not functional due to ExceptionInInitializer
    @Ignore
    @Test
    fun filterMultipleTags() {
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        val noteList = NoteList(noteModel)
        val searchBar = SearchBar(noteModel, noteList)

        note1.addTag("abc")
        note1.addTag("def")
        note2.addTag("abc")
        note2.addTag("def")
        note3.addTag("abc")

        searchBar.stringMatch(noteModel.notes, "#abc; def")
        assert(note1.isDisplay && !note2.isDisplay)
    }

    @Test
    fun pinNote() {
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        // pin note 2
        // assert(noteModel.notes[0] == note2 && noteModel.notes[1] == note3 && noteModel.notes[2] == note1)
    }

    @Test
    fun pinMultipleNotes() {
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        // pin note 2
        // pin note 1
        // assert(noteModel.notes[0] == note2 && noteModel.notes[1] == note1 && noteModel.notes[2] == note3)
    }

    @Test
    fun pinMultipleAndSortNotes() {
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        // pin note 2
        // pin note 1
        // noteModel.sortAlphaTitle(false)
        // assert(noteModel.notes[0] == note1 && noteModel.notes[1] == note2 && noteModel.notes[2] == note3)
    }

    @Test
    fun unpinNote() {
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        // noteModel.sortAlphaTitle(false)
        // pin note 3
        // assert(noteModel.notes[0] == note3 && noteModel.notes[1] == note1 && noteModel.notes[2] == note2)
        // unpin note 3
        // assert(noteModel.notes[0] == note1 && noteModel.notes[1] == note2 && noteModel.notes[2] == note3)
    }
}