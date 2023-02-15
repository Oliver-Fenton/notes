package notes.model

import javafx.beans.InvalidationListener
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class Model {
    /**
     * SimpleObjectValue notifies all listeners when its stored value is set. (This is different from a [SimpleObjectProperty] that only notifies al listeners when its stored value has changed.)
     *
     *                      *** FROM CS 349 CONNECTFOUR MODEL ***
     *
     */
    class SimpleObjectValue<T>(initialValue: T) : ObservableObjectValue<T> {
        private var value = initialValue
        private val invalidationListeners = mutableListOf<InvalidationListener?>()
        private val changeListeners = mutableListOf<ChangeListener<in T>?>()
        override fun addListener(listener: InvalidationListener?) { invalidationListeners.add(listener) }
        override fun addListener(listener: ChangeListener<in T>?) { changeListeners.add(listener) }
        override fun removeListener(listener: InvalidationListener?) { invalidationListeners.remove(listener) }
        override fun removeListener(listener: ChangeListener<in T>?) { changeListeners.remove(listener) }
        override fun getValue(): T { return value }
        override fun get(): T { return value }
        fun set(value: T) {
            invalidationListeners.forEach { it?.invalidated(this) }
            changeListeners.forEach { it?.changed(this, this.value, value) }
            this.value = value
        }
    }
    val activeNote = SimpleObjectValue<NoteData?>(null)
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