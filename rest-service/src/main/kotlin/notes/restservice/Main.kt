package notes.restservice

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

    @GetMapping("/{id}")
    fun getNote(@PathVariable id: Long): Note? {
        return service.getNote(id)
    }

    @PutMapping("/{id}")
    fun updateNote(@PathVariable id: Long, @RequestBody note: Note) {
        require(id == note.id)
        service.updateNote(id, note)
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: Long) = service.deleteNote(id)
}

@Service
class NoteService {
    var noteMap: MutableMap<Long, Note> = mutableMapOf()

    fun getAllNotes() = noteMap.values.toList()

    fun createNote(note: Note) {
        noteMap[note.id] = note
    }

    fun getNote(id: Long): Note? {
        return noteMap[id]
    }

    fun updateNote(id: Long, note: Note) {
        require(id == note.id)
        noteMap[id] = note
    }

    fun deleteNote(id: Long) {
        noteMap.remove(id)
    }
}
/*
@Service
class NoteDatabaseService( @Autowired val db: NoteRepository ) {
    fun getAllNotes(): List<Note> = db.findAll().toList()

    fun createNote(note: Note): Note { return db.save(note) }

    fun getNote(id: Long): Note? { return db.findById(id).orElse(null) }

    fun updateNote(id: Long, note: Note): Note {
        require(id == note.id)
        return db.save(note)
    }

    fun deleteNote(id: Long) { db.deleteById(id) }
}

@Repository
interface NoteRepository : CrudRepository<Note, Long> {
    // Define any additional methods here if needed
}
*/

data class Note(
    var id: Long,
    var title: String,
    var body: String,
    var dateCreated: LocalDateTime,
    var dateEdited: LocalDateTime
)