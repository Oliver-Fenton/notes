// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared.database

import notes.shared.preferences.Preferences
import notes.shared.preferences.Theme
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException

class PreferenceDatabase {

    fun savePreferences(x: Double, y: Double, width: Double, height: Double, dividerPos: Double, isListCollapsed: Boolean, theme: Theme ) {
        database.savePreferences( x, y, width, height, dividerPos, if (isListCollapsed) 1 else 0, theme )
    }

    fun getPreferences(): Preferences {
        return database.getPreferences()
    }

    private val database = Database()

    private class Database {
        init {
            createTableIfNotExists()
        }
        private fun connect(): Connection? {
            var conn: Connection? = null
            try {
                val url = "jdbc:sqlite:./PreferenceDatabase.db"
                // TODO Change to own for distzip
                //val url = "jdbc:sqlite:/Users/lindsaymark/IdeaProjects/glory/application/NoteDatabase.db"
                conn = DriverManager.getConnection( url )
                println( "Connection to SQLite has been established.")
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return conn
        }

        private fun createTableIfNotExists() {
            val createPreferencesTableIfNotExistsSQL = "CREATE TABLE IF NOT EXISTS preferences ( id INTEGER PRIMARY KEY, x REAL, y REAL, width REAL, height REAL, dividerPos REAL, isListCollapsed INTEGER, theme TEXT );"
            val setDefaultPreferencesIfTableEmptySQL = "INSERT INTO preferences (x, y, width, height, dividerPos, isListCollapsed, theme ) SELECT 0.0, 0.0, 600.0, 400.0, 200.0, 0, 'LIGHT' WHERE NOT EXISTS (SELECT 1 FROM preferences);"

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val createPreferencesTable = conn.prepareStatement( createPreferencesTableIfNotExistsSQL )
                createPreferencesTable.executeUpdate()
                createPreferencesTable.close()
                val insertDefaultPreferences = conn.prepareStatement( setDefaultPreferencesIfTableEmptySQL )
                insertDefaultPreferences.executeUpdate()
                insertDefaultPreferences.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }

        fun savePreferences(x: Double, y: Double, width: Double, height: Double, dividerPos: Double, isListCollapsed: Int, theme: Theme ) {
            val savePreferencesSQL = "UPDATE preferences SET x = $x, y = $y, width = $width, height = $height, dividerPos = $dividerPos, isListCollapsed = $isListCollapsed, theme = '$theme' WHERE id = 1;"
            println("save pref query is: $savePreferencesSQL")

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return
            }

            try {
                val preparedStatement = conn.prepareStatement( savePreferencesSQL )
                preparedStatement.executeUpdate()
            } catch ( e: SQLException ) {
                println( e.message )
            }
        }

        fun getPreferences(): Preferences {
            val getWindowPosSQL = "SELECT * FROM preferences"
            var x = 0.0
            var y = 0.0
            var width = 0.0
            var height = 0.0
            var dividerPos = 0.0
            var isListCollapsed = 0
            var theme = Theme.LIGHT

            val conn = connect()
            if ( conn == null ) {
                println("Error: could not establish connection to note database.")
                return Preferences( 0.0, 0.0, 600.0, 400.0, 200.0, false, theme )
            }

            try {
                val preparedStatement = conn.prepareStatement( getWindowPosSQL )
                val rs: ResultSet = preparedStatement.executeQuery()
                if ( rs.next() ) {
                    x = rs.getDouble("x")
                    y = rs.getDouble("y")
                    width = rs.getDouble("width")
                    height = rs.getDouble("height")
                    dividerPos = rs.getDouble("dividerPos")
                    isListCollapsed = rs.getInt("isListCollapsed")  // 0 = false, 1 = true
                    theme = if (rs.getString("theme").equals(Theme.LIGHT.toString())) Theme.LIGHT else Theme.DARK
                }
                rs.close()
                preparedStatement.close()
            } catch ( e: SQLException ) {
                println( e.message )
            }
            return Preferences( x, y, width, height, dividerPos, isListCollapsed != 0, theme )
        }
    }

}