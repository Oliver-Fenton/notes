package notes.view

import javafx.beans.value.ChangeListener
import javafx.scene.Scene
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import notes.model.Model

class View(private val noteModel: Model): BorderPane() {
    private val menuBar = Menubar( noteModel )
    private val toolbar = Toolbar( noteModel )
    private val topVBox = VBox( menuBar, toolbar )

    private val noteView = NoteView( noteModel )

    private val searchBar = SearchBar()
    private val searchListVBox = VBox(searchBar, NoteList(noteModel))

    private val noteList = ScrollPane(searchListVBox).apply {
        isFitToWidth = true
        minWidth = 200.0
        maxWidth = 500.0
    }

    init {
        top = topVBox
        center = SplitPane( noteList, noteView )

        // select most recent note
        noteModel.setActiveNote( noteModel.notes.last() )

        noteModel.isSplitView.addListener { _, _, newValue ->
            if ( newValue ) { // split view
                showNoteList()
            } else { // not split view
                hideNoteList()
            }
        }
    }

    fun hideNoteList() {
        center = noteView
    }

    fun showNoteList() {
        center = SplitPane( noteList, noteView )
    }
}