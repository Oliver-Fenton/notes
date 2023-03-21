package notes.view

import javafx.application.Platform
import javafx.beans.value.ObservableValueBase
import javafx.collections.FXCollections
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.MouseEvent
import notes.shared.SysInfo
import notes.shared.model.Model

class Menubar(noteModel: Model, noteView: NoteView, noteList: NoteList): MenuBar() {
    // FILE MENU
    private var fileMenu = Menu("File")
    private var newNote = MenuItem("New Note")
    private var newFolder = MenuItem("New Folder")
    private var deleteNote = MenuItem("Delete Note")
    private val quit = MenuItem("Quit")

    // EDIT MENU
    private var editMenu = Menu("Edit")
    private var undo = MenuItem("Undo")
    private var redo = MenuItem("Redo")
    private var cut = MenuItem("Cut")
    private var copy = MenuItem("Copy")
    private var paste = MenuItem("Paste")

    // VIEW MENU
    private var viewMenu = Menu("View")
    private var sortSubMenu = SortMenu(noteModel, noteList)

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
        deleteNote.setOnAction {
            println("User Deleted Active Note")
            noteModel.deleteNote()
        }
        //newFolder.setOnAction {  }
        quit.setOnAction { Platform.exit() }

        undo.setOnAction {
            noteView.notesArea.htmlText = noteModel.activeNote.value?.undo()
        }

        redo.setOnAction {
            noteView.notesArea.htmlText = noteModel.activeNote.value?.redo()
        }

        paste.setOnAction {
            val pasteButton = noteView.notesArea.lookup(".html-editor-paste")
            if (pasteButton is Button) {
                pasteButton.fire()
                noteModel.activeNote.value?.setNoteBody(noteView.notesArea.htmlText)
                println("activeNoteDataPaste: ${noteModel.activeNote.value?.getHTML()}")
            }
        }
        cut.setOnAction {
            val cutButton = noteView.notesArea.lookup(".html-editor-cut")
            if (cutButton is Button) {
                cutButton.fire()
                noteModel.activeNote.value?.setNoteBody(noteView.notesArea.htmlText)
                println("activeNoteDataCut: ${noteModel.activeNote.value?.getHTML()}")
            }
        }
        copy.setOnAction {
            val copyButton = noteView.notesArea.lookup(".html-editor-copy")
            if (copyButton is Button) {
                copyButton.fire()
            }
        }


        // TODO: DON'T FORGET TO ADD TEMP FROM VIEW MENU WHEN DECIDED

        val OS_KeyCombo = if (SysInfo.osName.contains("Mac")) KeyCodeCombination.META_DOWN else KeyCodeCombination.CONTROL_DOWN

        newNote.accelerator = KeyCodeCombination(KeyCode.N, OS_KeyCombo)
        deleteNote.accelerator = KeyCodeCombination(KeyCode.D, OS_KeyCombo)
        newFolder.accelerator = KeyCodeCombination(KeyCode.F, OS_KeyCombo)
        quit.accelerator = KeyCodeCombination(KeyCode.Q, OS_KeyCombo)
        undo.accelerator = KeyCodeCombination(KeyCode.Z, OS_KeyCombo)
        redo.accelerator = KeyCodeCombination(KeyCode.R, OS_KeyCombo) // not able to do 3 keycode combo
        cut.accelerator = KeyCodeCombination(KeyCode.X, OS_KeyCombo)
        copy.accelerator = KeyCodeCombination(KeyCode.C, OS_KeyCombo)
        paste.accelerator = KeyCodeCombination(KeyCode.V, OS_KeyCombo)

        fileMenu.items.addAll(newNote, deleteNote, newFolder, quit)
        editMenu.items.addAll(undo, redo, cut, copy, paste)

        viewMenu.items.addAll(sortSubMenu)
        this.menus.addAll(fileMenu, editMenu, viewMenu)

    }
}
