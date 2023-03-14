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
        val newNote = NoteData(idCounter, "New Note #$idCounter")
        notes.add( newNote )
        setActiveNote( newNote )

        // save new note to database
        noteDatabase.insertNote( newNote )
    }

    fun deleteNote() {
        if ( activeNote.value != null ) {
            println("Deleting active note titled '${activeNote.value!!.title}'")
            var curIndex = notes.indexOf( activeNote.value!! )
            if ( curIndex > 0 ) curIndex -= 1
            notes.remove( activeNote.value!! )
            setActiveNote( notes[curIndex] )
        }
    }

    fun saveWindowPosition(x: Double, y: Double, width: Double, height: Double) {
        noteDatabase.saveWindowPosition( x, y, width, height )
    }

    fun getWindowPosition(): Pair< Pair<Double,Double>, Pair<Double,Double> > {
        return noteDatabase.getWindowPosition()
    }

    fun saveNoteToDatabase( note: NoteData ) {
        noteDatabase.updateNote( note )
    }
}