package notes.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {
        val borderPaneLayout = BorderPane()
//        borderPaneLayout.top = Toolbar()

        stage.scene = Scene(borderPaneLayout, 250.0, 150.0)
        stage.isResizable = true
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.title = "Notes"
        stage.show()
    }
}