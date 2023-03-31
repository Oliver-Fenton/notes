// Copyright (c) 2023. Gillian Johnson, Lindsay Mark, Oliver Fenton, Richard Hua.

package notes.shared

import org.junit.jupiter.api.Test

class SysInfoTest {
    @Test
    fun osTest() {
        val os = System.getProperty("os.arch")
        assert(os == SysInfo.osArch)
    }
}