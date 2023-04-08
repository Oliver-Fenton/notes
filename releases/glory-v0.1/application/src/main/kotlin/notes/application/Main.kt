package notes.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import notes.model.Model
import notes.view.Menubar
import notes.view.Toolbar
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
    }
}