package notes.shared.model

import org.json.JSONObject
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class ModelTest {

    @Disabled
    @Test
    fun noteToJson() {
        val note = NoteData(999, "MODEL TEST NOTE", "test test test", "2023-03-20T10:30:15.123456", "2023-03-20T10:30:15.123456", "")
        println("expected: {\"id\":999,\"title\":\"MODEL TEST NOTE\",\"body\":\"test test test\",\"dateCreated\":\"2023-03-20T10:30:15.123456\",\"dateEdited\":\"2023-03-20T10:30:15.123456\"}")
        println("actual: ${note.toJson()}")
    }

    @Disabled
    @Test
    fun jsonToNote() {
        val model = Model()

        val json = JSONObject()
        json.put("id", 999)
        json.put("title", "MODEL TEST NOTE")
        json.put("body", "test test test")
        json.put("dateCreated", "2023-03-20T10:30:15.123456")
        json.put("dateEdited", "2023-03-20T10:30:15.123456")

        val note = model.jsonToNote(json.toString())
        println("expected: ${json.toString()}")
        println("actual: ${note.toJson()}")
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