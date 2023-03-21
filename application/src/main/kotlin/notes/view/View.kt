package notes.view

import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.web.HTMLEditor
import notes.shared.model.Model
import notes.shared.model.TextChange
import notes.shared.preferences.Preferences


class View(private val noteModel: Model): BorderPane() {

    val noteView = NoteView(noteModel)
    private val menuBar = Menubar(noteModel, noteView)
    private val topVBox = VBox(menuBar)

    val noteListView = NoteList(noteModel).apply {
        style = "-fx-background-color: white;"
    }

    private val searchBar = SearchBar(noteModel, noteListView)
    private val searchListVBox = VBox(searchBar, noteListView)

    private val noteList = ScrollPane(searchListVBox).apply {
        isFitToWidth = true
        minWidth = 200.0
        maxWidth = 500.0
    }

    private var splitView = SplitPane(noteList, noteView)
    var curDividerPos = splitView.dividerPositions.first()

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
    fun modifiedHTMLEditorToolbar(editor: HTMLEditor) {

        editor.isVisible = false

        val toolBar1: ToolBar = editor.lookup(".top-toolbar") as ToolBar
        val toolBar2: ToolBar = editor.lookup(".bottom-toolbar") as ToolBar

        val nodesToKeepTop: HashSet<Node> = HashSet()
        val nodesToKeepBottom: HashSet<Node> = HashSet()

        //toolBar1.items.forEach { e -> println(e) }
        //toolBar2.items.forEach { e -> println(e) }

        nodesToKeepTop.add(editor.lookup(".html-editor-cut"))
        editor.lookup(".html-editor-cut").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
        }
        nodesToKeepTop.add(editor.lookup(".html-editor-copy"))
        nodesToKeepTop.add(editor.lookup(".html-editor-paste"))
        editor.lookup(".html-editor-paste").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
        }
//        nodesToKeepTop.add(editor.lookup(".html-editor-numbers"))
        nodesToKeepTop.add(editor.lookup(".html-editor-bullets"))
        editor.lookup(".html-editor-bullets").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.LIST)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
        }
        nodesToKeepTop.add(editor.lookup(".html-editor-foreground"))
        editor.lookup(".html-editor-foreground").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.COLOR)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
        }
//        nodesToKeepTop.add(editor.lookup(".html-editor-background"))

//        nodesToKeepBottom.add(editor.lookup(".font-menu-button"))
        nodesToKeepBottom.add(editor.lookup(".html-editor-bold"))
        editor.lookup(".html-editor-bold").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.BOLD)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
        }
        nodesToKeepBottom.add(editor.lookup(".html-editor-italic"))
        editor.lookup(".html-editor-italic").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.ITALICIZE)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
        }
        nodesToKeepBottom.add(editor.lookup(".html-editor-underline"))
        editor.lookup(".html-editor-underline").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
            noteModel.activeNote.value?.addToUndoStack(TextChange.UNDERLINE)
            noteModel.activeNote.value?.setNoteBody(editor.htmlText)
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
        }

        title.textProperty().addListener { _, _, newValue ->
            if (noteModel.activeNote.value?.getNoteTitle() == newValue) {
                noteModel.activeNote.value?.changeNoteTitle(title.text)

            } else {
                noteModel.activeNote.value?.setNoteTitle(title.text)
                date.text = noteModel.activeNote.value?.getDateEdited()
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
                    if (noteModel.isSplitView.value) { // split view
                        noteModel.isSplitView.set(false)
                    } else { // not split view
                        noteModel.isSplitView.set(true)
                    }
                }
            }
            val undoButton = Button()
            undoButton.setOnMouseClicked {
                editor.htmlText = noteModel.activeNote.value?.undo()
            }

            val redoButton = Button()
            redoButton.setOnMouseClicked {
                editor.htmlText = noteModel.activeNote.value?.redo()
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

            editor.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED) { event ->
                if (event.isMetaDown && KeyCode.Z == event.code) {
                    editor.htmlText = noteModel.activeNote.value?.undo()
                    event.consume()
                } else if (event.isMetaDown && KeyCode.R == event.code) {
                    editor.htmlText = noteModel.activeNote.value?.redo()
                    event.consume()
                } else if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                    noteModel.activeNote.value?.emptyRedo()
                    noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
                    event.consume()
                } else if (event.isMetaDown && KeyCode.X == event.code) {
                    noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
                    noteModel.activeNote.value?.setNoteBody(editor.htmlText)
                    event.consume()
                } else if (event.isMetaDown && KeyCode.V == event.code) {
                    noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT)
                    noteModel.activeNote.value?.setNoteBody(editor.htmlText)
                    event.consume()
                }
            }

            editor.isVisible = true
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
        fun loadPreferences(preferences: Preferences) {
            if (preferences.isListCollapsed) {
                noteModel.isSplitView.set(false)
            } else {
                setDividerPos(preferences.dividerPos)
            }
        }

}
