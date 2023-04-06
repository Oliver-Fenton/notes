// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.application.Platform
import notes.shared.model.Model
import notes.shared.model.NoteData
import org.junit.jupiter.api.Test

class TagsTest {
    @Test
    fun addTag() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")

        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)

        note1.addTag("abc")
        assert(note1.tags.contains("abc"))
        Platform.exit()
    }

    @Test
    fun addMultipleTags() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")

        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)

        note1.addTag("abc")
        assert(note1.tags.contains("abc"))
        note1.addTag("def")
        assert(note1.tags.contains("abc") && note1.tags.contains("def"))
        Platform.exit()
    }

    @Test
    fun deleteTag() {
        Platform.startup {}
        val note1 = NoteData(1, "Note 1")

        val noteModel = Model()
        noteModel.notes.clear()
        noteModel.notes.add(note1)

        note1.addTag("abc")
        assert(note1.tags.contains("abc"))
        note1.addTag("def")
        assert(note1.tags.contains("abc") && note1.tags.contains("def"))
        note1.removeTag("abc")
        assert(!note1.tags.contains("abc") && note1.tags.contains("def"))
        Platform.exit()
    }

    @Test
    fun filterByOneTag() {
        Platform.startup {}
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
        Platform.exit()
    }

    @Test
    fun filterMultipleTags() {
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
        val searchBar = SearchBar(noteModel, noteList)

        note1.addTag("abc")
        note1.addTag("def")
        note2.addTag("abc")
        note2.addTag("def")
        note3.addTag("ghi")

        searchBar.stringMatch(noteModel.notes, "#abc; def")
        assert(note1.isDisplay && note2.isDisplay && !note3.isDisplay)
        Platform.exit()
    }

    @Test
    fun filterMultipleTags2() {
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
        val searchBar = SearchBar(noteModel, noteList)

        note1.addTag("abc")
        note2.addTag("def")
        note3.addTag("ghi")

        searchBar.stringMatch(noteModel.notes, "#abc; def")
        assert(note1.isDisplay && note2.isDisplay && !note3.isDisplay)
        Platform.exit()
    }

    @Test
    fun filterMultipleTagsReverseOrder() {
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
        val searchBar = SearchBar(noteModel, noteList)

        note1.addTag("abc")
        note1.addTag("def")
        note2.addTag("def")
        note2.addTag("abc")
        note3.addTag("ghi")

        searchBar.stringMatch(noteModel.notes, "#def; abc")
        assert(note1.isDisplay && note2.isDisplay && !note3.isDisplay)
        Platform.exit()
    }
}