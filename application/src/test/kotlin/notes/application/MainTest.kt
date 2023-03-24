package notes.application

import notes.shared.model.Model
import notes.shared.model.NoteData
import notes.shared.model.TextChange
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

//    @Test
//    fun addNotes() {
//        var model = Model()
//        assertEquals(model.notes.size, model.notes.size)
//        val tempSize = model.notes.size
//        for (i in 0..2) {
//            model.createNote()
//            // Check that the note body is blank
//            assertEquals(model.notes[i + tempSize].getHTML(), "")
//            // Check that the note is active
//            assertEquals(model.notes[i + tempSize].isActive, true)
//        }
//        // Check that we have added exactly 101 new notes
//        assertEquals(model.notes.size, tempSize + 3)
//    }

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

//    @Test
//    fun noteTimeUpdateOnTitleChange() {
//        val note1 = NoteData(1, "Note 1")
//        Thread.sleep(1_000)
//        note1.setNoteTitle("Hello World!")
//        val dateEdited = LocalDateTime.now()
//        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS")
//        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy '@' h:mm a")
//
//        assertEquals(dateEdited.format(formatter), note1.dateEdited.format(formatter))
//    }
//
//    @Test
//    fun noteListTimeUpdateOnTitleChange() {
//        val note1 = NoteData(1, "Note 1")
//        Thread.sleep(1_000)
//        val noteModel = Model()
//        note1.setNoteTitle("Hello World!")
//        val dateEdited = LocalDateTime.now()
//        val noteList = NoteList(noteModel)
//        //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:MM:SS")
//        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy '@' h:mm a")

//        assert(noteList.NotePreview(note1).date.text.contains(dateEdited.format(formatter)))
//    }
//
//    @Test
//    fun noteListTitleUpdateOnTitleChange() {
//        val note1 = NoteData(1, "Note 1")
//        val noteModel = Model()
//        noteModel.setActiveNote(note1)
//        val noteList = NoteList(noteModel)
//        note1.setNoteTitle("Hello World!")
//        assertEquals(noteList.NotePreview(note1).title.text, noteModel.activeNote.value?.getNoteTitle())
//    }
//
//    @Test
//    fun createNewNoteDefaultTitle() {
//        val note1 = NoteData(1, "")
//        val noteModel = Model()
//        noteModel.setActiveNote(note1)
//        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar
//        val title = toolBar2.lookup(".text-field") as TextField
//        assert(title.text.contains("New Note"))
//    }
//
//    @Test
//    fun deleteNoteTitleDefaultNoteListTitle() {
//        val note1 = NoteData(1, "Note 1")
//        val noteModel = Model()
//        noteModel.setActiveNote(note1)
//        val noteList = NoteList(noteModel)
//        note1.setNoteTitle("")
//        assert(noteList.NotePreview(note1).title.text.contains("New Note"))
//    }
//
//    @Test
//    fun noteListTitleMatchesNoteEditorTitle() {
//        val note1 = NoteData(1, "Note 1")
//        val noteModel = Model()
//        noteModel.setActiveNote(note1)
//        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar
//        val date = toolBar2.lookup(".label") as Label
//        val title = toolBar2.lookup(".text-field") as TextField
//        assertEquals(note1.getNoteTitle(), title.text)
//        assertEquals(note1.getDateEdited(), date.text)
//    }
//
//    @Test
//    fun undoAWord() {
//        val note1 = NoteData(1, "Note 1")
//        note1.addToUndoStack(TextChange.INSERT)
//        assertEquals(note1.undoStack.size, 1)
//    }
//
//    @Test
//    fun redoAWord() {
//        val note1 = NoteData(1, "Note 1")
//        note1.addToUndoStack(TextChange.INSERT)
//        note1.undo()
//        assertEquals(note1.undoStack.size, 0)
//        assertEquals(note1.redoStack.size, 1)
//    }
//
//    @Test
//    fun disableRedoAfterTextInsert() {
//        val note1 = NoteData(1, "Note 1")
//        note1.addToUndoStack(TextChange.INSERT)
//        note1.undo()
//        assertEquals(note1.redoStack.size, 1)
//        note1.addToUndoStack(TextChange.INSERT)
//        assertEquals(note1.redoStack.size, 0)
//    }
//
//    @Test
//    fun searchBodyAndTitle() {
//        val note1 = NoteData(1, "abc")
//        val note2 = NoteData(1, "bcd")
//        val note3 = NoteData(1, "cde")
//        val noteModel = Model()
//        noteModel.notes.add(note1)
//        noteModel.notes.add(note2)
//        noteModel.notes.add(note3)
//        val noteList = NoteList(noteModel)
//        val searchBar = SearchBar(noteModel, noteList)
//        searchBar.stringMatch(noteModel.notes, "b")
//        assert(note1.isDisplay && note2.isDisplay && !note3.isDisplay)
//        searchBar.stringMatch(noteModel.notes, "c")
//        assert(note1.isDisplay && note2.isDisplay && note3.isDisplay)
//        searchBar.stringMatch(noteModel.notes, "e")
//        assert(note1.isDisplay && !note2.isDisplay && !note3.isDisplay)
//    }
}