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

    fun saveWindowPosition( x: Double, y: Double, width: Double, height: Double ) {
        database.saveWindowPos( x, y, width, height )
    }

    fun getWindowPosition(): Pair< Pair<Double,Double>, Pair<Double,Double> > {
        return database.getWindowPosition()
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
            val createNoteDataTableIfNotExistsSQL = "CREATE TABLE IF NOT EXISTS note_data ( id INTEGER PRIMARY KEY, title TEXT, body TEXT, dateCreated TEXT, dateEdited TEXT );"
            val createPreferencesTableIfNotExistsSQL = "CREATE TABLE IF NOT EXISTS preferences ( id INTEGER PRIMARY KEY, x REAL, y REAL, width REAL, height REAL);"
            val setDefaultPreferencesIfTableEmptySQL = "INSERT INTO preferences (x, y, width, height) VALUES (0.0, 0.0, 600.0, 400.0) WHERE NOT EXISTS (SELECT 1 FROM preferences);"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val createNoteDataTable = conn.prepareStatement( createNoteDataTableIfNotExistsSQL )
                createNoteDataTable.executeQuery()
                createNoteDataTable.close()
                val createPreferencesTable = conn.prepareStatement( createPreferencesTableIfNotExistsSQL )
                createPreferencesTable.executeQuery()
                createPreferencesTable.close()
                val insertDefaultPreferences = conn.prepareStatement( setDefaultPreferencesIfTableEmptySQL )
                insertDefaultPreferences.executeQuery()
                insertDefaultPreferences.close()
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
            var maxId = 0

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

        fun saveWindowPos( x: Double, y: Double, width: Double, height: Double ) {
            val saveWindowPosSQL = "UPDATE preferences SET x = $x, y = $y, width = $width, height = $height WHERE id = 1;"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( saveWindowPosSQL )
                preparedStatement.executeUpdate()
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }

        fun getWindowPosition(): Pair< Pair<Double,Double>, Pair<Double,Double> > {
            val getWindowPosSQL = "SELECT * FROM preferences"
            var coordinates = Pair(0.0, 0.0)
            var dimensions = Pair(600.0, 400.0)

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return Pair( coordinates, dimensions )
            }

            try {
                val preparedStatement = conn.prepareStatement( getWindowPosSQL )
                val rs: ResultSet = preparedStatement.executeQuery()
                if ( rs.next() ) {
                    val x = rs.getDouble("x")
                    val y = rs.getDouble("y")
                    val width = rs.getDouble("width")
                    val height = rs.getDouble("height")
                    coordinates = Pair( x, y )
                    dimensions = Pair( width, height )
                }
                rs.close()
                preparedStatement.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return Pair( coordinates, dimensions )
        }
    }

}