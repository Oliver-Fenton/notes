package notes.view

import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import notes.shared.Constants
import notes.shared.SysInfo
import notes.shared.model.Model

class Menubar(val noteModel: Model, val noteView: NoteView, val noteList: NoteList, val tagsBar: TagsBar): MenuBar() {
    // FILE MENU
    private var fileMenu = Menu("File")
    private var newNote = MenuItem("New Note")
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
    private var darkTheme = MenuItem("Dark Theme")
    private var lightTheme = MenuItem("Light Theme")

    init {

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
            Constants.notesArea.htmlText = noteModel.activeNote.value?.undo()
        }

        redo.setOnAction {
            Constants.notesArea.htmlText = noteModel.activeNote.value?.redo()
        }

        paste.setOnAction {
            val pasteButton = Constants.notesArea.lookup(".html-editor-paste")
            if (pasteButton is Button) {
                pasteButton.fire()
                noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
                println("activeNoteDataPaste: ${noteModel.activeNote.value?.getHTML()}")
            }
        }
        cut.setOnAction {
            val cutButton = Constants.notesArea.lookup(".html-editor-cut")
            if (cutButton is Button) {
                cutButton.fire()
                noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
                println("activeNoteDataCut: ${noteModel.activeNote.value?.getHTML()}")
            }
        }
        copy.setOnAction {
            val copyButton = Constants.notesArea.lookup(".html-editor-copy")
            if (copyButton is Button) {
                copyButton.fire()
            }
        }

        darkTheme.setOnAction {
            setDarkTheme()
        }
        lightTheme.setOnAction {
            setLightTheme()
        }


        // TODO: DON'T FORGET TO ADD TEMP FROM VIEW MENU WHEN DECIDED

        val OS_KeyCombo = if (SysInfo.osName.contains("Mac")) KeyCodeCombination.META_DOWN else KeyCodeCombination.CONTROL_DOWN

        newNote.accelerator = KeyCodeCombination(KeyCode.N, OS_KeyCombo)
        deleteNote.accelerator = KeyCodeCombination(KeyCode.D, OS_KeyCombo)
        quit.accelerator = KeyCodeCombination(KeyCode.Q, OS_KeyCombo)
        undo.accelerator = KeyCodeCombination(KeyCode.Z, OS_KeyCombo)
        redo.accelerator = KeyCodeCombination(KeyCode.R, OS_KeyCombo) // not able to do 3 keycode combo
        cut.accelerator = KeyCodeCombination(KeyCode.X, OS_KeyCombo)
        copy.accelerator = KeyCodeCombination(KeyCode.C, OS_KeyCombo)
        paste.accelerator = KeyCodeCombination(KeyCode.V, OS_KeyCombo)

        fileMenu.items.addAll(newNote, deleteNote, quit)
        editMenu.items.addAll(undo, redo, cut, copy, paste)
        viewMenu.items.addAll(darkTheme, lightTheme, sortSubMenu)

        this.menus.addAll(fileMenu, editMenu, viewMenu)
    }

    fun setDarkTheme() {
        scene.root.style = "-fx-base:black"
        Constants.theme = "dark"
        noteList.refreshList(noteModel.notes)
        noteList.apply {
            style = "-fx-background-color: ${Constants.DarkNoteListBackgroundColor};" // change to be theme background color
        }
        val newHtml = noteModel.activeNote.value?.changeBodyBackgroundColor(Constants.DarkHTMLEditorColor)
        if (newHtml != null) {
            noteModel.activeNote.value?.changeNoteBody(newHtml)
            noteView.setTextArea(newHtml)
        }

        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar
        toolBar2.apply {
            style = "-fx-background-color: ${Constants.DarkToolbarColor};"
        }

        val toolBar1: ToolBar = Constants.notesArea.lookup(".top-toolbar") as ToolBar
        for (node in toolBar1.lookupAll(".button")) {
            if (node is Button) {
                if (node.id == "undo-button") {
                    val undoImageView = ImageView(Image("Dark-Undo-Icon.png"))
                    undoImageView.fitHeight = 20.0
                    undoImageView.isPreserveRatio = true
                    node.graphic = undoImageView
                }
                else if (node.id == "redo-button") {
                    val redoImageView = ImageView(Image("Dark-Redo-Icon.png"))
                    redoImageView.fitHeight = 20.0
                    redoImageView.isPreserveRatio = true
                    node.setPrefSize(20.0, 20.0)
                    node.graphic = redoImageView
                }
                else if (node.id == "list-collapsable") {
                    val listCollapsableImageView = ImageView(Image("Dark-Sidebar-Icon.png"))
                    listCollapsableImageView.fitHeight = 20.0
                    listCollapsableImageView.isPreserveRatio = true
                    node.setPrefSize(20.0, 20.0)
                    node.graphic = listCollapsableImageView
                }
            }
        }
        tagsBar.setTagsBarColors()
    }

    fun setLightTheme() {
        scene.root.style = ""
        Constants.theme = "light"
        noteList.refreshList(noteModel.notes)
        noteList.apply {
            style = "-fx-background-color: ${Constants.LightNoteListBackgroundColor};" // change to be theme background color
        }
        val newHtml = noteModel.activeNote.value?.changeBodyBackgroundColor(Constants.LightHTMLEditorColor)
        if (newHtml != null) {
            noteModel.activeNote.value?.changeNoteBody(newHtml)
            noteView.setTextArea(newHtml)
        }

        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar
        toolBar2.apply {
            style = "-fx-background-color: ${Constants.LightToolbarColor};"
        }

        val toolBar1: ToolBar = Constants.notesArea.lookup(".top-toolbar") as ToolBar
        for (node in toolBar1.lookupAll(".button")) {
            if (node is Button) {
                if (node.id == "undo-button") {
                    val undoImageView = ImageView(Image("Undo-Icon.png"))
                    undoImageView.fitHeight = 20.0
                    undoImageView.isPreserveRatio = true
                    node.graphic = undoImageView
                }
                else if (node.id == "redo-button") {
                    val redoImageView = ImageView(Image("Redo-Icon.png"))
                    redoImageView.fitHeight = 20.0
                    redoImageView.isPreserveRatio = true
                    node.setPrefSize(20.0, 20.0)
                    node.graphic = redoImageView
                }
                else if (node.id == "list-collapsable") {
                    val listCollapsableImageView = ImageView(Image("Sidebar-Icon.png"))
                    listCollapsableImageView.fitHeight = 20.0
                    listCollapsableImageView.isPreserveRatio = true
                    node.setPrefSize(20.0, 20.0)
                    node.graphic = listCollapsableImageView
                }
            }
        }
        tagsBar.setTagsBarColors()
    }
}
