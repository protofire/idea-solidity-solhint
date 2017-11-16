package idrabenia.solhint.client

import org.junit.Test
import java.io.File
import java.nio.file.Files


class SolhintClientTest {

    @Test
    fun `should correctly start solhint server` () {
        SolhintClient.startServer(".")

        assert(SolhintClient.server!!.isAlive())
        SolhintClient.stopServer()
    }

    @Test
    fun `should correctly stop solhint server` () {
        SolhintClient.startServer(".")
        val process = SolhintClient.server!!

        SolhintClient.stopServer()

        waitToStop(process.process)
        assert(!process.isAlive())
    }

    @Test
    fun `should correctly verify solidity file` () {
        val client = SolhintClient
        client.startServer(".")
        val file = File.createTempFile("test", ".sol")
        Files.write(file.toPath(), arrayListOf("pragma solidity ^4.1.1"))

        Thread.sleep(3000)
        val result = client.fileErrors(".", file.absolutePath)

        assert(result.size == 1)
        client.stopServer()
    }

    fun waitToStop(process: Process?) {
        while (process != null && process.isAlive) {
            // wait
        }
    }

}