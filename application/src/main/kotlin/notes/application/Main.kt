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
        stage.scene = Scene(noteView, 250.0, 150.0)
        stage.isResizable = true
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.isResizable = true
        stage.title = "Notes"
        stage.show()

        // Modify the toolbar to include only necessary items (can only be done after at least one layout pass)
        noteView.modifiedHTMLEditorToolbar(noteView.noteView.notesArea)
    }



}