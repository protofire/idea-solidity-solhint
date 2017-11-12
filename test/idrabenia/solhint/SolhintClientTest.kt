package idrabenia.solhint

import org.apache.commons.lang.StringUtils
import org.junit.Test
import java.io.File
import java.nio.file.Files


class SolhintClientTest {

    @Test
    fun `should correctly start solhint server` () {
        val client = SolhintClient()

        client.startServer(".")

        assert(client.server!!.isAlive())
        client.stopServer()
    }

    @Test
    fun `should correctly stop solhint server` () {
        val client = SolhintClient()
        client.startServer(".")
        val process = client.server

        client.stopServer()

        waitToStop(process!!.process!!)
        assert(!process.isAlive())
    }

    @Test
    fun `should correctly verify solidity file` () {
        val client = SolhintClient()
        client.startServer(".")
        val file = File.createTempFile("test", ".sol")
        Files.write(file.toPath(), arrayListOf("pragma solidity ^4.1.1"))

        Thread.sleep(1000)
        val result = client.fileErrors(".", file.absolutePath)

        assert(result.size == 1)
        client.stopServer()
    }

    fun waitToStop(process: Process) {
        while (process.isAlive) {
            // wait
        }
    }

}