package notes.view


import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.ToolBar
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import javafx.scene.web.HTMLEditor
import notes.shared.Constants
import notes.shared.model.Model
import notes.shared.model.NoteData
import notes.shared.model.TextChange


class NoteView(noteModel: Model): StackPane() {


    fun setTextArea(newText: String) {
        Constants.notesArea.htmlText = newText
    }

    fun clearTextArea() {
        Constants.notesArea.htmlText = "" }

    fun displayNote(noteData: NoteData) {
        clearTextArea()
        setTextArea( noteData.getHTML() )
    }



    init {

        noteModel.activeNote.addListener { _, _, newActiveNote ->
            if (newActiveNote != null) displayNote( newActiveNote )
            else clearTextArea()
        }

        Constants.notesArea.setOnKeyReleased { e ->
            if (e.code.isLetterKey || e.code == KeyCode.SPACE || e.code.isDigitKey) {
                noteModel.activeNote.value?.emptyRedo()
                noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
            }
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
            println("activeNoteData: ${noteModel.activeNote.value?.getHTML()}")
        }

        children.add(Constants.notesArea)
    }
}