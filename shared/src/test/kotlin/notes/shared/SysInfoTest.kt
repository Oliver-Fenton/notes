package notes.shared

import org.junit.jupiter.api.Test

class SysInfoTest {
    @Test
    fun osTest() {
        val os = System.getProperty("os.arch")
        assert(os == SysInfo.osArch)
    }
}