package notes.application

import javafx.application.Platform
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCode

class Menubar: MenuBar() {
    // FILE MENU
    var fileMenu = Menu("File")
    var newNote = MenuItem("New Note")
    var newFolder = MenuItem("New Folder")
    val quit = MenuItem("Quit")

    // EDIT MENU
    var editMenu = Menu("Edit")
    var undo = MenuItem("Undo")
    var redo = MenuItem("Redo")
    var cut = MenuItem("Cut")
    var copy = MenuItem("Copy")
    var paste = MenuItem("Paste")
    var selectAll = MenuItem("Select All")

    // VIEW MENU
    var viewMenu = Menu("View")
    var temp = MenuItem("Temp") // TODO: update when decided

    init {
        // Set action for menu items

        // TODO: ADD FUNCTIONALITY WHEN POSSIBLE
        //newNote.setOnAction {  }
        //newFolder.setOnAction {  }
        quit.setOnAction { Platform.exit() }
        //undo.setOnAction {  }
        //redo.setOnAction {  }
        //cut.setOnAction {  }
        //copy.setOnAction {  }
        //paste.setOnAction {  }
        //selectAll.setOnAction { }
        // TODO: DON'T FORGET TO ADD TEMP FROM VIEW MENU WHEN DECIDED

        // TODO: remove next 2 lines when next todo is completed
        //quit.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN) // Windows
        quit.accelerator = KeyCodeCombination(KeyCode.Q, KeyCombination.META_DOWN) // Mac

        // TODO: remove and add this section to Constant
        //if OS = Mac then use OS_KeyCombo = KeyCombination.META_DOWN
        //else use OS_KeyCombo = KeyCombination.CONTROL_DOWN
        /*
        newNote.accelerator = KeyCodeCombination(KeyCode.N, OS_KeyCombo)
        newFolder.accelerator = KeyCodeCombination(KeyCode.F, OS_KeyCombo)
        quit.accelerator = KeyCodeCombination(KeyCode.Q, OS_KeyCombo)
        undo.accelerator = KeyCodeCombination(KeyCode.Z, OS_KeyCombo)
        redo.accelerator = KeyCodeCombination(KeyCode.R, OS_KeyCombo) // not able to do 3 keycode combo
        cut.accelerator = KeyCodeCombination(KeyCode.X, OS_KeyCombo)
        copy.accelerator = KeyCodeCombination(KeyCode.C, OS_KeyCombo)
        paste.accelerator = KeyCodeCombination(KeyCode.V, OS_KeyCombo)
        selectAll.accelerator = KeyCodeCombination(KeyCode.A, OS_KeyCombo)
        */


        fileMenu.items.addAll(newNote, newFolder, quit)
        editMenu.items.addAll(undo, redo, cut, copy, paste, selectAll)
        viewMenu.items.add(temp)

        this.menus.addAll(fileMenu, editMenu, viewMenu)
    }
}