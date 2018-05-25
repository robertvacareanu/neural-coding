package reader.binary

import main.almostEqual
import org.junit.Test
import reader.readFloatBinary
import reader.readIntBinary

/**
 * Created by robert on 5/25/18.
 * Tests for functions that work with binary files
 * It considers two dummy binary files, for int and float
 */
class BinaryFileReaderTest {

    @Test
    fun readFloatBinaryTest() {
        val result1 = readFloatBinary("src/test/kotlin/reader/binary/float_binary_test_file")

        assert(result1.size == 256)
        assert(result1[0] almostEqual 1f)
        assert(result1[1] almostEqual 2f)
        assert(result1[2] almostEqual 3f)
        assert(result1[3] almostEqual 4f)
        assert(result1[5] almostEqual 6f)
        assert(result1[10] almostEqual 11f)
        assert(result1[25] almostEqual 26f)

        val result2 = readFloatBinary("src/test/kotlin/reader/binary/float_binary_test_file", 5 until 10)

        assert(result2.size == 5)
        assert(result2[0] almostEqual 6f)
        assert(result2[1] almostEqual 7f)
        assert(result2[2] almostEqual 8f)
        assert(result2[3] almostEqual 9f)
        assert(result2[4] almostEqual 10f)
    }

    @Test
    fun readIntBinaryTest() {
        val result1 = readIntBinary("src/test/kotlin/reader/binary/int_binary_test_file")

        assert(result1.size == 256)
        assert(result1[0] == 1)
        assert(result1[1] == 2)
        assert(result1[2] == 3)
        assert(result1[3] == 4)
        assert(result1[5] == 6)
        assert(result1[10] == 11)
        assert(result1[25] == 26)

        val result2 = readIntBinary("src/test/kotlin/reader/binary/int_binary_test_file", 5 until 10)

        assert(result2.size == 5)
        assert(result2[0] == 6)
        assert(result2[1] == 7)
        assert(result2[2] == 8)
        assert(result2[3] == 9)
        assert(result2[4] == 10)
    }


}