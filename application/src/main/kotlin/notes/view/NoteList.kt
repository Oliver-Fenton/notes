// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment
import notes.shared.Constants
import notes.shared.model.Model
import notes.shared.model.NoteData

class NoteList(val noteModel: Model): VBox() {

    inner class NotePreview(noteData: NoteData): VBox() {

        fun formatNoteTitle(noteData: NoteData, node: HBox) {
            node.children.clear()
            var notePin = Label(noteData.getPin())
            notePin.style = "-fx-font-size: 19px"
            notePin.textAlignment = TextAlignment.CENTER
            var noteTitle =  Label(noteData.getNoteTitle())
            noteTitle.style = "-fx-font-weight: bold;" + "-fx-font-family: Arial;"
            noteTitle.textAlignment = TextAlignment.CENTER
            node.alignment = Pos.CENTER
            node.children.addAll(notePin, noteTitle)
        }

        val title = HBox().apply {
            HBox.setHgrow(this, Priority.NEVER)
            formatNoteTitle(noteData, this)
        }

        val date = Label( noteData.getDateEdited()).apply {
            HBox.setHgrow(this, Priority.NEVER)
            minWidth = Label.USE_PREF_SIZE
            style = "-fx-font-family: Arial;"
        }
        val filler = Label( "  ").apply {
            HBox.setHgrow(this, Priority.NEVER)
        }
        val preview = Label( noteData.getPreview() ).apply {
            HBox.setHgrow(this, Priority.NEVER)
        }
        val tags = Label(formatTagDisplay(noteData.getTheTags())).apply {
            HBox.setHgrow(this, Priority.NEVER)
        }

        val dateAndPreview = HBox(date, filler, preview).apply {
            HBox.setMargin(date, Insets(0.0, 0.0, 0.0, 0.0))
        }
        val titleHBox = HBox(title).apply {
            HBox.setMargin(date, Insets(0.0, 0.0, 0.0, 0.0))
            style = "-fx-font-weight: bold;"
        }

        val tagsHBox = HBox(tags).apply {
            HBox.setMargin(date, Insets(0.0, 0.0, 0.0, 0.0))
        }

        fun formatTitleDisplay(noteData: NoteData): String {
            return noteData.getPin() + noteData.getNoteTitle()
        }

        fun formatTagDisplay(previewTags: ObservableList<String>) : String {
            var tagDisplay = ""

            for (tag in previewTags) {
                tagDisplay += "#"
                tagDisplay += tag
                if (previewTags.indexOf(tag) < previewTags.size - 1) {
                    tagDisplay += ", "
                }
            }

            return tagDisplay
        }
        fun refresh(noteData: NoteData) {
            //title.text = noteData.title
            // title.text = formatTitleDisplay(noteData)
            formatNoteTitle(noteData, title)
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
                    println("Note Data Selected")
                    noteModel.setPrepData(noteData)
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
            if (noteData.isDisplay) {
                if (noteData.isPinned) {
                    var notePreview = NotePreview(noteData)

                    if (noteData.isActive) {
                        notePreview.setToActiveColor()
                    } else {
                        notePreview.setToInactiveColor()
                    }
                    children.add(notePreview)
                }
            }
        }

        for (noteData in noteList.reversed()) {
            if (noteData.isDisplay && !noteData.isPinned) {
                var notePreview = NotePreview(noteData)

                if (noteData.isActive) {
                    notePreview.setToActiveColor()
                } else {
                    notePreview.setToInactiveColor()
                }
                children.add(notePreview)
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