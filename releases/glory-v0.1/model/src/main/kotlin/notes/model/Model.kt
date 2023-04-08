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
        NoteData("Sample Note", "some content")
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