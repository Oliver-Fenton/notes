package notes.shared.database

import notes.shared.model.NoteData
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
    VALUES(1,'note #1','blah, blah, blah','2023-03-09T14:30:00.123456789','2023-03-09T14:30:00.123456789');
INSERT INTO note_data (id, title, body, dateCreated, dateEdited)
    VALUES(2,'note #2','blah, blah, blah, blah, blah','2023-03-09T14:30:00.123456789','2023-03-09T14:30:00.123456789');

 */

class NoteDatabase {
    fun getNotes(): List<NoteData> {
        return database.getNotes()
    }

    fun getMaxId(): Int {
        return database.getMaxId()
    }

    fun insertNote( note: NoteData ) {
        database.insertNote( note )
    }

    fun updateNote( note: NoteData ) {
        database.updateNote( note )
    }

    fun deleteNote( note: NoteData ) {
        database.deleteNote( note )
    }

    private val database = Database()

    private class Database {
        init {
            createTableIfNotExists()
        }
        private fun connect(): Connection? {
            var conn: Connection? = null
            try {
                val url = "jdbc:sqlite:./NoteDatabase.db"
                conn = DriverManager.getConnection( url )
                println( "Connection to SQLite has been established.")
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return conn
        }

        private fun createTableIfNotExists() {
            val createTableIfNoteExistsSQL = "CREATE TABLE IF NOT EXISTS note_data ( id INTEGER PRIMARY KEY, title TEXT, body TEXT, dateCreated TEXT, dateEdited TEXT );"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( createTableIfNoteExistsSQL )
                val rs: ResultSet = preparedStatement.executeQuery()
                rs.close()
                preparedStatement.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }

        fun getNotes(): List<NoteData> {
            val getNotesSQL = "SELECT * FROM note_data;"
            val list = mutableListOf<NoteData>()

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return list
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
                    list.add( NoteData( id, title, body, dateCreated, dateEdited ) )
                }
                rs.close()
                preparedStatement.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return list
        }

        fun getMaxId(): Int {
            val getMaxIdSQL = "SELECT MAX(id) FROM note_data;"
            var maxId: Int = 0

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return 0
            }

            try {
                val preparedStatement = conn.prepareStatement( getMaxIdSQL )
                val rs: ResultSet = preparedStatement.executeQuery()
                if (rs.next()) maxId = rs.getInt(1)
                rs.close()
                preparedStatement.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return maxId
        }

        fun insertNote( note: NoteData ) {
            val insertNoteSQL = "INSERT INTO note_data (id, title, body, dateCreated, dateEdited) VALUES ('${note.id}', '${note.title}', '${note.body}', '${note.dateCreated}', '${note.dateEdited}');"

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

        fun updateNote( note: NoteData ) {
            val updateNoteSQL = "UPDATE note_data SET title = '${note.title}', body = '${note.body}', dateEdited = '${note.dateEdited}' WHERE id = ${note.id};"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( updateNoteSQL )
                preparedStatement.executeUpdate()
                println("Updated note titled ${note.title}} in database.")
            } catch ( e: SQLException ) {
                println( e.message )
            }

        }

        fun deleteNote( note: NoteData ) {
            // TODO
        }
    }

}