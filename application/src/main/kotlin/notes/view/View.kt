// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import notes.shared.Constants
import notes.shared.model.Model
import notes.shared.model.TextChange
import notes.shared.preferences.Preferences


class View(private val noteModel: Model): BorderPane() {

    val noteListView = NoteList(noteModel).apply {
        val noteListBackground = if (Constants.theme == "light") Constants.LightNoteListBackgroundColor else Constants.DarkNoteListBackgroundColor
        style = "-fx-background-color: $noteListBackground;" // change to be theme background color
    }

    val tagsBar = TagsBar(noteModel, noteListView)
    val tagsBarScrollBar = ScrollPane(tagsBar)

    val noteView = NoteView( noteModel).apply{
        style = "-fx-line-spacing: 50px;"
    }

    val noteViewWrapper = VBox(noteView, tagsBarScrollBar).apply{
        VBox.setVgrow(noteView, Priority.ALWAYS)
    }

    private val menuBar = Menubar( noteModel, noteView, noteListView, tagsBar)
    private val topVBox = VBox( menuBar )

    private val searchBar = SearchBar(noteModel, noteListView)
    private val searchListVBox = VBox(searchBar, noteListView)

    private val noteList = ScrollPane(searchListVBox).apply {
        isFitToWidth = true
        minWidth = 250.0
        maxWidth = 500.0
    }

    private var splitView = SplitPane(noteList, noteViewWrapper)
    var curDividerPos = splitView.dividerPositions.first()
    val contextMenuSort = SortMenuContext(noteModel, noteListView)

    init {
        top = topVBox
        center = splitView

        // select most recent note
        if (noteModel.notes.isNotEmpty()) noteModel.setActiveNote(noteModel.notes.last())

        noteModel.isSplitView.addListener { _, _, newValue ->
            if (newValue) { // split view
                showNoteList()
            } else { // not split view
                hideNoteList()
            }
        }
        noteList.setOnMouseClicked{e ->
            if (e.button == MouseButton.SECONDARY) {
                noteList.contextMenu = this.contextMenuSort
            }
        }

        // val tagsList = noteModel.activeNote.value?.getAllTags()
        // val tagsBar = TagsBar()
    }

    private fun hideNoteList() {
        curDividerPos = splitView.dividerPositions.first()
        splitView.items.removeAt(0)
    }

    private fun showNoteList() {
        splitView.items.add(0, noteList)
        setDividerPos(curDividerPos)
    }

    /* Function below is adapted from:
    Source: https://stackoverflow.com/questions/10075841/how-to-hide-the-controls-of-htmleditor
    Commenter: Tag Howard
    */
    fun modifiedHTMLEditorToolbar() {

        Constants.notesArea.isVisible = false

        val toolBar1: ToolBar = Constants.notesArea.lookup(".top-toolbar") as ToolBar
        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar

        val nodesToKeepTop: HashSet<Node> = HashSet()
        val nodesToKeepBottom: HashSet<Node> = HashSet()

        toolBar1.items.forEach { e -> println(e) }
        toolBar2.items.forEach { e -> println(e) }

        nodesToKeepTop.add(Constants.notesArea.lookup(".html-editor-cut"))
        Constants.notesArea.lookup(".html-editor-cut").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }
        nodesToKeepTop.add(Constants.notesArea.lookup(".html-editor-copy"))
        nodesToKeepTop.add(Constants.notesArea.lookup(".html-editor-paste"))
        Constants.notesArea.lookup(".html-editor-paste").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }
//        nodesToKeepTop.add(editor.lookup(".html-editor-numbers"))
        nodesToKeepTop.add(Constants.notesArea.lookup(".html-editor-bullets"))
        Constants.notesArea.lookup(".html-editor-bullets").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.LIST)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }
        nodesToKeepTop.add(Constants.notesArea.lookup(".html-editor-foreground"))
        Constants.notesArea.lookup(".html-editor-foreground").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.COLOR)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }
//        nodesToKeepTop.add(editor.lookup(".html-editor-background"))

