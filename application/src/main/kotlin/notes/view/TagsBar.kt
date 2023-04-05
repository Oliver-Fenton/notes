// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.view

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import notes.shared.model.Model
import notes.shared.model.NoteData
import notes.shared.Constants

class TagsBar(noteModel: Model, noteList: NoteList) : HBox() {
    var tagsBar = this

    inner class TagButton(activeNote: SimpleObjectProperty<NoteData?>, tag: String, noteList: NoteList, noteModel: Model) : HBox() {
        var tagImageView = ImageView(Image("Close-Icon.png"))

        var tagText = Label(tag).apply {
            style = "-fx-background: white;"
        }
        init {
            this.id = "TagButton"
            tagImageView.maxHeight(10.0)
            tagImageView.maxWidth(10.0)
            tagImageView.isPreserveRatio = true

            var textBackgroundColor = if (Constants.theme == "light") "white" else "black"
            tagText.apply {
                style = "-fx-background: $textBackgroundColor;"
            }

            var backgroundColor = if (Constants.theme == "light") "white" else "black"
            var borderColor = if (Constants.theme == "light") "#BEBEBE" else "#5A5A5A"
            this.style =
                        "-fx-padding: 3;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-color: $borderColor;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-color: $backgroundColor;"
            this.prefHeight = 10.0
            this.children.addAll(tagText, tagImageView)

            tagsBar.children.add(this)

            this.setOnMouseClicked { event->
                tagsBar.children.remove(this)
                activeNote.value?.removeTag(tag)
                noteList.refreshList(noteModel.notes)
            }

        }
    }

    inner class AddTagTextField(activeNote: SimpleObjectProperty<NoteData?>, noteList: NoteList, noteModel: Model) : HBox() {
        var confirmImageView = ImageView(Image("Checkmark-Icon.png"))
        var confirmButton = Button()
        var tagInput = TextField()
        init {
            this.id = "TagTextField"
            tagInput.promptText = "add tag"
            confirmImageView.fitHeight = 10.0
            confirmImageView.isPreserveRatio = true
            confirmButton.graphic = confirmImageView
            confirmButton.apply {
                style = "-fx-background-color: white;" +
                        "-fx-border-color: #BEBEBE;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 4;"
            }
            this.children.addAll(tagInput, confirmButton)

            confirmButton.setOnAction {
                if (tagInput.text != "") {
                    tagsBar.TagButton(activeNote, tagInput.text.trim(), noteList, noteModel)
                    activeNote.value?.addTag(tagInput.text.trim())
                    noteList.refreshList(noteModel.notes)
                    tagInput.text = ""
                }
            }
            this.setOnKeyReleased { event ->
                if (event.code == KeyCode.ENTER) {
                    if (tagInput.text != "") {
                        tagsBar.TagButton(activeNote, tagInput.text.trim(), noteList, noteModel)
                        activeNote.value?.addTag(tagInput.text.trim())
                        noteList.refreshList(noteModel.notes)
                        tagInput.text = ""
                    }
                }
            }
        }
    }

    fun setTagsBarColors() {

        for (node in this.children) {
            if (node.id == "TagTextField") {
                if (node is AddTagTextField && Constants.theme == "light") {
                    node.confirmImageView = ImageView(Image("Checkmark-Icon.png"))
                    node.confirmImageView.fitHeight = 10.0
                    node.confirmImageView.isPreserveRatio = true
                    node.confirmButton.graphic = node.confirmImageView
                    node.confirmButton.style = "-fx-background: white;"
                } else if (node is AddTagTextField && Constants.theme == "dark") {
                    node.confirmImageView = ImageView(Image("Dark-Checkmark-Icon.png"))
                    node.confirmImageView.fitHeight = 10.0
                    node.confirmImageView.isPreserveRatio = true
                    node.confirmButton.graphic = node.confirmImageView
                    node.confirmButton.style = "-fx-background: black;"
                }

            } else if (node.id == "TagButton") {
                if (node is TagButton && Constants.theme == "light") {

                    node.tagImageView = ImageView(Image("Close-Icon.png"))
                    node.tagText.apply { style = "-fx-background: white;" }
                    node.style =
                        "-fx-padding: 3;" +
                                "-fx-border-width: 1;" +
                                "-fx-border-color: #BEBEBE;" +
                                "-fx-border-radius: 4;" +
                                "-fx-background-color: white;"
                } else if (node is TagButton && Constants.theme == "dark") {

                    node.tagImageView = ImageView(Image("Dark-Close-Icon.png"))
                    node.tagText.apply { style = "-fx-background: black;" }
                    node.style =
                        "-fx-padding: 3;" +
                                "-fx-border-width: 1;" +
                                "-fx-border-color: #5A5A5A;" +
                                "-fx-border-radius: 4;" +
                                "-fx-background-color: black;"

                }
            }
        }
    }

    init {
        this.maxHeight = 20.0
        this.minHeight = 12.0
        this.spacing = 10.0

        this.AddTagTextField(noteModel.activeNote, noteList, noteModel).prefHeightProperty().bind(this.heightProperty())
        this.apply{
            HBox.setHgrow(this.AddTagTextField(noteModel.activeNote, noteList, noteModel), Priority.ALWAYS)

        }
        noteModel.activeNote.addListener { _, _, newActiveNote ->
            if (newActiveNote != null)
            {
                val tagList = newActiveNote.getAllTags()
                println(newActiveNote.getNoteTitle())
                while (tagsBar.children.size > 1) {
                    tagsBar.children.remove(tagsBar.children.last())
                }
                if (tagList != null) {
                    for (n in tagList) {
                        TagButton(noteModel.activeNote, n, noteList, noteModel)
                    }
                }
            }

        }
        this.children.add( 0, tagsBar.AddTagTextField(noteModel.activeNote, noteList, noteModel))
        this.AddTagTextField(noteModel.activeNote, noteList, noteModel).setBackground(null)
    }

}