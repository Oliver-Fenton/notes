// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.restservice

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@SpringBootApplication
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}

@RestController
@RequestMapping("/notes")
class NoteController(private val service: NoteService) {

    @GetMapping()
    fun getAllNotes(): List<Note> {
        return service.getAllNotes()
    }

    @PostMapping()
    fun createNote(@RequestBody note: Note) {
        service.createNote(note)
    }

    @PutMapping("/{id}")
    fun updateNote(@PathVariable id: Int, @RequestBody note: Note) {
        require(id == note.id)
        service.updateNote(id, note)
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: Int) {
        println("deleting note with id '$id'")
        service.deleteNote(id)
    }
}

@Service
class NoteService {
    val db = NoteDatabase()

    fun getAllNotes(): List<Note> {
        val noteList = mutableListOf<Note>()
        val jsonArray = db.getNotes()
        val gson = Gson()

        for ( i in 0 until jsonArray.size() ) {
            val noteJson = jsonArray.get(i).asJsonObject
            val note = gson.fromJson(noteJson, Note::class.java)
            noteList.add(note)
        }

        return noteList
    }

    fun createNote(note: Note) {
        val gson = Gson()
        val jsonString = gson.toJson(note)
        val jsonObject: JsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        db.insertNote(jsonObject)
    }

    fun updateNote(id: Int, note: Note) {
        require(id == note.id)

        val gson = Gson()
        val jsonString = gson.toJson(note)
        val jsonObject: JsonObject = gson.fromJson(jsonString, JsonObject::class.java)
        db.updateNote(id, jsonObject)
    }

    fun deleteNote(id: Int) {
        db.deleteNote(id)
    }
}

data class Note(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("body") var body: String,
    @SerializedName("dateCreated") var dateCreated: String,
    @SerializedName("dateEdited") var dateEdited: String
)