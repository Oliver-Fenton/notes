package notes.view

import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import notes.model.Model

class View(private val noteModel: Model): BorderPane() {
    private val menuBar = Menubar( noteModel )
    private val toolbar = Toolbar()
    private val topVBox = VBox( menuBar, toolbar )

    private val noteView = NoteView( noteModel )
    private val noteList = ScrollPane( NoteList( noteModel ) ).apply {
        isFitToWidth = true
        minWidth = 200.0
        maxWidth = 500.0
    }
    private val mainContent = SplitPane( noteList, noteView )

    init {
        top = topVBox
        center = mainContent

        // select most recent note
        noteModel.setActiveNote( noteModel.notes.last() )
    }
}