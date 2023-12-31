// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.StackPane
import javafx.scene.text.Text
import notes.shared.model.Model
import notes.shared.model.NoteData

class SearchBar(noteModel: Model, nList: NoteList) : StackPane(){
    private val searchBar = TextField()
    private val labelContainer = StackPane()
    private val searchLabel = Text("search")

    private val searchButton = Button("x")
    private val searchContainer = HBox(searchBar, searchButton)

    private fun getSearchValue(): String {
        return this.searchBar.text
    }

    fun tagMatch(list: ObservableList<NoteData>, input: String) {
        for (item in list) {
            for (tag in item.tags) {
                if (input == tag) {
                    item.doDisplay()
                    break
                }
            }
        }
    }

    private fun clearDisplay(list: ObservableList<NoteData>) {
        for (item in list) {
            item.notDisplay()
        }
    }

    private fun cleanWhitespace(list: List<String>): MutableList<String> {
        val temp = mutableListOf<String>()
        for (item in list) {
            temp.add(item.trim())
        }
        return temp
    }

    fun stringMatch(list: ObservableList<NoteData>, input: String) {
        if (input.length > 1 && input.substring(0,1) == "#") {
            clearDisplay(list)
            val parsedTags = input.substring(1,input.length).split(";")
            val cleanParsedTags = cleanWhitespace(parsedTags)
            for (tag in cleanParsedTags) {
                tagMatch(list, tag)
            }
        } else {
            for (item in list) {
                val ref = item.getText()
                val title = item.getNoteTitle()
                if (ref.contains(input, true) || title.contains(input, true)) {
                    item.doDisplay()
                } else {
                    item.notDisplay()
                }
            }
        }
    }

    init {
        searchBar.promptText = "search"
        searchBar.isFocusTraversable = false
        searchButton.isFocusTraversable = false
        HBox.setHgrow(searchBar, Priority.ALWAYS)
        this.labelContainer.children.add(searchLabel)
        children.add(searchContainer)

        this.searchButton.onMouseClicked = EventHandler {
            stringMatch(noteModel.notes, "")
            this.searchBar.text = ""
            nList.refreshList(noteModel.notes)
        }
        this.searchBar.onKeyReleased = EventHandler {
            val comp = this.getSearchValue()
            stringMatch(noteModel.notes, comp)
            nList.refreshList(noteModel.notes)
        }
    }
}