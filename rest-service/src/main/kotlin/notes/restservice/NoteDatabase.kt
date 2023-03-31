// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.restservice

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

/*

DROP TABLE note_data;

CREATE TABLE note_data (
  id LONG PRIMARY KEY,
  title TEXT,
  body TEXT,
  dateCreated TEXT,
  dateEdited TEXT
);

INSERT INTO note_data (id, title, body, dateCreated, dateEdited)
    VALUES(1,'note #1','1 1 1 1 1','2023-03-09T14:30:00.123456789','2023-03-09T14:30:00.123456789');
INSERT INTO note_data (id, title, body, dateCreated, dateEdited)
    VALUES(2,'note #2','2 2 2 2 2','2023-03-09T14:30:00.123456789','2023-03-09T14:30:00.123456789');

 */

class NoteDatabase {
    fun getNotes(): JsonArray {
        return database.getNotes()
    }

    fun insertNote(note: JsonObject) {
        database.insertNote( note )
    }

    fun updateNote( id: Int, note: JsonObject ) {
        database.updateNote( id, note )
    }

    fun deleteNote( id: Int ) {
        database.deleteNote( id )
    }

    private val database = Database()

    private class Database {
        init {
            createTableIfNotExists()
        }
        private fun connect(): Connection? {
            var conn: Connection? = null
            try {
                val url = "jdbc:sqlite:./WebServiceNoteDatabase.db"
                conn = DriverManager.getConnection( url )
                println( "Connection to SQLite has been established.")
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return conn
        }

        private fun createTableIfNotExists() {
            val createNoteDataTableIfNotExistsSQL = "CREATE TABLE IF NOT EXISTS note_data ( id INTEGER PRIMARY KEY, title TEXT, body TEXT, dateCreated TEXT, dateEdited TEXT );"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                println("Creating note_data table in WebServiceNoteDatabase if it does not exist.")
                val createNoteDataTable = conn.prepareStatement( createNoteDataTableIfNotExistsSQL )
                createNoteDataTable.executeUpdate()
                createNoteDataTable.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }

        fun getNotes(): JsonArray {
            val getNotesSQL = "SELECT * FROM note_data;"
            val jsonArray = JsonArray()

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return jsonArray
            }

            try {
                val preparedStatement = conn.prepareStatement( getNotesSQL )
                val rs: ResultSet = preparedStatement.executeQuery()
                while ( rs.next() ) {
                    val id = rs.getInt("id")
                    val title = rs.getString("title")
                    val body = rs.getString("body")
                    val dateCreated = rs.getString("dateCreated")
                    val dateEdited = rs.getString("dateEdited")

                    val json = JsonObject()
                    json.addProperty("id", id)
                    json.addProperty("title", title)
                    json.addProperty("body", body)
                    json.addProperty("dateCreated", dateCreated)
                    json.addProperty("dateEdited", dateEdited)

                    jsonArray.add(json)
                }
                rs.close()
                preparedStatement.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return jsonArray
        }

        fun insertNote( note: JsonObject ) {
            val id = note["id"].asInt
            val title = note["title"].asString
            val body = note["body"].asString
            val dateCreated = note["dateCreated"].asString
            val dateEdited = note["dateEdited"].asString

            val insertNoteSQL = "INSERT INTO note_data (id, title, body, dateCreated, dateEdited) VALUES ('$id', '$title', '$body', '$dateCreated', '$dateEdited');"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( insertNoteSQL )
                preparedStatement.executeUpdate()
                println("Added note to database.")
            } catch ( e: SQLException ) {
                println( e.message )
            }

        }

        fun updateNote( id: Int, note: JsonObject ) {
            val noteId = note["id"].asInt
            val title = note["title"].asString
            val body = note["body"].asString
            val dateEdited = note["dateEdited"].asString

            val updateNoteSQL = "UPDATE note_data SET title = '$title', body = '$body', dateEdited = '$dateEdited' WHERE id = $id;"

            require( id == noteId )

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( updateNoteSQL )
                preparedStatement.executeUpdate()
                println("Updated note titled $title} in database.")
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }

        fun deleteNote( id: Int ) {
            val deleteNoteSQL = "DELETE FROM note_data WHERE id = $id;"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( deleteNoteSQL )
                preparedStatement.executeUpdate()
                println("Deleted note with id '$id' in database.")
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }
    }

}