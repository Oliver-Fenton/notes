package notes.model

import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class Model {
    var isSplitView = SimpleBooleanProperty( true )
    val activeNote = SimpleObjectProperty<NoteData?>(null)
    var notes: ObservableList<NoteData> = FXCollections.observableArrayList(
        NoteData("note #1", "blah blah blah"),
        NoteData("note #2", "blah blah blah, blah blah blah"),
        NoteData("note #3", "blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #4", "blah blah blah, blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #5", "blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #6", "blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #7", "blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #8", "blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #9", "blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah"),
        NoteData("note #10", "blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah, blah blah blah")
    )

    fun setActiveNote(noteData: NoteData?) {
        activeNote.value?.setInactive()
        activeNote.set( noteData )
        noteData?.setActive()
    }

    fun createNote() {
        val newNote = NoteData("New Note")
        notes.add( newNote )
        setActiveNote( newNote )
    }
}