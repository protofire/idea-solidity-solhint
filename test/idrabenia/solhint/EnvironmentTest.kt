package idrabenia.solhint

import com.intellij.mock.MockApplication
import com.intellij.mock.MockApplicationEx
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import idrabenia.solhint.client.Environment
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class EnvironmentTest {

    @Test
    fun `should verify when command is not installed`() {
        val result = Environment.canRunProcess("node1")

        assert(!result)
    }

    @Test
    fun `should verify when command is installed`() {
        val result = Environment.canRunProcess("cd")

        assert(result)
    }

}