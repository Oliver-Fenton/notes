package notes.application

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCodeCombination
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {
        val borderPaneLayout = BorderPane()
       // val topVBox = VBox(Menubar(), Toolbar())
        borderPaneLayout.top = Menubar()

        stage.scene = Scene(borderPaneLayout, 250.0, 150.0)
        stage.isResizable = true
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.title = "Notes"
        stage.show()
    }
}