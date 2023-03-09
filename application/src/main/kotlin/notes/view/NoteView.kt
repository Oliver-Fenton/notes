package notes.view


import javafx.scene.layout.StackPane
import javafx.scene.web.HTMLEditor
import notes.shared.model.Model
import notes.shared.model.NoteData


class NoteView(noteModel: Model): StackPane() {
    val notesArea = HTMLEditor()

    fun setTextArea(newText: String) {
        this.notesArea.htmlText = newText
    }

    fun clearTextArea() { //notesArea.clear()
        notesArea.htmlText = "" }

    fun displayNote(noteData: NoteData) {
        clearTextArea()
        setTextArea( noteData.getHTML() )
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

        notesArea.setOnKeyReleased { e ->
            // noteModel.activeNote.value?.setBody(this.notesArea.htmlText + e.text)
            noteModel.activeNote.value?.setBody(this.notesArea.htmlText)
            // println(this.notesArea.htmlText)
            println("activeNoteDataKeyReleased: ${noteModel.activeNote.value?.getHTML()}")
        }
        notesArea.setOnMouseClicked{ e ->
            noteModel.activeNote.value?.setBody(this.notesArea.htmlText)
            println("activeNoteDataMousePressed: ${noteModel.activeNote.value?.getHTML()}")
        }


        children.add(notesArea)

       // modifiedHTMLEditorToolbar(notesArea)
    }
}