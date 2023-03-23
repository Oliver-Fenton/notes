package notes.view

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import notes.shared.Constants
import notes.shared.model.Model
import notes.shared.model.NoteData

class NoteList(val noteModel: Model): VBox() {

    inner class NotePreview(noteData: NoteData): VBox() {

        val title = Label( noteData.title ).apply {
            HBox.setHgrow(this, Priority.NEVER)
        }

        val date = Label( noteData.getDateEdited() + " " ).apply {
            minWidth = Label.USE_PREF_SIZE
        }
        val preview = Label( noteData.getPreview() ).apply {
            HBox.setHgrow(this, Priority.NEVER)
        }
        val tags = Label(noteData.getFirstTags()[0] +  noteData.getFirstTags()[1] + noteData.getFirstTags()[2]).apply {
            HBox.setHgrow(this, Priority.NEVER)
        }

        val dateAndPreview = HBox(date, preview).apply {
            HBox.setMargin(date, Insets(0.0, 0.0, 0.0, 0.0))
        }
        val titleHBox = HBox(title).apply {
            HBox.setMargin(date, Insets(0.0, 0.0, 0.0, 0.0))
        }

        val tagsHBox = HBox(tags).apply {
            HBox.setMargin(date, Insets(0.0, 0.0, 0.0, 0.0))
        }

        fun refresh(noteData: NoteData) {
            //title.text = noteData.title
            title.text = noteData.getNoteTitle()
            date.text = noteData.getDateEdited()
            preview.text = noteData.getPreview()
            if (noteData.isActive) {
                setToActiveColor()
            } else {
                setToInactiveColor()
            }
        }

        fun setToInactiveColor() {
            val inactiveBackgroundColor = if (Constants.theme == "light") Constants.LightInactiveNoteColor else Constants.DarkInactiveNoteColor
            style = "-fx-outline: 10;" +
                    "-fx-outline-offset: -20;"+
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 5;" +
                    "-fx-border-insets: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: $inactiveBackgroundColor;" +
                    "-fx-background-origin: padding-box;"
            titleHBox.apply {
                style = "-fx-background-color: $inactiveBackgroundColor;"
            }
            dateAndPreview.apply {
                style = "-fx-background-color: $inactiveBackgroundColor;"
            }
            tagsHBox.apply {
                style = "-fx-background-color: $inactiveBackgroundColor;"
            }
        }

        fun setToActiveColor() {
            val activeBackgroundColor = if (Constants.theme == "light") Constants.LightActiveNoteColor else Constants.DarkActiveNoteColor
            style = "-fx-outline: 10;" +
                    "-fx-outline-offset: -20;"+
                    "-fx-border-style: solid inside;" +
                    "-fx-border-width: 5;" +
                    "-fx-border-insets: 2;" +
                    "-fx-border-radius: 5;" +
                    "-fx-border-color: $activeBackgroundColor;" +
                    "-fx-background-origin: padding-box;"
            titleHBox.apply {
                style = "-fx-background-color: $activeBackgroundColor"
            }
            dateAndPreview.apply {
                style = "-fx-background-color: $activeBackgroundColor"
            }
            tagsHBox.apply {
                style = "-fx-background-color: $activeBackgroundColor"
            }
        }
        init {
            setToInactiveColor()
            children.addAll(titleHBox, dateAndPreview, tagsHBox)

            prefHeight = 55.0 // needs to be set to fit all children so no whitespace shows betwee border

            this.setOnMouseClicked{e ->
                if (e.button == MouseButton.PRIMARY) {
                    noteModel.setActiveNote(noteData)
                    println("Note named ${noteData.title} set as active note with body ${noteData.getHTML()}")
                    println("Note tags ${noteData.getAllTags()}")
                } else if (e.button == MouseButton.SECONDARY) {

                }
            }

            noteData.addListener { _, _, newValue ->
                println("detected a change in note ${newValue?.title}")
                if (newValue != null) refresh(newValue)
            }
        }
    }
    fun refreshList(noteList: ObservableList<NoteData>) {
        children.clear()
        for (noteData in noteList.reversed()) {
            if(noteData.isDisplay) {
                children.add(NotePreview(noteData))
            }
        }
    }
    init {
        minWidth = 200.0
        maxWidth = 500.0

        refreshList( noteModel.notes )

        noteModel.notes.addListener(ListChangeListener {
            println("detected a change in note list")
            refreshList( noteModel.notes )
        })
    }
}