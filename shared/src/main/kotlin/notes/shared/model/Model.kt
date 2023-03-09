package notes.shared.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class Model {
    var isSplitView = SimpleBooleanProperty( true )
    val activeNote = SimpleObjectProperty<NoteData?>(null)
    var notes: ObservableList<NoteData> = FXCollections.observableArrayList(
        NoteData("note #1", "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>"),
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