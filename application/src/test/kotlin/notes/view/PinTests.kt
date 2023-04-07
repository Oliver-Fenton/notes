// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.application.Platform
import notes.shared.model.Model
import notes.shared.model.NoteData
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class PinTests {
    @Disabled
    @Test
    fun pinNote() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        val noteList = NoteList(noteModel)
        note2.pin()
        noteList.refreshList(noteModel.notes)
        val noteListNote1 = noteList.children[0] as NoteList.NotePreview
        val noteListNote2 = noteList.children[1] as NoteList.NotePreview
        val noteListNote3 = noteList.children[2] as NoteList.NotePreview

        assert(noteListNote1.noteData.getNoteTitle() == note2.getNoteTitle() && noteListNote2.noteData.getNoteTitle() == note3.getNoteTitle() && noteListNote3.noteData.getNoteTitle() == note1.getNoteTitle())
        Platform.exit()
    }

    @Disabled
    @Test
    fun pinMultipleNotes() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        val noteList = NoteList(noteModel)
        note2.pin()
        note1.pin()
        noteList.refreshList(noteModel.notes)
        val noteListNote1 = noteList.children[0] as NoteList.NotePreview
        val noteListNote2 = noteList.children[1] as NoteList.NotePreview
        val noteListNote3 = noteList.children[2] as NoteList.NotePreview

        assert(noteListNote1.noteData.getNoteTitle() == note2.getNoteTitle() && noteListNote2.noteData.getNoteTitle() == note1.getNoteTitle() && noteListNote3.noteData.getNoteTitle() == note3.getNoteTitle())
        Platform.exit()
    }

    @Disabled
    @Test
    fun pinMultipleAndSortNotes() {
        Platform.startup {}
        val note1 = NoteData(1, "a")
        val note2 = NoteData(2, "b")
        val note3 = NoteData(3, "c")
        val note4 = NoteData(4, "d")

        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        noteModel.notes.add(note4)

        val noteList = NoteList(noteModel)
        note2.pin()
        note1.pin()
        noteList.refreshList(noteModel.notes)
        noteModel.sortAlphaTitle(true)
        noteList.refreshList(noteModel.notes)
        val noteListNote1 = noteList.children[0] as NoteList.NotePreview
        val noteListNote2 = noteList.children[1] as NoteList.NotePreview
        val noteListNote3 = noteList.children[2] as NoteList.NotePreview

        assert(noteListNote1.noteData.getNoteTitle() == note1.getNoteTitle() && noteListNote2.noteData.getNoteTitle() == note2.getNoteTitle() && noteListNote3.noteData.getNoteTitle() == note3.getNoteTitle())
        Platform.exit()
    }

    @Disabled
    @Test
    fun unpinNote() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")
        val note2 = NoteData(2, "Note 2")
        val note3 = NoteData(3, "Note 3")

        val noteModel = Model()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)

        val noteList = NoteList(noteModel)
        noteModel.sortAlphaTitle(true)
        noteList.refreshList(noteModel.notes)
        note3.pin()
        noteList.refreshList(noteModel.notes)
        var noteListNote1 = noteList.children[0] as NoteList.NotePreview
        var noteListNote2 = noteList.children[1] as NoteList.NotePreview
        var noteListNote3 = noteList.children[2] as NoteList.NotePreview
        assert(noteListNote1.noteData.getNoteTitle() == note3.getNoteTitle() && noteListNote2.noteData.getNoteTitle() == note1.getNoteTitle() && noteListNote3.noteData.getNoteTitle() == note2.getNoteTitle())
        note3.removePin()
        noteList.refreshList(noteModel.notes)
        noteListNote1 = noteList.children[0] as NoteList.NotePreview
        noteListNote2 = noteList.children[1] as NoteList.NotePreview
        noteListNote3 = noteList.children[2] as NoteList.NotePreview
        assert(noteListNote1.noteData.getNoteTitle() == note1.getNoteTitle() && noteListNote2.noteData.getNoteTitle() == note2.getNoteTitle() && noteListNote3.noteData.getNoteTitle() == note3.getNoteTitle())
        Platform.exit()
    }
}