package notes.view


import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.web.HTMLEditor
import notes.shared.model.Model
import notes.shared.model.NoteData
import notes.shared.model.TextChange


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
            if (e.code.isLetterKey || e.code == KeyCode.SPACE || e.code.isDigitKey) {
                noteModel.activeNote.value?.emptyRedo()
                noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
            }
            noteModel.activeNote.value?.setBody(this.notesArea.htmlText)
            println("activeNoteData: ${noteModel.activeNote.value?.getHTML()}")
        }

        children.add(notesArea)

       // modifiedHTMLEditorToolbar(notesArea)
    }
}