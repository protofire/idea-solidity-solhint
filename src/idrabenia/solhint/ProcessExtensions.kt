package idrabenia.solhint

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.TimeUnit


fun Process.waitUntilExit(timeout: Long, unit: TimeUnit): Process {
    this.waitFor(timeout, unit)
    return this
}

fun Process.stdout(): List<String> {
    return readOutput(this.inputStream)
}

fun readOutput(input: InputStream): List<String> {
    val reader = BufferedReader(InputStreamReader(input))
    val lineList = ArrayList<String>()

    var line = reader.readLine()
    while (line!= null) {
        lineList.add(line)

        line = reader.readLine()
    }

    return lineList
}