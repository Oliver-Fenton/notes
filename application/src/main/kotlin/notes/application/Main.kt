package notes.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import notes.shared.model.Model
import notes.view.View

class Main : Application() {
    private val noteModel = Model()
    private val noteView = View( noteModel )


    override fun start(stage: Stage) {
        val windowPos = noteModel.getWindowPosition()
        println("window position fetched from db: $windowPos")
        stage.scene = Scene(noteView, 250.0, 150.0)
        stage.isResizable = true
        stage.x = windowPos.first.first
        stage.y = windowPos.first.second
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.width = windowPos.second.first
        stage.height = windowPos.second.second
        stage.isResizable = true
        stage.title = "Notes"

        stage.setOnCloseRequest { _ ->
            noteModel.saveWindowPosition( stage.x, stage.y, stage.width, stage.height )
            noteModel.saveNoteToDatabase( noteModel.activeNote.value!! )
        }

        stage.show()

        // Modify the toolbar to include only necessary items (can only be done after at least one layout pass)
        noteView.modifiedHTMLEditorToolbar(noteView.noteView.notesArea)
    }



}