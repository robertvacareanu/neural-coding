package algorithm

import org.junit.Test

/**
 * Created by robert on 4/19/18.
 * Tests for IterableUtils functions
 */
class IterableUtilsTest {

    @Test
    fun multiMapTest() {

        val transforms1 = listOf<(String) -> String>({ it + "a" }, { it + "b" }, { it + "c" })

        val base1 = listOf("1", "2").multiMap(transforms1)

        assert(base1[0] == "1abc")
        assert(base1[1] == "2abc")

        val base2 = listOf("1", "2").multiMapRight(transforms1)

        assert(base2[0] == "1cba")
        assert(base2[1] == "2cba")

        val transforms2 = listOf<(Int) -> Int>({ it + 1 }, { it * 2 }, { it + 4 }, { it * it }, { it - 3 })
        val base3 = listOf(1, 2, 3, 4).multiMap(transforms2)

        assert(base3[0] == 61)
        assert(base3[1] == 97)
        assert(base3[2] == 141)
        assert(base3[3] == 193)

        val base4 = listOf(1, 2, 3, 4).multiMapRight(transforms2)

        assert(base4[0] == 17)
        assert(base4[1] == 11)
        assert(base4[2] == 9)
        assert(base4[3] == 11)

    }

    @Test
    fun multiFilterTest() {
        val filters1 = listOf<(Int) -> Boolean>({ it % 2 != 0 }, { it % 3 != 0 }, { it % 5 != 0 }, { it % 7 != 0 }, { it % 11 != 0 })
        val base1 = IntRange(0, 100).toList().multiFilter(filters1)
        assert(base1[0] == 1)
        assert(base1[1] == 13)
        assert(base1[2] == 17)
        assert(base1[3] == 19)
        assert(base1[4] == 23)
        assert(base1[5] == 29)
        assert(base1[6] == 31)
        assert(base1[7] == 37)
        assert(base1[8] == 41)
        assert(base1[9] == 43)
        assert(base1[10] == 47)
    }

    @Test
    fun cumulativeSumTest() {
        var result = listOf(1, 2, 3, 4, 5).cumulativeSum()
        assert(result[0] == 1)
        assert(result[1] == 3)
        assert(result[2] == 6)
        assert(result[3] == 10)
        assert(result[4] == 15)

        result = listOf(-1, 0, 5, -4, 3, 10).cumulativeSum()
        assert(result[0] == -1)
        assert(result[1] == -1)
        assert(result[2] == 4)
        assert(result[3] == 0)
        assert(result[4] == 3)
        assert(result[5] == 13)

    }

    @Test
    fun medianTest() {
        var x = listOf(1,2,3,4,5)
        assert(x.median() == x[2].toDouble())

        x = listOf(6,7,8,9)
        assert(x.median() == (x[1].toDouble() + x[2].toDouble())/2)

    }

    @Test
    fun joinTest() {
        val a = listOf(1,2,3,4)
        val b = listOf(1.1, 2.2, 3.3, 4.4)

        assert(a.join(b) {i, d -> i + d } == listOf(a[0] + b[0], a[1] + b[1], a[2] + b[2], a[3]+b[3]))
    }

}