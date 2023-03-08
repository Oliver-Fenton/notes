package notes.view

import javafx.application.Platform
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCode
import notes.model.Model
import notes.shared.SysInfo

class Menubar(noteModel: Model): MenuBar() {
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
        newNote.setOnAction {
            println("User Created New Note")
            noteModel.createNote()
            //var newNote = NoteTemplate("")
            // data.listOfNotes.add(newNote)
            // data.preview.add("New Note")
        }
        //newFolder.setOnAction {  }
        quit.setOnAction { Platform.exit() }
        //undo.setOnAction {  }
        //redo.setOnAction {  }
        //cut.setOnAction {  }
        //copy.setOnAction {  }
        //paste.setOnAction {  }
        //selectAll.setOnAction { }
        // TODO: DON'T FORGET TO ADD TEMP FROM VIEW MENU WHEN DECIDED

//        newNote.accelerator = KeyCodeCombination(KeyCode.N, SysInfo.OS_KeyCombo)
//        newFolder.accelerator = KeyCodeCombination(KeyCode.F, SysInfo.OS_KeyCombo)
//        quit.accelerator = KeyCodeCombination(KeyCode.Q, SysInfo.OS_KeyCombo)
//        undo.accelerator = KeyCodeCombination(KeyCode.Z, SysInfo.OS_KeyCombo)
//        redo.accelerator = KeyCodeCombination(KeyCode.R, SysInfo.OS_KeyCombo) // not able to do 3 keycode combo
//        cut.accelerator = KeyCodeCombination(KeyCode.X, SysInfo.OS_KeyCombo)
//        copy.accelerator = KeyCodeCombination(KeyCode.C, SysInfo.OS_KeyCombo)
//        paste.accelerator = KeyCodeCombination(KeyCode.V, SysInfo.OS_KeyCombo)
//        selectAll.accelerator = KeyCodeCombination(KeyCode.A, SysInfo.OS_KeyCombo)


        fileMenu.items.addAll(newNote, newFolder, quit)
        editMenu.items.addAll(undo, redo, cut, copy, paste, selectAll)
        viewMenu.items.add(temp)

        this.menus.addAll(fileMenu, editMenu, viewMenu)
    }
}
