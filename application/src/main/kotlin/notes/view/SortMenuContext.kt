package notes.view

import javafx.scene.control.ContextMenu

class SortMenuContext(sortMenu: SortMenu): ContextMenu() {
    init {
        this.items.add(sortMenu)
    }
}