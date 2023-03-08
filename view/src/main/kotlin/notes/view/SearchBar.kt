package notes.view

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import java.util.Stack

class SearchBar : StackPane(){
    private val searchBar = TextField()
    private val labelContainer = StackPane()
    private val searchLabel = Text("search")

    private val searchButton = Button("Go")
    private val searchContainer = HBox(searchBar, searchButton)

    private fun getSearchValue() {
        println(this.searchBar.text)
    }

    init {
        HBox.setHgrow(searchBar, Priority.ALWAYS)
        this.labelContainer.children.add(searchLabel)
        children.add(searchContainer)

        this.searchButton.onMouseClicked = EventHandler {
            this.getSearchValue()
        }
    }
}