// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import notes.shared.Constants
import notes.shared.database.NoteDatabase
import notes.shared.database.PreferenceDatabase
import notes.shared.preferences.Preferences
import notes.shared.preferences.Theme
import notes.shared.webserviceclient.WebServiceClient

class Model {
    private val noteDatabase = PreferenceDatabase()
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
        val noteList = getNotesFromWebService()

        for ( note in noteList ) {
            notes.add( note )
        }

        //idCounter = noteDatabase.getMaxId()
        idCounter = notes.maxByOrNull { it.id }?.id ?: 0
    }

    fun setActiveNote( note: NoteData? ) {
        // save changes to old note in database
        if ( activeNote.value != null ) {
            //noteDatabase.updateNote( activeNote.value!! )
            webServiceClient.put(activeNote.value!!.id, activeNote.value!!.toJson())
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

        // save new note to database
        //noteDatabase.insertNote( newNote )
        webServiceClient.post( newNote.toJson() )
    }

    fun deleteNote() {
        activeNote.value?.let {
            println("Deleting active note titled '${it.title}'")
            var curIndex = notes.indexOf( it.value )
            if ( curIndex > 0 ) curIndex -= 1
            notes.remove( it )
            //noteDatabase.deleteNote( it )
            webServiceClient.delete( it.id )
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
        noteDatabase.savePreferences( x, y, width, height, dividerPos, isListCollapsed, theme )
    }

    fun getPreferences(): Preferences {
        return noteDatabase.getPreferences()
    }

    fun saveNoteToDatabase( note: NoteData ) {
        webServiceClient.put(note.id, note.toJson())
    }

    fun jsonToNote(json: String): NoteData {
        return NoteData.deserializeNote(json)
    }

    fun getNoteFromWebService(id: Long): NoteData {
        val string = webServiceClient.get(id)
        return if (string.isNotEmpty()) NoteData.deserializeNote(string) else NoteData(-1, "error")
    }

    fun getNotesFromWebService(): List<NoteData> {
        val string = webServiceClient.get()
        return NoteData.deserializeNoteList(string)
    }

    fun postNoteToWebService(note: NoteData) {
        webServiceClient.post(note.toJson())
    }

    fun putNoteToWebService(note: NoteData) {
        webServiceClient.put(note.id, note.toJson())
    }

    fun deleteNoteFromWebService(id: Int) {
        webServiceClient.delete(id)
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