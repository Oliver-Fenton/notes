package notes.view


import javafx.scene.layout.StackPane

import javafx.scene.web.HTMLEditor
import notes.model.Model
import notes.model.NoteData


class NoteView(noteModel: Model): StackPane() {
    val notesArea = HTMLEditor()

    fun setTextArea(newText: String) {
        this.notesArea.htmlText = newText
    }

    fun clearTextArea() { //notesArea.clear()
        notesArea.htmlText = "" }

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

        notesArea.setOnKeyPressed { e ->
            noteModel.activeNote.value?.setBody(this.notesArea.htmlText + e.text)
            println("activeNoteData: ${noteModel.activeNote.value?.getBody()}")
        }

        children.add(notesArea)

       // modifiedHTMLEditorToolbar(notesArea)
    }
}