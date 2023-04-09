// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view


import javafx.scene.control.TextField
import javafx.scene.control.ToolBar
import javafx.scene.input.KeyCode
import javafx.scene.layout.StackPane
import notes.shared.Constants
import notes.shared.model.Model
import notes.shared.model.NoteData
import notes.shared.model.TextChange


class NoteView(noteModel: Model): StackPane() {


    fun setTextArea(newText: String) {
        Constants.notesArea.htmlText = newText
    }

    fun clearTextArea() { //notesArea.clear()
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
            if (e.code == KeyCode.COMMAND) {
                Constants.notesArea.isDisable = false
                e.consume()
                return@setOnKeyReleased
            }
            if (e.isMetaDown && e.code == KeyCode.D) {
                Constants.notesArea.isDisable = false
                e.consume()
                return@setOnKeyReleased
            }
            val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar
            val title = toolBar2.lookup(".text-field") as TextField
            if (noteModel.notes.size == 0 && title.text == "") {
                val currentNotesAreaHtml = Constants.notesArea.htmlText
                noteModel.createNote()
                noteModel.activeNote.value?.setNoteBody(currentNotesAreaHtml)
                setTextArea(currentNotesAreaHtml)
            }
            else {
                if (e.code.isLetterKey || e.code == KeyCode.SPACE || e.code.isDigitKey) {
                    noteModel.activeNote.value?.emptyRedo()
                    noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT, e.code)
                }
                noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
                if (e.code == KeyCode.SPACE) {
                    noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
                }
            }
        }

        children.add(Constants.notesArea)
    }
}