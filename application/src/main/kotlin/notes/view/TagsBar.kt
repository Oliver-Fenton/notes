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

class TagsBar(noteModel: Model, noteList: NoteList) : HBox() {
    var tagsBar = this

    inner class TagButton(activeNote: SimpleObjectProperty<NoteData?>, tag: String, noteList: NoteList, noteModel: Model) : HBox() {
        val tagImageView = ImageView(Image("Tag-Close-Icon.png"))
        val tagText = Label(tag).apply {
            style = "-fx-background: white;"
        }
        init {
            tagImageView.maxHeight(10.0)
            tagImageView.maxWidth(10.0)
            tagImageView.isPreserveRatio = true
            this.style =
                        "-fx-padding: 3;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-color: #BEBEBE;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-color: white;"
                      //  "-fx-border-insets: 5;"
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
        val confirmImageView = ImageView(Image("Tag-Add-Icon.png"))
        val confirmButton = Button()
        val tagInput = TextField()
        init {
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
                    tagsBar.TagButton(activeNote, tagInput.text, noteList, noteModel)
                    activeNote.value?.addTag(tagInput.text)
                    noteList.refreshList(noteModel.notes)
                    tagInput.text = ""
                }
            }
            this.setOnKeyReleased { event ->
                if (event.code == KeyCode.ENTER) {
                    if (tagInput.text != "") {
                        tagsBar.TagButton(activeNote, tagInput.text, noteList, noteModel)
                        activeNote.value?.addTag(tagInput.text)
                        noteList.refreshList(noteModel.notes)
                        tagInput.text = ""
                    }
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