package reader

import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by robert on 3/24/18.
 * Functions for reading binary files
 */

fun readFloatBinary(filePath: String): FloatArray {
    val result = arrayListOf<Float>()
    val f = File(filePath)
    f.inputStream().channel.use {
        val bb = ByteBuffer.allocate(1024 * 1024)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        while (it.read(bb) > 0) {
            bb.flip()
            (0 until bb.limit() step 4).mapTo(result) { bb.getFloat(it) }
            bb.clear()
        }
    }
    return result.toFloatArray()
}

fun readFloatBinary(filePath: String, between: IntRange): FloatArray {
    val result = arrayListOf<Float>()
    val f = File(filePath)
    f.inputStream().channel.use {
        val bb = ByteBuffer.allocate(4 * (between.last - between.first + 1))
        it.position((between.first * 4).toLong())
        bb.order(ByteOrder.LITTLE_ENDIAN)
        while (it.read(bb) > 0 && it.position() <= 4 * between.last + 4) {
            bb.flip()
            (0 until bb.limit() step 4).mapTo(result) { bb.getFloat(it) }
            bb.clear()
        }
    }
    return result.toFloatArray()
}

fun readIntBinary(filePath: String): IntArray {
    val result = arrayListOf<Int>()

    val f = File(filePath)
    f.inputStream().channel.use {
        val bb = ByteBuffer.allocate(1024 * 1024)
        bb.order(ByteOrder.LITTLE_ENDIAN)
        while (it.read(bb) > 0) {
            bb.flip()
            (0 until bb.limit() step 4).mapTo(result) { bb.getInt(it) }
            bb.clear()
        }
    }
    return result.toIntArray()
}

fun readIntBinary(filePath: String, between: IntRange): IntArray {
    val result = arrayListOf<Int>()

    val f = File(filePath)
    f.inputStream().channel.use {
        val bb = ByteBuffer.allocate(4 * (between.last - between.first + 1))
        it.position((between.first * 4).toLong())
        bb.order(ByteOrder.LITTLE_ENDIAN)
        while (it.read(bb) > 0 && it.position() <= 4 * between.last + 4) {
            bb.flip()
            (0 until bb.limit() step 4).mapTo(result) { bb.getInt(it) }
            bb.clear()
        }
    }
    return result.toIntArray()
}
