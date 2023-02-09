package notes.model

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import notes.shared.SysInfo

class Model {
    companion object {
        val testMessage = "Hello ${SysInfo.userName}"
    }
}