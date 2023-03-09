package notes.view

import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ScrollPane
import javafx.scene.control.SplitPane
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.web.HTMLEditor
import notes.model.Model
import notes.model.TextChange


class View(private val noteModel: Model): BorderPane() {

    private val menuBar = Menubar( noteModel )
    private val topVBox = VBox( menuBar )

    val noteView = NoteView( noteModel )

    private val noteList = ScrollPane( NoteList( noteModel ) ).apply {
        isFitToWidth = true
        minWidth = 200.0
        maxWidth = 500.0
    }

    init {
        top = topVBox
        center = SplitPane( noteList, noteView)

        // select most recent note
        noteModel.setActiveNote( noteModel.notes.last() )

        noteModel.isSplitView.addListener { _, _, newValue ->
            if ( newValue ) { // split view
                showNoteList()
            } else { // not split view
                hideNoteList()
            }
        }
    }

    fun hideNoteList() {
        center = noteView
    }

    fun showNoteList() {
        center = SplitPane( noteList, noteView )
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
        editor.lookup(".html-editor-cut").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE) }
        nodesToKeepTop.add(editor.lookup(".html-editor-copy"))
        nodesToKeepTop.add(editor.lookup(".html-editor-paste"))
        editor.lookup(".html-editor-paste").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.INSERT) }
        nodesToKeepTop.add(editor.lookup(".html-editor-numbers"))
        nodesToKeepTop.add(editor.lookup(".html-editor-bullets"))
        editor.lookup(".html-editor-bullets").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.LIST) }
        nodesToKeepTop.add(editor.lookup(".html-editor-foreground"))
        editor.lookup(".html-editor-foreground").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.COLOR) }
        nodesToKeepTop.add(editor.lookup(".html-editor-background"))

        nodesToKeepBottom.add(editor.lookup(".font-menu-button"))
        nodesToKeepBottom.add(editor.lookup(".html-editor-bold"))
        editor.lookup(".html-editor-bold").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.BOLD) }
        nodesToKeepBottom.add(editor.lookup(".html-editor-italic"))
        editor.lookup(".html-editor-italic").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.ITALICIZE) }
        nodesToKeepBottom.add(editor.lookup(".html-editor-underline"))
        editor.lookup(".html-editor-underline").addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED) { noteModel.activeNote.value?.addToUndoStack(TextChange.UNDERLINE) }

        toolBar1.items.removeIf { n: Node? -> !nodesToKeepTop.contains(n) }
        toolBar2.items.removeIf { n: Node? -> !nodesToKeepBottom.contains(n) }

        // Add all items onto toolBar1
        val toCopy = ArrayList<Node>()
        toCopy.addAll(toolBar2.items)
        toolBar2.items.clear()
        toolBar1.items.addAll(toCopy)

        toolBar2.isVisible = false // everything from toolBar2 in now in toolBar1
        toolBar2.isManaged = false

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

        editor.addEventFilter(KeyEvent.KEY_PRESSED) { event ->
            if (event.isMetaDown && KeyCode.Z == event.code) {
                event.consume()
                println("consumed event = $event")
            }
            else if (event.code == KeyCode.BACK_SPACE || event.code == KeyCode.DELETE) {
                event.consume()
                noteModel.activeNote.value?.emptyRedo()
                noteModel.activeNote.value?.addToUndoStack(TextChange.DELETE)
            }
        }

        editor.isVisible = true
    }
}