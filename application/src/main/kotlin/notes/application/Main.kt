// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

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
        val preferences = noteModel.getPreferences()

        stage.scene = Scene(noteView, 250.0, 150.0)
        stage.isResizable = true
        stage.x = preferences.x
        stage.y = preferences.y
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.width = preferences.width
        stage.height = preferences.height
        stage.isResizable = true
        stage.title = "Notes"

        stage.setOnCloseRequest { _ ->
            noteModel.savePreferences( stage.x, stage.y, stage.width, stage.height, noteView.getDividerPos(), noteView.isListCollapsed(), noteView.getTheme() )
            noteModel.activeNote.value?.let { noteModel.putNoteToWebService( it ) }
        }

        stage.show()

        // Modify the toolbar to include only necessary items (can only be done after at least one layout pass)
        noteView.modifiedHTMLEditorToolbar()

        // ! important ! this also has to be done after stage.show()
        noteView.loadPreferences( preferences )
    }
}