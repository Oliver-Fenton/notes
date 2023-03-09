package notes.shared.model

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import notes.shared.SysInfo
import org.jsoup.Jsoup

class NoteData(private var title: String): ObservableObjectValue<NoteData?> {
    private var changeListeners = mutableListOf<ChangeListener<in NoteData>?>()
    private var invalidationListeners = mutableListOf<InvalidationListener?>()

    private var body = ""
    val dateCreated = SysInfo.curTime
    var dateEdited = SysInfo.curTime
    var isActive = false

    constructor(title: String, body: String) : this(title) {
        this.body = body
    }

    override fun addListener(listener: ChangeListener<in NoteData?>?) { changeListeners.add(listener) }

    override fun addListener(listener: InvalidationListener?) { invalidationListeners.add(listener) }

    override fun removeListener(listener: ChangeListener<in NoteData?>?) { changeListeners.remove(listener) }

    override fun removeListener(listener: InvalidationListener?) { invalidationListeners.remove(listener) }

    override fun getValue(): NoteData { return this }

    override fun get(): NoteData { return this }

    fun setTitle(newTitle: String) {
        title = newTitle
        dateEdited = SysInfo.curTime

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun getTitle(): String { return title }

    fun setBody(newBody: String) {
        this.body = newBody
        dateEdited = SysInfo.curTime

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun getHTML(): String { return body }

    private fun getText(): String { return Jsoup.parse( getHTML() ).text() }

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
}