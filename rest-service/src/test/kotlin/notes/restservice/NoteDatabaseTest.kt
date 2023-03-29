package notes.restservice

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.LocalDateTime

class NoteDatabaseTest {

    @Test
    fun getNotes() {
        val db = NoteDatabase()
        println(db.getNotes())
    }

    @Test
    fun insertNote() {
        val db = NoteDatabase()
        val json = JsonObject()
        json.addProperty("id", 12)
        json.addProperty("title", "REST-SERVICE DB TEST NOTE")
        json.addProperty("body", "test test test")
        json.addProperty("dateCreated", "2023-03-20T10:30:15.123456")
        json.addProperty("dateEdited", "2023-03-20T10:30:15.123456")
        db.insertNote(json)
        println(db.getNotes())
    }

    @Test
    fun updateNote() {
        val db = NoteDatabase()
        val json = JsonObject()
        json.addProperty("id", 10)
        json.addProperty("title", "UPDATED REST-SERVICE DB TEST NOTE")
        json.addProperty("body", "update update update")
        json.addProperty("dateCreated", "2023-03-20T10:30:15.123456")
        json.addProperty("dateEdited", "2023-03-20T10:30:15.123456")
        db.updateNote(10, json)
        println(db.getNotes())
    }

    @Test
    fun deleteNote() {
        val db = NoteDatabase()
        db.deleteNote(10)
        println(db.getNotes())
    }
}