// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import notes.shared.model.Model
import notes.shared.model.NoteData
import org.junit.jupiter.api.Test

class SortTest {
    @Test
    fun sortByTitleAtoZ() {
        val note1 = NoteData(1, "a")
        val note2 = NoteData(2, "b")
        val note3 = NoteData(3, "c")
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
        val note2 = NoteData(2, "b")
        val note3 = NoteData(3, "c")
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
        val note2 = NoteData(2, "b")
        val note3 = NoteData(3, "c")
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
        val note2 = NoteData(2, "b")
        val note3 = NoteData(3, "c")
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
}