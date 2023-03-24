# CS 346 - Notes Application

## Goal
An application that stores and organizes user's notes according to their specifications while also providing stylization for note text.

## Group 203 (GLORy)
Gillian Johnson  
Lindsay Mark  
Oliver Fenton  
Richard Hua

## Quick-start
Download distZip file and run the executable inside

## Release Notes 1.3.0
* Add and remove tags for each note
* Dark and light themes
* Sorting by date created, date edited, and title
* Tags bar included to display all tags of active note
* Title bar included to change note title
* Default note added
* Date edited updated for note title and body changes
* Hooked application up with REST Web Service
* Undo/redo for entire words vs single characters
* Added unit tests for new features

## Release Notes 1.2.0
* Save and restore window size and position
* Undo and redo text inputs and styles (cut, copy, paste, bold, italics, underline, colour, bullet list, etc.)
* Search note body
* Save and restore note data
* HTML rich text compatible

## Release Notes 1.1.0
* Toolbar with necessary styling elements included (bold, underline, italicize, etc.)
* Menu bar with necessary items and keyboard shortcuts
* Collapsible panel displaying list of existing notes by their title, body preview, and most recent edit timestamp 
* Note editor that displays current content of selected note with changes stored
* Application window is resizable


Supported gradle tasks:

| Tasks   | Description                                          |
|:--------|:-----------------------------------------------------|
| clean   | Remove build/ directory                              |
| build   | Build the application project in build/ directory    |
| run     | Run the application or console project               |
| distZip | Create run scripts in application/build/distribution |
| distTar | Create run scripts in application/build/distribution |

Sources Used:
* https://stackoverflow.com/questions/10075841/how-to-hide-the-controls-of-htmleditor (Commenter: Tag Howard)