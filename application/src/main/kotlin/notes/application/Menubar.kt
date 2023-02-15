package notes.application

import javafx.application.Platform
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCode

class Menubar: MenuBar() {
    // FILE MENU
    private var fileMenu = Menu("File")
    private var newNote = MenuItem("New Note")
    private var newFolder = MenuItem("New Folder")
    private val quit = MenuItem("Quit")

    // EDIT MENU
    private var editMenu = Menu("Edit")
    private var undo = MenuItem("Undo")
    private var redo = MenuItem("Redo")
    private var cut = MenuItem("Cut")
    private var copy = MenuItem("Copy")
    private var paste = MenuItem("Paste")
    private var selectAll = MenuItem("Select All")

    // VIEW MENU
    private var viewMenu = Menu("View")
    private var temp = MenuItem("Temp") // TODO: update when decided

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

        newNote.accelerator = KeyCodeCombination(KeyCode.N, Constants.OS_KeyCombo)
        newFolder.accelerator = KeyCodeCombination(KeyCode.F, Constants.OS_KeyCombo)
        quit.accelerator = KeyCodeCombination(KeyCode.Q, Constants.OS_KeyCombo)
        undo.accelerator = KeyCodeCombination(KeyCode.Z, Constants.OS_KeyCombo)
        redo.accelerator = KeyCodeCombination(KeyCode.R, Constants.OS_KeyCombo) // not able to do 3 keycode combo
        cut.accelerator = KeyCodeCombination(KeyCode.X, Constants.OS_KeyCombo)
        copy.accelerator = KeyCodeCombination(KeyCode.C, Constants.OS_KeyCombo)
        paste.accelerator = KeyCodeCombination(KeyCode.V, Constants.OS_KeyCombo)
        selectAll.accelerator = KeyCodeCombination(KeyCode.A, Constants.OS_KeyCombo)


        fileMenu.items.addAll(newNote, newFolder, quit)
        editMenu.items.addAll(undo, redo, cut, copy, paste, selectAll)
        viewMenu.items.add(temp)

        this.menus.addAll(fileMenu, editMenu, viewMenu)
    }
}