//        nodesToKeepBottom.add(editor.lookup(".font-menu-button"))
        nodesToKeepTop.add(Constants.notesArea.lookup(".font-menu-button"))
        nodesToKeepBottom.add(Constants.notesArea.lookup(".font-menu-button"))

        nodesToKeepBottom.add(Constants.notesArea.lookup(".html-editor-bold"))
        Constants.notesArea.lookup(".html-editor-bold").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.BOLD)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }
        nodesToKeepBottom.add(Constants.notesArea.lookup(".html-editor-italic"))
        Constants.notesArea.lookup(".html-editor-italic").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.ITALICIZE)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }
        nodesToKeepBottom.add(Constants.notesArea.lookup(".html-editor-underline"))
        Constants.notesArea.lookup(".html-editor-underline").addEventHandler(MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.UNDERLINE)
            noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
        }

        toolBar1.items.removeIf { n: Node? -> !nodesToKeepTop.contains(n) }
        toolBar2.items.removeIf { n: Node? -> !nodesToKeepBottom.contains(n) }

        // Add all items onto toolBar1
        val toCopy = ArrayList<Node>()
        toCopy.addAll(toolBar2.items)
        toolBar2.items.clear()
        toolBar1.items.addAll(toCopy)

        val date = Label(noteModel.activeNote.value?.getDateEdited()).apply {
            // minWidth = Label.USE_PREF_SIZE
            //minWidth = toolBar2.width
             alignment = Pos.CENTER
        }

        val title = TextField(noteModel.activeNote.value?.getNoteTitle()).apply {
        // minWidth = Label.USE_PREF_SIZE
        //minWidth = toolBar2.width
            alignment = Pos.CENTER
            promptText = "New Note"
        }

        title.textProperty().addListener { _, _, newValue ->
            if (noteModel.activeNote.value != null) {
                if (noteModel.activeNote.value?.getNoteTitle() == newValue) {
                    noteModel.activeNote.value?.changeNoteTitle(title.text)

                } else {
                    noteModel.activeNote.value?.setNoteTitle(title.text)
                    date.text = noteModel.activeNote.value?.getDateEdited()
                }
            }
            else {
                noteModel.createNote(newValue)
            }
        }

        toolBar2.isVisible = true // everything from toolBar2 in now in toolBar1
        toolBar2.isManaged = true
        toolBar2.items.addAll(title, date)
        toolBar2.apply {
            style = "-fx-background-color: white;"
        }

        // Now add custom buttons
        val listCollapsableImageView = ImageView(Image("Sidebar-Icon.png"))
        val undoImageView = ImageView(Image("Undo-Icon.png"))
        val redoImageView = ImageView(Image("Redo-Icon.png"))

        val listCollapsable = Button().apply {
            onMouseClicked = EventHandler {
                if ( noteModel.isSplitView.value ) { // split view
                    noteModel.isSplitView.set( false )
                } else { // not split view
                    noteModel.isSplitView.set( true )
                }
            }
        }
        listCollapsable.id = "list-collapsable"

        val undoButton = Button()
        undoButton.id = "undo-button"
        undoButton.setOnMouseClicked {
            Constants.notesArea.htmlText = noteModel.activeNote.value?.undo()
        }

        val redoButton = Button()
        redoButton.id = "redo-button"
        redoButton.setOnMouseClicked {
            Constants.notesArea.htmlText = noteModel.activeNote.value?.redo()
        }

        listCollapsableImageView.fitHeight = 20.0
        listCollapsableImageView.isPreserveRatio = true
        listCollapsable.setPrefSize(20.0, 20.0)
        listCollapsable.graphic = listCollapsableImageView

        undoImageView.fitHeight = 20.0
        undoImageView.isPreserveRatio = true
        undoButton.setPrefSize(20.0, 20.0)
        undoButton.graphic = undoImageView

        redoImageView.fitHeight = 20.0
        redoImageView.isPreserveRatio = true
        redoButton.setPrefSize(20.0, 20.0)
        redoButton.graphic = redoImageView

        toolBar1.items.add(0, listCollapsable)
        toolBar1.items.add(1, undoButton)
        toolBar1.items.add(2, redoButton)

        Constants.notesArea.addEventFilter(KeyEvent.KEY_RELEASED) { event ->
            if (event.isMetaDown && KeyCode.Z == event.code) {
                Constants.notesArea.htmlText = noteModel.activeNote.value?.undo()
                event.consume()
            }
            else if (event.isMetaDown && KeyCode.R == event.code) {
                Constants.notesArea.htmlText = noteModel.activeNote.value?.redo()
                event.consume()
            }
            else if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                noteModel.activeNote.value?.emptyRedo()
                noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
                event.consume()
            }
            else if (event.isMetaDown && KeyCode.X == event.code) {
                noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
                noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
                event.consume()
            }
            else if (event.isMetaDown && KeyCode.V == event.code) {
                noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
                noteModel.activeNote.value?.setNoteBody(Constants.notesArea.htmlText)
                event.consume()
            }
        }

        Constants.notesArea.isVisible = true
    }

        fun getDividerPos(): Double {
            return if (splitView.dividerPositions.isNotEmpty()) splitView.dividerPositions.first()
            else curDividerPos
        }

        fun setDividerPos(pos: Double) {
            if (splitView.dividerPositions.isNotEmpty()) splitView.dividers.first().position = pos
        }

        fun isListCollapsed(): Boolean {
            return splitView.items.size == 1
        }

        /*
     * for now loadPreferences just sets the divider position
     */
    fun loadPreferences( preferences: Preferences ) {
        if ( preferences.isListCollapsed ) {
            noteModel.isSplitView.set( false )
        } else {
            setDividerPos( preferences.dividerPos )
        }

        // NEED TO LOAD THIS FROM DATABASE PREFERENCES
        if (Constants.theme == "light") {
            menuBar.setLightTheme()
        }
        else {
            menuBar.setDarkTheme()
        }
    }
}