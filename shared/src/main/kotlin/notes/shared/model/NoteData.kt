// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.model

import com.google.gson.*
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableObjectValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.ToolBar
import javafx.scene.input.KeyCode
import notes.shared.Constants
import notes.shared.SysInfo
import org.jsoup.Jsoup
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

class LocalDateTimeTypeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        return LocalDateTime.parse(json?.asString, formatter)
    }
}

class NoteDataTypeAdapter: JsonSerializer<NoteData>, JsonDeserializer<NoteData> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): NoteData {
        val jsonObject: JsonObject? = json?.asJsonObject
        val id = jsonObject?.get("id")?.asInt ?: throw JsonParseException("id is missing")
        val title = jsonObject.get("title").asString
        val body = jsonObject.get("body").asString
        val dateCreated = context?.deserialize(jsonObject.get("dateCreated"), LocalDateTime::class.java)
            ?: LocalDateTime.now()
        val dateEdited = context?.deserialize(jsonObject.get("dateEdited"), LocalDateTime::class.java)
            ?: LocalDateTime.now()
        val tagsArray = jsonObject?.get("tags")?.asJsonArray ?: JsonArray()
        val tagsList = tagsArray.map { it.asString }

        return NoteData(id, title).apply {
            this.body = body
            this.dateCreated = dateCreated
            this.dateEdited = dateEdited
            this.tags = FXCollections.observableArrayList(tagsList)
        }
    }

    override fun serialize(src: NoteData?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val json = JsonObject()
        json.addProperty("id", src?.id)
        json.addProperty("title", src?.title)
        json.addProperty("body", src?.body)
        json.addProperty("dateCreated", src?.dateCreated?.format(formatter))
        json.addProperty("dateEdited", src?.dateEdited?.format(formatter))
        json.add("tags", context?.serialize(src?.tags))

        return json
    }
}

class NoteData(public val id: Int, public var title: String): ObservableObjectValue<NoteData?> {
    private var changeListeners = mutableListOf<ChangeListener<in NoteData>?>()
    private var invalidationListeners = mutableListOf<InvalidationListener?>()
    var tags: ObservableList<String> = FXCollections.observableArrayList()

    var body = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>\n"
    var dateCreated = LocalDateTime.now()
    var dateEdited = LocalDateTime.now()
    var isActive = false
    var isDisplay = true
    // IGNORE THIS
    var prepData = false
    // ADD THIS
    var isPinned = false

    var undoStack = ArrayList<Pair<TextChange, String>>()
    var redoStack = ArrayList<Pair<TextChange, String>>()

    init {
        addToUndoStack(TextChange.INSERT)
    }

    constructor( id: Int, title: String, body: String, dateCreated: String, dateEdited: String, noteTags: String) : this( id, title ) {
        this.body = body
        this.dateCreated = LocalDateTime.parse( dateCreated )
        this.dateEdited = LocalDateTime.parse( dateEdited )
        val trimmedNoteTags =  noteTags.substring(1, noteTags.length - 1)
        if (trimmedNoteTags != "") {
            val noteTagsArr = trimmedNoteTags.split(", ")
            noteTagsArr.forEach { e ->
                this.tags.add(e)
            }
        }
    }

    companion object {
        fun deserializeNote(json: String): NoteData {
            val gson = GsonBuilder()
                .registerTypeAdapter(NoteData::class.java, NoteDataTypeAdapter())
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()

            return gson.fromJson(json, NoteData::class.java)
        }

        fun deserializeNoteList(jsonArray: String): List<NoteData> {
            val gson = GsonBuilder()
                .registerTypeAdapter(NoteData::class.java, NoteDataTypeAdapter())
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()
            val type: Type = object: TypeToken<List<NoteData>>() {}.type
            return gson.fromJson(jsonArray, type)
        }
    }

    override fun addListener(listener: ChangeListener<in NoteData?>?) { changeListeners.add(listener) }

    override fun addListener(listener: InvalidationListener?) { invalidationListeners.add(listener) }

    override fun removeListener(listener: ChangeListener<in NoteData?>?) { changeListeners.remove(listener) }

    override fun removeListener(listener: InvalidationListener?) { invalidationListeners.remove(listener) }

    override fun getValue(): NoteData { return this }

    override fun get(): NoteData { return this }

