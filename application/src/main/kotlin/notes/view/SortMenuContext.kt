// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.scene.control.ContextMenu

class SortMenuContext(sortMenu: SortMenu): ContextMenu() {
    init {
        this.items.add(sortMenu)
    }
}