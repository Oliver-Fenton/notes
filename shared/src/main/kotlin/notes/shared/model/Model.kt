package notes.shared.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.ToolBar
import javafx.scene.web.HTMLEditor
import notes.shared.Constants
import notes.shared.database.NoteDatabase
import notes.shared.preferences.Preferences

class Model {
    val noteDatabase = NoteDatabase()
    var isSplitView = SimpleBooleanProperty( true )
    val activeNote = SimpleObjectProperty<NoteData?>(null)
    var notes: ObservableList<NoteData> = FXCollections.observableArrayList()
    var allTags: Array<String> = arrayOf<String>()

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
        if ( activeNote.value != null ) {
            noteDatabase.updateNote( activeNote.value!! )
        }

        // deactivate old note
        activeNote.value?.setInactive()

        // activate new note
        activeNote.set( note )
        note?.setActive()

        activeNote.value?.setDateHTMLEditor()
        activeNote.value?.setTitleHTMLEditor()
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
        activeNote.value?.let {
            println("Deleting active note titled '${it.title}'")
            var curIndex = notes.indexOf( it.value )
            if ( curIndex > 0 ) curIndex -= 1
            notes.remove( it )
            noteDatabase.deleteNote( it )
            if ( notes.isNotEmpty() ) { setActiveNote( notes[curIndex] ) }
            else {
                activeNote.value?.clearTitleAndDateHTMLEditor()
                setActiveNote( null )
               // activeNote.value?.clearTitleAndDateHTMLEditor()
                // no active notes left, so clear the title and date visible on htmleditor
            }
        }
    }

    fun saveWindowPosition(x: Double, y: Double, width: Double, height: Double, dividerPos: Double, isListCollapsed: Boolean ) {
        noteDatabase.saveWindowPosition( x, y, width, height, dividerPos, isListCollapsed )
    }

    fun getWindowPosition(): Preferences {
        return noteDatabase.getWindowPosition()
    }

    fun saveNoteToDatabase( note: NoteData ) {
        noteDatabase.updateNote( note )
    }


    fun sortAlpha(reverseOrder: Boolean) {
        println("Sort Alphabetically")
        var sortedNotes = notes.sortedWith(compareBy{ it.getText() })
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()
        }
        notes.clear()
        sortedNotes.forEach {element ->
            notes.add(element)
        }
    }

    fun sortDate(reverseOrder: Boolean) {
        println("Sort Date")
        var sortedNotes = notes.sortedWith(compareBy{ it.getDateCreated()})
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()
        }
        notes.clear()
        sortedNotes.forEach {element ->
            notes.add(element)
        }
    }

    fun sortDateEdited(reverseOrder: Boolean) {
        println("Sort Date Edited")
        var sortedNotes = notes.sortedWith(compareBy{ it.getDateEdited()})
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()
        }
        notes.clear()
        sortedNotes.forEach {element ->
            notes.add(element)
        }
    }

    fun sortAlphaTitle(reverseOrder: Boolean) {
        println("Sort Note Title")
        var sortedNotes = notes.sortedWith(compareBy{ it.getNoteTitle()})
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()
        }
        notes.clear()
        sortedNotes.forEach {element ->
            notes.add(element)
        }
    }

    fun trimTags(currTag: String) {
        for (n in notes) {
            if (!n.getDisplay()) {
                continue
            }else if (this.allTags.contains(currTag)) {
                n.doDisplay()
            } else {
                n.notDisplay()
            }
        }
    }
}