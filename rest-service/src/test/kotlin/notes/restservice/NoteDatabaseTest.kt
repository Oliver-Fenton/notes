// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.restservice

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import java.time.LocalDateTime

class NoteDatabaseTest {

    @Disabled
    @Test
    fun getNotes() {
        val db = NoteDatabase()
        println(db.getNotes())
    }

    @Disabled
    @Test
    fun insertNote() {
        val db = NoteDatabase()
        val json = JsonObject()
        json.addProperty("id", 12)
        json.addProperty("title", "REST-SERVICE DB TEST NOTE")
        json.addProperty("body", "test test test")
        json.addProperty("dateCreated", "2023-03-20T10:30:15.123456")
        json.addProperty("dateEdited", "2023-03-20T10:30:15.123456")
        val tags = JsonArray()
        tags.add("tag1")
        json.add("tags", tags)

        db.insertNote(json)
        println(db.getNotes())
    }

    @Disabled
    @Test
    fun updateNote() {
        val db = NoteDatabase()
        val json = JsonObject()
        json.addProperty("id", 10)
        json.addProperty("title", "UPDATED REST-SERVICE DB TEST NOTE")
        json.addProperty("body", "update update update")
        json.addProperty("dateCreated", "2023-03-20T10:30:15.123456")
        json.addProperty("dateEdited", "2023-03-20T10:30:15.123456")
        val tags = JsonArray()
        tags.add("new tag")
        json.add("tags", tags)
        db.updateNote(10, json)
        println(db.getNotes())
    }

    @Disabled
    @Test
    fun deleteNote() {
        val db = NoteDatabase()
        db.deleteNote(10)
        println(db.getNotes())
    }
}