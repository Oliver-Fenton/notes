package notes.application
import javafx.scene.control.ListView
import javafx.scene.layout.StackPane


class temp(data: NoteData, noteView: NoteView): StackPane() {
    val names = data.preview
    val notesList = ListView(names)

    init {
        notesList.setOnMouseClicked {
            data.setActiveIndex(notesList.getSelectionModel().getSelectedIndex())
            if (data.getActiveIndex() + 1 > data.listOfNotes.size) {
                data.listOfNotes.add(NoteTemplate(""))
            }
            noteView.setTextArea(data.getNoteBody())
            println("active:" + data.getActiveIndex())
        }
        this.children.add(notesList)
    }

}