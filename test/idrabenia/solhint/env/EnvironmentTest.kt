package idrabenia.solhint.env

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
