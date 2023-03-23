package notes.view

import javafx.scene.control.CheckMenuItem
import javafx.scene.control.Menu
import javafx.scene.control.SeparatorMenuItem
import notes.shared.model.Model

class SortMenu(noteModel: Model, noteList: NoteList): Menu("Sort") {
    private var sortDateCreated = CheckMenuItem("Date Created")
    private var sortDateEdited = CheckMenuItem("Date Edited")
    private var sortAlphaBody = CheckMenuItem("Note Body")
    private var sortAlphaTitle = CheckMenuItem("Note Title")
    private var sortSubMenuDivider = SeparatorMenuItem()
    var reverseOrder = false

    private val A_Z = "A to Z"
    private val Z_A = "Z to A"

    private val OLDEST = "Oldest to Newest"
    private val NEWEST = "Newest to Oldest"

    private var sortAscending = CheckMenuItem(this.getMessage(true, "Date"))
    private var sortDescending = CheckMenuItem(this.getMessage(false, "Date"))
    private fun getMessage(ascending: Boolean, type: String): String {
        if (ascending) {
            if (type == "Alphabetical") {
                return A_Z
            } else if (type == "Date") {
                return NEWEST
            }
        } else {
            if (type == "Alphabetical") {
                return Z_A
            } else if (type == "Date") {
                return OLDEST
            }
        }
        return ""
    }

    private fun setAllSelectedFalse(param: CheckMenuItem) {
        this.sortDateCreated.isSelected = false
        this.sortAlphaBody.isSelected = false
        this.sortDateEdited.isSelected = false
        this.sortAlphaTitle.isSelected = false
        param.isSelected = true
    }

    init {
        this.items.addAll(sortDateCreated, sortDateEdited, sortAlphaTitle, sortAlphaBody, sortSubMenuDivider, sortAscending, sortDescending)
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
            if (this.sortAlphaBody.isSelected) {
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
            if (this.sortAlphaBody.isSelected) {
                noteModel.sortAlpha(reverseOrder)
            } else {
                noteModel.sortDate(reverseOrder)
            }
            noteList.refreshList(noteModel.notes)
        }

        this.sortAlphaBody.setOnAction {
            this.setAllSelectedFalse(this.sortAlphaBody)
            this.sortAscending.text = this.getMessage(true, "Alphabetical")
            this.sortDescending.text = this.getMessage(false, "Alphabetical")
            println("Sort Alphabetical Menu")
            noteModel.sortAlpha(reverseOrder)
            noteList.refreshList(noteModel.notes)
        }

        this.sortDateCreated.setOnAction {
            this.setAllSelectedFalse(this.sortDateCreated)
            this.sortAscending.text = this.getMessage(true, "Date")
            this.sortDescending.text = this.getMessage(false, "Date")
            println("Sort Date Menu")
            noteModel.sortDate(reverseOrder)
            noteList.refreshList(noteModel.notes)
        }

        this.sortDateEdited.setOnAction {
            this.setAllSelectedFalse(this.sortDateEdited)
            this.sortAscending.text = this.getMessage(true, "Date")
            this.sortDescending.text = this.getMessage(false, "Date")
            println("Sort Date Edited Menu")
            noteModel.sortDateEdited(reverseOrder)
            noteList.refreshList(noteModel.notes)
        }

        this.sortAlphaTitle.setOnAction {
            this.setAllSelectedFalse(this.sortAlphaTitle)
            this.sortAscending.text = this.getMessage(true, "Alphabetical")
            this.sortDescending.text = this.getMessage(false, "Alphabetical")
            println("Sort Note Title")
            noteModel.sortAlphaTitle(reverseOrder)
            noteList.refreshList(noteModel.notes)
        }

    }
}