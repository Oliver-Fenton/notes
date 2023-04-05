// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.model

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ModelTest {

    @Disabled
    @Test
    fun noteToJson() {
        val note = NoteData( 1, "Test Note").apply {
            body = "<html><body>This is a test note</body></html>"
            dateCreated = LocalDateTime.now()
            dateEdited = LocalDateTime.now()
        }
        note.tags.addAll(listOf("tag1", "tag2", "tag3"))

        println(note.toJson())
    }

    @Disabled
    @Test
    fun jsonToNote() {
        val model = Model()

        val note = NoteData( 1, "Test Note").apply {
            body = "<html><body>This is a test note</body></html>"
            dateCreated = LocalDateTime.now()
            dateEdited = LocalDateTime.now()
        }
        note.tags.addAll(listOf("tag1", "tag2", "tag3"))

        val noteFromJson = model.jsonToNote(note.toJson())

        println("expected: ${note.toJson()}")
        println("actual: ${noteFromJson.toJson()}")
    }

    @Disabled
    @Test
    fun getNoteFromWebService() {
        val model = Model()

        val note = model.getNoteFromWebService(10)
        println(note.toJson())
    }

    @Disabled
    @Test
    fun getNotesFromWebService() {
        val model = Model()

        val notes = model.getNotesFromWebService()
        for (note in notes) println(note.toJson())
    }

    @Disabled
    @Test
    fun postNoteToWebService() {
        val model = Model()

        val note = NoteData(10, "note 10", "10 10 10 10", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "")
        model.postNoteToWebService(note)
        println(model.getNotesFromWebService())
    }

    @Disabled
    @Test
    fun putNoteToWebService() {
        val model = Model()

        val note = NoteData(10, "NEW note 10", "new stuff", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "")
        model.putNoteToWebService(note)
        println(model.getNotesFromWebService())
    }

    @Disabled
    @Test
    fun deleteNoteFromWebService() {
        val model = Model()

        println(model.getNoteFromWebService(10))
        model.deleteNoteFromWebService(10)
        println(model.getNoteFromWebService(10))
    }
}