// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import notes.shared.database.PreferenceDatabase
import notes.shared.preferences.Preferences
import notes.shared.preferences.Theme
import notes.shared.webserviceclient.WebServiceClient
import java.lang.Exception

class Model {
    private val preferenceDatabase = PreferenceDatabase()
    private val webServiceClient = WebServiceClient()
    var isSplitView = SimpleBooleanProperty( true )
    val activeNote = SimpleObjectProperty<NoteData?>(null)
    val prepNote = SimpleObjectProperty<NoteData?>(null)
    var notes: ObservableList<NoteData> = FXCollections.observableArrayList()
    var allTags: Array<String> = arrayOf<String>()

    private var idCounter: Int = 0

    /*
     * load notes from database
     */
    init {
        //val noteList = noteDatabase.getNotes()
        var noteList: List<NoteData> = emptyList()
        try {
            noteList = getNotesFromWebService()
        } catch (_: Exception) {
            println("ERROR: unable to fetch notes from web service!")
        }

        for ( note in noteList ) {
            notes.add( note )
        }

        idCounter = notes.maxByOrNull { it.id }?.id ?: 0
    }

    fun setActiveNote( note: NoteData? ) {
        // save changes to old note in database
        if ( activeNote.value != null ) {
            putNoteToWebService( activeNote.value!! )
        }

        // deactivate old note
        activeNote.value?.setInactive()

        // activate new note
        activeNote.set( note )
        note?.setActive()

        activeNote.value?.setDateHTMLEditor()
        activeNote.value?.setTitleHTMLEditor()
    }

    fun setPrepData( note: NoteData? ) {
        prepNote.value?.inactivePrepData()
        prepNote.set(note)
        note?.activePrepData()
    }

    fun createNote(title: String? = null) {
        idCounter++
        val newNote = NoteData(idCounter, title ?: "New Note #$idCounter")
        newNote.addToUndoStack(TextChange.INSERT)
        notes.add( newNote )
        setActiveNote( newNote )

        // save new note to web service database
        postNoteToWebService( newNote )
    }

    fun deleteNote() {
        activeNote.value?.let {
            println("Deleting active note titled '${it.title}'")
            var curIndex = notes.indexOf( it.value )
            if ( curIndex > 0 ) curIndex -= 1
            notes.remove( it )
            deleteNoteFromWebService( it.id )
            if ( notes.isNotEmpty() ) { setActiveNote( notes[curIndex] ) }
            else {
                activeNote.value?.clearTitleAndDateHTMLEditor()
                setActiveNote( null )
                Constants.notesArea.isDisable = true

                val htmlEditorTheme = if (Constants.theme == "light") Constants.LightHTMLEditorColor else Constants.DarkHTMLEditorColor
                // Update html background color
                Constants.notesArea.htmlText = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body></html>\n"
                val beginningStyleIndex = Constants.notesArea.htmlText.indexOf("<body style=")
                val endingStyleIndex = Constants.notesArea.htmlText.indexOf("contenteditable=")

                if (beginningStyleIndex == -1) { // no style set yet
                    var bodyTagIndex = Constants.notesArea.htmlText.indexOf("<body")
                    var beginningSubstring = Constants.notesArea.htmlText.substring(0, bodyTagIndex + 5)
                    var endingSubstring = Constants.notesArea.htmlText.substring(bodyTagIndex + 5)
                    Constants.notesArea.htmlText = "$beginningSubstring style=\"background-color: $htmlEditorTheme;\" $endingSubstring"
                }
                else { // has style already
                    var beginningSubstring = Constants.notesArea.htmlText.substring(0, beginningStyleIndex + 12)
                    var endingSubstring = Constants.notesArea.htmlText.substring(endingStyleIndex)
                    Constants.notesArea.htmlText = "$beginningSubstring\"background-color: $htmlEditorTheme;\" $endingSubstring"
                }
            }
        }
    }

    fun savePreferences(x: Double, y: Double, width: Double, height: Double, dividerPos: Double, isListCollapsed: Boolean, theme: Theme ) {
        preferenceDatabase.savePreferences( x, y, width, height, dividerPos, isListCollapsed, theme )
    }

    fun getPreferences(): Preferences {
        return preferenceDatabase.getPreferences()
    }

    fun getNoteFromWebService(id: Long): NoteData {
        var string = ""
        try {
            string = webServiceClient.get(id)
        } catch (e: Exception) {
            println("ERROR: could not connect to web service to fetch note!")
        }
        return if (string.isNotEmpty()) NoteData.deserializeNote(string) else NoteData(-1, "error")
    }

    fun getNotesFromWebService(): List<NoteData> {
        var string = ""
        try {
            string = webServiceClient.get()
        } catch (e: Exception) {
            println("ERROR: could not connect to web service to fetch notes!")
        }
        return NoteData.deserializeNoteList(string)
    }

    fun postNoteToWebService(note: NoteData) {
        try {
            webServiceClient.post(note.toJson())
        } catch (e: Exception) {
            println("ERROR: could not connect to web service to insert note!")
        }
    }

    fun putNoteToWebService(note: NoteData) {
        try {
            webServiceClient.put(note.id, note.toJson())
        } catch (e: Exception) {
            println("ERROR: could not connect to web service to update note!")
        }
    }

    fun deleteNoteFromWebService(id: Int) {
        try {
            webServiceClient.delete(id)
        } catch (e: Exception) {
            println("ERROR: could not connect to web service to delete note!")
        }
    }

    fun sortDate(reverseOrder: Boolean) {
        var sortedNotes = notes.sortedWith(compareBy{ it.getDateCreated()})
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()
        }
        notes.clear()

        sortedNotes.forEach {element ->
            if (element.isPinned) {
                notes.add(element)
            }
        }
        sortedNotes.forEach {element ->
            if (!element.isPinned) {
                notes.add(element)
            }
        }
    }

    fun sortDateEdited(reverseOrder: Boolean) {
        var sortedNotes = notes.sortedWith(compareBy{ it.dateEdited})
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()

        }
        notes.clear()

        sortedNotes.forEach {element ->
            if (element.isPinned) {
                notes.add(element)
            }
        }
        sortedNotes.forEach {element ->
            if (!element.isPinned) {
                notes.add(element)
            }
        }
    }

    fun sortAlphaTitle(reverseOrder: Boolean) {
        var sortedNotes = notes.sortedWith(compareBy{ it.getNoteTitle()})
        if (reverseOrder) {
            sortedNotes = sortedNotes.reversed()
        }
        notes.clear()

        sortedNotes.forEach {element ->
            if (element.isPinned) {
                notes.add(element)
            }
        }
        sortedNotes.forEach {element ->
            if (!element.isPinned) {
                notes.add(element)
            }
        }
    }

    fun trimTags(currTag: String) {
        for (n in notes) {
            if (!n.getDisplay()) {
                continue
            }else if (this.allTags.contains(currTag)) {
                n.doDisplay()
            } else {
                n.notDisplay()
            }
        }
    }
}