package notes.application

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import notes.model.Model

class Main : Application() {
    val notesModel = Model()

    override fun start(stage: Stage) {
        val borderPaneLayout = BorderPane()
       // val topVBox = VBox(Menubar(), Toolbar())

        // noteView contains note text box
        val noteView = NoteView(notesModel)
        // noteList is a VBox displaying all notes
        val noteList = ScrollPane(NoteList(notesModel, noteView)).apply {
            isFitToWidth = true
        }

        notesModel.setActiveNote(notesModel.notes.last().value)


        borderPaneLayout.top = Menubar(notesModel)
        borderPaneLayout.center = SplitPane(noteList, noteView)

        stage.scene = Scene(borderPaneLayout, 250.0, 150.0)
        stage.isResizable = true
        stage.minWidth = 600.0
        stage.minHeight = 400.0
        stage.isResizable = true
        stage.title = "Notes"
        stage.show()
    }
}