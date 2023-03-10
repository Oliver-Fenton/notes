package notes.shared.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import notes.shared.database.NoteDatabase

class Model {
    val noteDatabase = NoteDatabase()
    var isSplitView = SimpleBooleanProperty( true )
    val activeNote = SimpleObjectProperty<NoteData?>(null)
    var notes: ObservableList<NoteData> = FXCollections.observableArrayList()
    private var idCounter: Int = 0

    /*
     * load notes from database
     */
    init {
        val noteList = noteDatabase.getNotes()
        for ( note in noteList ) {
            notes.add( note )
        }
        idCounter = noteDatabase.getMaxId()
    }

    fun setActiveNote( note: NoteData? ) {
        // save changes to old note in database
        if ( activeNote.value != null ) noteDatabase.updateNote( activeNote.value!! )

        // deactivate old note
        activeNote.value?.setInactive()

        // activate new note
        activeNote.set( note )
        note?.setActive()
    }

    fun createNote() {
        idCounter++
        val newNote = NoteData(idCounter, "New Note $idCounter")
        notes.add( newNote )
        setActiveNote( newNote )

        // save new note to database
        noteDatabase.insertNote( newNote )
    }

    fun deleteNote() {
        val currIndex = notes.indexOf(activeNote.value)
        println(currIndex)
        notes.remove(activeNote.value)

        activeNote.value?.let { noteDatabase.deleteNote(it) }
        if (activeNote.value != null && notes.size > 1) {
           // setActiveNote(notes[currIndex])
            setActiveNote(notes[0])
        }

    }

    fun saveWindowPosition(x: Double, y: Double, width: Double, height: Double) {
        noteDatabase.saveWindowPosition( x, y, width, height )
        activeNote.value?.let { noteDatabase.updateNote(it) } // update last note on exit
    }

    fun getWindowPosition(): Pair< Pair<Double,Double>, Pair<Double,Double> > {
        return noteDatabase.getWindowPosition()
    }
}