package notes.shared.database

import notes.shared.preferences.Preferences
import notes.shared.preferences.Theme
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PreferenceDatabaseTest {

    @Test
    fun saveRestorePreferences() {
        val preferenceDatabase = PreferenceDatabase()

        val expectedPreferences = Preferences(10.0, 10.0, 500.0, 300.0, 200.0, false, Theme.DARK)

        preferenceDatabase.savePreferences(10.0, 10.0, 500.0, 300.0, 200.0, false, Theme.DARK)
        val preferences = preferenceDatabase.getPreferences()

        println("expected preferences: $expectedPreferences")
        println("actual preferences: $preferences")

        assertEquals(preferences, expectedPreferences)
    }
}