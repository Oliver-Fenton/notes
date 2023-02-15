package notes.application

class NoteTemplate(noteBody: String) {
    var noteTitle: String = ""
    var noteBody: String = ""

    init {
        this.noteBody = noteBody
    }
}