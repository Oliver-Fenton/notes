// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.application.Platform
import notes.shared.model.Model
import notes.shared.model.NoteData
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

// Some tests are disabled as they only run in intelliJ due to the Platform.startup command
class SearchTest {
    @Disabled
    @Test
    fun searchTitle() {
        Platform.startup {}
        val note1 = NoteData(1, "abc")
        val note2 = NoteData(1, "bcd")
        val note3 = NoteData(1, "cde")
        val noteModel = Model()
        noteModel.notes.clear()
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
        assert(!note1.isDisplay && !note2.isDisplay && note3.isDisplay)
        Platform.exit()
    }

    @Disabled
    @Test
    fun searchBody() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")
        note1.body = "abc"
        val note2 = NoteData(1, "Note 2")
        note2.body = "bcd"
        val note3 = NoteData(1, "Note 3")
        note3.body = "cdf"
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        val noteList = NoteList(noteModel)
        val searchBar = SearchBar(noteModel, noteList)
        searchBar.stringMatch(noteModel.notes, "b")
        assert(note1.isDisplay && note2.isDisplay && !note3.isDisplay)
        searchBar.stringMatch(noteModel.notes, "c")
        assert(note1.isDisplay && note2.isDisplay && note3.isDisplay)
        searchBar.stringMatch(noteModel.notes, "f")
        assert(!note1.isDisplay && !note2.isDisplay && note3.isDisplay)
        Platform.exit()
    }

    @Disabled
    @Test
    fun searchBodyAndTitle() {
        Platform.startup {}
        val note1 = NoteData(1, "abc")
        note1.body = "note"
        val note2 = NoteData(1, "Note 2")
        note2.body = "bcd"
        val note3 = NoteData(1, "Note 3")
        note3.body = "cdf"
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        val noteList = NoteList(noteModel)
        val searchBar = SearchBar(noteModel, noteList)
        searchBar.stringMatch(noteModel.notes, "note")
        assert(note1.isDisplay && note2.isDisplay && note3.isDisplay)
        Platform.exit()
    }

    @Disabled
    @Test
    fun searchAndNothingAppears() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")
        note1.body = "abc"
        val note2 = NoteData(1, "Note 2")
        note2.body = "bcd"
        val note3 = NoteData(1, "Note 3")
        note3.body = "cde"
        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)
        noteModel.notes.add(note2)
        noteModel.notes.add(note3)
        val noteList = NoteList(noteModel)
        val searchBar = SearchBar(noteModel, noteList)
        searchBar.stringMatch(noteModel.notes, "k")
        assert(!note1.isDisplay && !note2.isDisplay && !note3.isDisplay)
        Platform.exit()
    }
}