package notes.application
import javafx.scene.input.KeyCodeCombination
import java.lang.System
object Constants {
    val OS = System.getProperty("os.name").lowercase()
    val OS_KeyCombo = if (OS.contains("mac")) KeyCodeCombination.META_DOWN else KeyCodeCombination.CONTROL_DOWN

}