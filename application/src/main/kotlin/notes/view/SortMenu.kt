// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.scene.control.CheckMenuItem
import javafx.scene.control.Menu
import javafx.scene.control.SeparatorMenuItem
import notes.shared.model.Model

class SortMenu(noteModel: Model, noteList: NoteList): Menu("Sort") {
    private var sortDateCreated = CheckMenuItem("Date Created")
    private var sortDateEdited = CheckMenuItem("Date Edited")
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
                return Z_A
            } else if (type == "Date") {
                return NEWEST
            }
        } else {
            if (type == "Alphabetical") {
                return A_Z
            } else if (type == "Date") {
                return OLDEST
            }
        }
        return ""
    }

    fun whichSort(noteModel: Model) {
        if (sortAlphaTitle.isSelected) {
            noteModel.sortAlphaTitle(this.reverseOrder)
        } else if (sortDateCreated.isSelected) {
            noteModel.sortDate(this.reverseOrder)
        } else if (sortDateEdited.isSelected) {
            noteModel.sortDateEdited(this.reverseOrder)
        }
    }

    private fun setAllSelectedFalse(param: CheckMenuItem) {
        this.sortDateCreated.isSelected = false
        this.sortDateEdited.isSelected = false
        this.sortAlphaTitle.isSelected = false
        param.isSelected = true
    }

    init {
        this.items.addAll(sortDateCreated, sortDateEdited, sortAlphaTitle, sortSubMenuDivider, sortAscending, sortDescending)
        this.sortAscending.isSelected = true
        this.sortDateCreated.isSelected = true

        this.sortAscending.setOnAction {
            this.sortAscending.isSelected = true
            this.sortDescending.isSelected = false
            this.reverseOrder = false
            whichSort(noteModel)
            noteList.refreshList(noteModel.notes)
        }

        this.sortDescending.setOnAction {
            this.sortDescending.isSelected = true
            this.sortAscending.isSelected = false
            this.reverseOrder = true
            whichSort(noteModel)
            noteList.refreshList(noteModel.notes)
        }

        this.sortDateCreated.setOnAction {
            this.setAllSelectedFalse(this.sortDateCreated)
            this.sortAscending.text = this.getMessage(true, "Date")
            this.sortDescending.text = this.getMessage(false, "Date")
            whichSort(noteModel)
            noteList.refreshList(noteModel.notes)
        }

        this.sortDateEdited.setOnAction {
            this.setAllSelectedFalse(this.sortDateEdited)
            this.sortAscending.text = this.getMessage(true, "Date")
            this.sortDescending.text = this.getMessage(false, "Date")
            whichSort(noteModel)
            noteList.refreshList(noteModel.notes)
        }

        this.sortAlphaTitle.setOnAction {
            this.setAllSelectedFalse(this.sortAlphaTitle)
            this.sortDescending.text = this.getMessage(false, "Alphabetical")
            this.sortAscending.text = this.getMessage(true, "Alphabetical")
            whichSort(noteModel)
            noteList.refreshList(noteModel.notes)
        }

    }
}