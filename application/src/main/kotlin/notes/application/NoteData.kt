package notes.application

import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList

class NoteData {
    var preview: ObservableList<String> = FXCollections.observableArrayList()
    var listOfNotes: ObservableList<NoteTemplate> = FXCollections.observableArrayList()
    var activeNoteIndex = SimpleIntegerProperty(0)

    fun getActiveIndex(): Int {
        return this.activeNoteIndex.get()
    }

    fun setActiveIndex(num: Int) {
        this.activeNoteIndex.set(num)
    }

    fun setNoteBody(newText: String) {
        this.listOfNotes[activeNoteIndex.get()].noteBody = newText
    }

    fun getNoteBody(): String {
        return this.listOfNotes[activeNoteIndex.get()].noteBody
    }

    fun getNotePreview(): String {
        return this.preview[activeNoteIndex.get()]
    }

    fun setNotePreview() {
        this.preview[activeNoteIndex.get()] = this.getNoteBody()
    }

    init {
        listOfNotes.add(NoteTemplate(""))
        preview.add("New Note")
    }

}