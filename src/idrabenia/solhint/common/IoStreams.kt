package idrabenia.solhint.common

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


object IoStreams {

    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream): Long {
        return copy(input, output, 8024)
    }

    @Throws(IOException::class)
    fun copy(input: InputStream, output: OutputStream, buffersize: Int): Long {
        val buffer = ByteArray(buffersize)
        var count: Long = 0
        var n = input.read(buffer)

        while (-1 != n) {
            output.write(buffer, 0, n)
            count += n.toLong()

            n = input.read(buffer)
        }

        return count
    }

    @Throws(IOException::class)
    fun toByteArray(input: InputStream): ByteArray {
        val output = ByteArrayOutputStream()
        copy(input, output)
        return output.toByteArray()
    }

    @Throws(IOException::class)
    fun drain(input: InputStream) {
        toByteArray(input)
    }

}
