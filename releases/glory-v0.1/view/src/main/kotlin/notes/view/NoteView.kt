package notes.view

import javafx.scene.control.TextArea
import javafx.scene.layout.StackPane
import notes.model.Model
import notes.model.NoteData

class NoteView(noteModel: Model): StackPane() {
    private val notesArea = TextArea()

    fun setTextArea(newText: String) {
        this.notesArea.text = newText
    }

    fun clearTextArea() { notesArea.clear() }

    fun displayNote(noteData: NoteData) {
        clearTextArea()
        setTextArea( noteData.getBody() )
    }

    init {
        /*
        for (note in noteModel.getNotes()) {
            note.isActive.addListener(ChangeListener { _, _, newValue ->
                if ( newValue == true ) {
                    displayNote( note )
                }
            })
        }
        */
        noteModel.activeNote.addListener { _, _, newActiveNote ->
            if (newActiveNote != null) displayNote( newActiveNote )
            else clearTextArea()
        }

        notesArea.setOnKeyPressed { e, ->
            noteModel.activeNote.value?.setBody(this.notesArea.text + e.text)
            println("activeNoteData: ${noteModel.activeNote.value?.getBody()}")
        }

        children.add(notesArea)
    }
}