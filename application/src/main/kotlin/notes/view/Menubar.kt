// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

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
import notes.shared.preferences.Theme

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
    private var themeSubMenu = Menu("Theme")
    var darkTheme = CheckMenuItem("Dark")
    var lightTheme = CheckMenuItem("Light")

    init {

        // TODO: ADD FUNCTIONALITY WHEN POSSIBLE
        newNote.setOnAction {
            noteModel.createNote()
            //var newNote = NoteTemplate("")
            // data.listOfNotes.add(newNote)
            // data.preview.add("New Note")
        }
        deleteNote.setOnAction {
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
            }
        }
        cut.setOnAction {
            val cutButton = Constants.notesArea.lookup(".html-editor-cut")
            if (cutButton is Button) {
                cutButton.fire()
                noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
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
        themeSubMenu.items.addAll(darkTheme, lightTheme)
        // light theme as default selected
        lightTheme.isSelected = true
        viewMenu.items.addAll(themeSubMenu, sortSubMenu)
        this.menus.addAll(fileMenu, editMenu, viewMenu)
    }

    fun setDarkTheme() {
        darkTheme.isSelected = true
        lightTheme.isSelected = false
        scene.root.style = "-fx-base:black"
        Constants.theme = "dark"
        noteList.refreshList(noteModel.notes)
        noteList.apply {
            style = "-fx-background-color: ${Constants.DarkNoteListBackgroundColor};" // change to be theme background color
        }
        if (noteModel.activeNote.value != null) {
            val newHtml = noteModel.activeNote.value?.changeBodyBackgroundColor(Constants.DarkHTMLEditorColor)
            if (newHtml != null) {
                noteModel.activeNote.value?.changeNoteBody(newHtml)
                noteView.setTextArea(newHtml)
            }
        }
        else {
            val beginningStyleIndex = Constants.notesArea.htmlText.indexOf("<body style=")
            val endingStyleIndex = Constants.notesArea.htmlText.indexOf("contenteditable=")

            if (beginningStyleIndex == -1) { // no style set yet
                var bodyTagIndex = Constants.notesArea.htmlText.indexOf("<body")
                var beginningSubstring = Constants.notesArea.htmlText.substring(0, bodyTagIndex + 5)
                var endingSubstring = Constants.notesArea.htmlText.substring(bodyTagIndex + 5)
                Constants.notesArea.htmlText = "$beginningSubstring style=\"background-color: ${Constants.DarkHTMLEditorColor};\" $endingSubstring"
            }
            else { // has style already
                var beginningSubstring = Constants.notesArea.htmlText.substring(0, beginningStyleIndex + 12)
                var endingSubstring = Constants.notesArea.htmlText.substring(endingStyleIndex)
                Constants.notesArea.htmlText = "$beginningSubstring\"background-color: ${Constants.DarkHTMLEditorColor};\" $endingSubstring"
            }
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
        lightTheme.isSelected = true
        darkTheme.isSelected = false
        scene.root.style = ""
        Constants.theme = "light"
        noteList.refreshList(noteModel.notes)
        noteList.apply {
            style = "-fx-background-color: ${Constants.LightNoteListBackgroundColor};" // change to be theme background color
        }
        if (noteModel.activeNote.value != null) {
            val newHtml = noteModel.activeNote.value?.changeBodyBackgroundColor(Constants.LightHTMLEditorColor)
            if (newHtml != null) {
                noteModel.activeNote.value?.changeNoteBody(newHtml)
                noteView.setTextArea(newHtml)
            }
        }
        else {
            val beginningStyleIndex = Constants.notesArea.htmlText.indexOf("<body style=")
            val endingStyleIndex = Constants.notesArea.htmlText.indexOf("contenteditable=")

            if (beginningStyleIndex == -1) { // no style set yet
                var bodyTagIndex = Constants.notesArea.htmlText.indexOf("<body")
                var beginningSubstring = Constants.notesArea.htmlText.substring(0, bodyTagIndex + 5)
                var endingSubstring = Constants.notesArea.htmlText.substring(bodyTagIndex + 5)
                Constants.notesArea.htmlText = "$beginningSubstring style=\"background-color: ${Constants.LightHTMLEditorColor};\" $endingSubstring"
            }
            else { // has style already
                var beginningSubstring = Constants.notesArea.htmlText.substring(0, beginningStyleIndex + 12)
                var endingSubstring = Constants.notesArea.htmlText.substring(endingStyleIndex)
                Constants.notesArea.htmlText = "$beginningSubstring\"background-color: ${Constants.LightHTMLEditorColor};\" $endingSubstring"
            }
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

    fun getTheme(): Theme {
        return if (lightTheme.isSelected) Theme.LIGHT else Theme.DARK
    }
}
