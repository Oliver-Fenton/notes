package notes.view

import javafx.scene.control.CheckMenuItem
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import notes.shared.model.Model

class SortMenu(noteModel: Model, noteList: NoteList): Menu("Sort") {
    private var sortDateCreated = CheckMenuItem("Date Created")
    private var sortDateEdited = CheckMenuItem("Date Edited")
    private var sortAlpha = CheckMenuItem("Alphabetical")
    private var sortSubMenuDivider = SeparatorMenuItem()
    var reverseOrder = false
    private var temp = MenuItem("Temp") // TODO: update when decided

    private val A_Z = "A to Z"
    private val Z_A = "Z to A"

    private val OLDEST = "Oldest to Newest"
    private val NEWEST = "Newest to Oldest"

    private var sortAscending = CheckMenuItem(this.getMessage(true, "Date"))
    private var sortDescending = CheckMenuItem(this.getMessage(false, "Date"))
    private fun getMessage(ascending: Boolean, type: String): String {
        if (ascending) {
            if (type.equals("Alphabetical")) {
                return A_Z
            } else if (type.equals("Date")) {
                return NEWEST
            }
        } else {
            if (type.equals("Alphabetical")) {
                return Z_A
            } else if (type.equals("Date")) {
                return OLDEST
            }
        }
        return ""
    }
    init {
        this.items.addAll(sortDateCreated, sortDateEdited, sortAlpha, sortSubMenuDivider, sortAscending, sortDescending)
        this.sortAscending.isSelected = true
        this.sortDateCreated.isSelected = true

        this.sortAscending.setOnAction {
            this.sortAscending.isSelected = true
            if (this.sortAscending.isSelected) {
                this.sortDescending.isSelected = false
                this.reverseOrder = false
            }
            noteModel.sortAlpha(reverseOrder)
            noteList.refreshList(noteModel.notes)
            if (this.sortAlpha.isSelected) {
                noteModel.sortAlpha(reverseOrder)
            } else {
                noteModel.sortDate(reverseOrder)
            }
        }

        this.sortDescending.setOnAction {
            this.sortDescending.isSelected = true
            if (this.sortDescending.isSelected) {
                this.sortAscending.isSelected = false
                this.reverseOrder = true
            }
            if (this.sortAlpha.isSelected) {
                noteModel.sortAlpha(reverseOrder)
            } else {
                noteModel.sortDate(reverseOrder)
            }
            noteList.refreshList(noteModel.notes)
        }

        this.sortAlpha.setOnAction {
            this.sortAlpha.isSelected = true
            this.sortDateCreated.isSelected = false
            this.sortAscending.text = this.getMessage(true, "Alphabetical")
            this.sortDescending.text = this.getMessage(false, "Alphabetical")
            println("Sort Alphabetical Menu")
            noteModel.sortAlpha(reverseOrder)
            noteList.refreshList(noteModel.notes)
        }

        this.sortDateCreated.setOnAction {
            this.sortDateCreated.isSelected = true
            this.sortAlpha.isSelected = false
            this.sortAscending.text = this.getMessage(true, "Date")
            this.sortDescending.text = this.getMessage(false, "Date")
            println("Sort Date Menu")
            noteModel.sortDate(reverseOrder)
            noteList.refreshList(noteModel.notes)
        }

    }
}