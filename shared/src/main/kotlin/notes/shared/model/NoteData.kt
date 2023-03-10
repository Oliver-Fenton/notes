package notes.shared.model

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import notes.shared.SysInfo
import org.jsoup.Jsoup
import java.time.LocalDateTime

enum class TextChange {
    INSERT,
    DELETE,
    ITALICIZE,
    UNITALICIZE,
    UNDERLINE,
    UNUNDERLINE,
    BOLD,
    UNBOLD,
    LIST,
    UNLIST,
    COLOR,
    UNCOLOR
}

class NoteData(val id: Int, var title: String): ObservableObjectValue<NoteData?> {
    private var changeListeners = mutableListOf<ChangeListener<in NoteData>?>()
    private var invalidationListeners = mutableListOf<InvalidationListener?>()

    var body = ""
    var dateCreated = SysInfo.curTime
    var dateEdited = SysInfo.curTime
    var isActive = false
    var isDisplay = true

    var undoStack = ArrayList<Pair<TextChange, String>>()
    var redoStack = ArrayList<Pair<TextChange, String>>()

    constructor( id: Int, title: String, body: String, dateCreated: String, dateEdited: String ) : this( id, title ) {
        this.body = body
        this.dateCreated = LocalDateTime.parse( dateCreated )
        this.dateEdited = LocalDateTime.parse( dateEdited )
    }

    override fun addListener(listener: ChangeListener<in NoteData?>?) { changeListeners.add(listener) }

    override fun addListener(listener: InvalidationListener?) { invalidationListeners.add(listener) }

    override fun removeListener(listener: ChangeListener<in NoteData?>?) { changeListeners.remove(listener) }

    override fun removeListener(listener: InvalidationListener?) { invalidationListeners.remove(listener) }

    override fun getValue(): NoteData { return this }

    override fun get(): NoteData { return this }

    fun setNoteTitle(newTitle: String) {
        title = newTitle
        dateEdited = SysInfo.curTime

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun setNoteBody(newBody: String) {
        this.body = newBody
        dateEdited = SysInfo.curTime

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun getHTML(): String { return body }

    fun getText(): String { return Jsoup.parse( getHTML() ).text() }

    fun getPreview(): String { return getText().take(100) }

    fun getDateCreated(): String { return dateCreated.toString() }

    fun getDateEdited(): String { return dateEdited.toString() }

    fun setActive() {
        isActive = true

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun setInactive() {
        isActive = false

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun doDisplay() {
        isDisplay = true
    }

    fun notDisplay() {
        isDisplay = false
    }

    fun undo(): String? {
        if (undoStack.isNotEmpty()) {
            val action = undoStack.last()
            undoStack.removeLast()

            when (action.first) {
                TextChange.INSERT -> {
                    redoStack.add(Pair(TextChange.DELETE, getHTML()))
                }

                TextChange.DELETE -> {
                    redoStack.add(Pair(TextChange.INSERT, getHTML()))
                }

                TextChange.ITALICIZE -> {
                    redoStack.add(Pair(TextChange.UNITALICIZE, getHTML()))
                }

                TextChange.UNITALICIZE -> {
                    redoStack.add(Pair(TextChange.ITALICIZE, getHTML()))
                }

                TextChange.BOLD -> {
                    redoStack.add(Pair(TextChange.UNBOLD, getHTML()))
                }

                TextChange.UNBOLD -> {
                    redoStack.add(Pair(TextChange.BOLD, getHTML()))
                }

                TextChange.UNDERLINE -> {
                    redoStack.add(Pair(TextChange.UNUNDERLINE, getHTML()))
                }

                TextChange.UNUNDERLINE -> {
                    redoStack.add(Pair(TextChange.UNDERLINE, getHTML()))
                }

                TextChange.LIST -> {
                    redoStack.add(Pair(TextChange.UNLIST, getHTML()))
                }

                TextChange.UNLIST -> {
                    redoStack.add(Pair(TextChange.LIST, getHTML()))
                }

                TextChange.COLOR -> {
                    redoStack.add(Pair(TextChange.UNCOLOR, getHTML()))
                }

                TextChange.UNCOLOR -> {
                    redoStack.add(Pair(TextChange.COLOR, getHTML()))
                }
            }
            setNoteBody(action.second)
            return action.second
        }
        return null
    }

    fun addToUndoStack(type: TextChange) {
        undoStack.add(Pair(type, getHTML()))
    }

    fun redo(): String? {
        if (redoStack.isNotEmpty()) {
            val action = redoStack.last()
            redoStack.removeLast()

            when (action.first) {
                TextChange.INSERT -> {
                    undoStack.add(Pair(TextChange.DELETE, getHTML()))
                }

                TextChange.DELETE -> {
                    undoStack.add(Pair(TextChange.INSERT, getHTML()))
                }

                TextChange.ITALICIZE -> {
                    undoStack.add(Pair(TextChange.UNITALICIZE, getHTML()))
                }

                TextChange.UNITALICIZE -> {
                    undoStack.add(Pair(TextChange.ITALICIZE, getHTML()))
                }

                TextChange.BOLD -> {
                    undoStack.add(Pair(TextChange.UNBOLD, getHTML()))
                }

                TextChange.UNBOLD -> {
                    undoStack.add(Pair(TextChange.BOLD, getHTML()))
                }

                TextChange.UNDERLINE -> {
                    undoStack.add(Pair(TextChange.UNUNDERLINE, getHTML()))
                }

                TextChange.UNUNDERLINE -> {
                    undoStack.add(Pair(TextChange.UNDERLINE, getHTML()))
                }

                TextChange.LIST -> {
                    undoStack.add(Pair(TextChange.UNLIST, getHTML()))
                }

                TextChange.UNLIST -> {
                    undoStack.add(Pair(TextChange.LIST, getHTML()))
                }

                TextChange.COLOR -> {
                    undoStack.add(Pair(TextChange.UNCOLOR, getHTML()))
                }

                TextChange.UNCOLOR -> {
                    undoStack.add(Pair(TextChange.COLOR, getHTML()))
                }
            }
            setNoteBody(action.second)
            return action.second
        }
        return null
    }

    fun emptyRedo() {
        redoStack.clear()
    }
}