    fun setNoteTitle(newTitle: String) {
        if (newTitle == "") {
            title = "New Note"

        } else {
            title = newTitle
        }
        dateEdited = LocalDateTime.now()


        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }
    fun changeNoteTitle(newTitle: String) {
        title = newTitle

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun getNoteTitle(): String { return title }

    fun setNoteBody(newBody: String) { // Update Note Body for front end changes
        if (newBody == this.body) { // ensures htmleditor button click does not update time if no text selected
            return
        }
        this.body = newBody
        dateEdited = LocalDateTime.now()
        setDateHTMLEditor()

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun changeNoteBody(newBody: String) { // Update Note Body but not front end changes
        this.body = newBody

        invalidationListeners.forEach { it?.invalidated(this) }
        changeListeners.forEach { it?.changed(this, this.value, value) }
    }

    fun setDateHTMLEditor() {
        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar

        val date = toolBar2.lookup(".label")
        if(date is Label) {
            if (this.isActive) {
                date.text = this.getDateEdited()
            }
        }


    }

    fun setTitleHTMLEditor() {
        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar
        val title = toolBar2.lookup(".text-field")
        if(title is TextField) {
            if (this.isActive) {
                title.text = this.getNoteTitle()
            }
        }
    }

    fun clearTitleAndDateHTMLEditor() {
        val toolBar2: ToolBar = Constants.notesArea.lookup(".bottom-toolbar") as ToolBar

        // must set title first, then date due to the listener on title (that updates date when title changes)
        val title = toolBar2.lookup(".text-field")
        if(title is TextField) {
            title.text = ""
        }
        val date = toolBar2.lookup(".label")
        if(date is Label) {
            date.text = ""
        }

    }

    fun changeBodyBackgroundColor(backgroundColor: String) : String {
        val beginningStyleIndex = this.body.indexOf("<body style=")
        val endingStyleIndex = this.body.indexOf("contenteditable=")

        if (beginningStyleIndex == -1) { // no style set yet
            var bodyTagIndex = this.body.indexOf("<body")
            var beginningSubstring = this.body.substring(0, bodyTagIndex + 5)
            var endingSubstring = this.body.substring(bodyTagIndex + 5)
            return "$beginningSubstring style=\"background-color: $backgroundColor;\" $endingSubstring"
        }
        else { // has style already
            var beginningSubstring = this.body.substring(0, beginningStyleIndex + 12)
            var endingSubstring = this.body.substring(endingStyleIndex)
            return "$beginningSubstring\"background-color: $backgroundColor;\" $endingSubstring"
        }
    }

    fun getHTML(): String { return body }

    fun getText(): String { return Jsoup.parse( getHTML() ).text() }

    fun getPreview(): String { return getText().take(100) }

    fun getDateCreated(): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy '@' h:mm a")

        return dateCreated.format(formatter)
    }

    fun getDateEdited(): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy '@' h:mm a")

        return dateEdited.format(formatter)
    }

    fun setActive() {
        isActive = true
        val updatedBackgroundHTML = changeBodyBackgroundColor(if (Constants.theme == "light") Constants.LightHTMLEditorColor else Constants.DarkHTMLEditorColor)
        changeNoteBody(updatedBackgroundHTML)
        Constants.notesArea.htmlText = getHTML()


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

    fun getDisplay(): Boolean {
        return isDisplay
    }

    fun pin() {
        this.isPinned = true
    }

    fun removePin() {
        this.isPinned = false
    }

    fun getPin(): String {
        if (this.isPinned) {
            return "\uD83D\uDCCC"
        } else {
            return ""
        }
    }

    fun activePrepData() {
        this.prepData = true
    }

    fun inactivePrepData() {
        this.prepData = false;
    }

    fun getPrep(): Boolean {
        return this.prepData
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

    fun addToUndoStack(type: TextChange, letter: KeyCode? = null) {
        if (letter != null) {
            if (letter == KeyCode.SPACE) {
                undoStack.add(Pair(type, getHTML()))
            }
        }
        else {
            undoStack.add(Pair(type, getHTML()))
        }

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

    fun getAllTags(): ObservableList<String> {
        return tags
    }

    fun addTag(input: String) {
        tags.add(input)
    }

    fun removeTag(input: String) {
        tags.remove(input)
    }

    fun getTheTags(): ObservableList<String> {
        val previewTags: ObservableList<String> = FXCollections.observableArrayList()

        for (i in 0 until tags.size) {
            previewTags.add(tags[i])
        }

        return previewTags
    }


    fun toJson(): String {
        val gson = GsonBuilder()
            .registerTypeAdapter(NoteData::class.java, NoteDataTypeAdapter())
            .create()

        return gson.toJson(this)
    }
}