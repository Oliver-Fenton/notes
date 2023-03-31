// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared

import javafx.scene.web.HTMLEditor


object Constants {

    val notesArea = HTMLEditor()

    var theme = "light"

    // Dark Theme Colors
    val DarkActiveNoteColor = "#4F4F4F"
    val DarkInactiveNoteColor = "#929292"
    val DarkHTMLEditorColor = "gray"
    val DarkNoteListBackgroundColor = "black"
    val DarkToolbarColor = "#4E4E4E"

    // Light Theme Colors
    val LightActiveNoteColor = "#67E1CB"
    val LightInactiveNoteColor = "#EBECF0"
    val LightHTMLEditorColor = "white"
    val LightNoteListBackgroundColor = "white"
    val LightToolbarColor = "#86ECDA"

}