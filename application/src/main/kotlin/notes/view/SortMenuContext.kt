// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.beans.Observable
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.scene.control.CheckMenuItem
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import notes.shared.model.Model

class SortMenuContext(noteModel: Model, noteList: NoteList): ContextMenu() {
    val sortMenu = SortMenu(noteModel, noteList)
    val pinMessage: SimpleObjectProperty<String> = SimpleObjectProperty("Pin/Unpin")
    init {
        val pinMessage = MenuItem(pinMessage.get())

        this.items.add(sortMenu)
        this.items.add(pinMessage)

        pinMessage.setOnAction {
            if (noteModel.prepNote.value?.isPinned == true) {
                noteModel.prepNote.value?.removePin()
            } else {
                noteModel.prepNote.value?.pin()
            }
            noteList.refreshList(noteModel.notes)
        }

    }
}