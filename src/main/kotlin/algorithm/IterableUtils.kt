package algorithm

import model.Spike
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Created by robert on 4/19/18.
 * Some iterable utils for handling multi lambda parameters applied sequentially on a list
 */
fun <T> Iterable<T>.multiMap(transforms: List<(T) -> T>): List<T> = if (transforms.isNotEmpty()) {
    this.map {
        transforms.drop(1).fold(transforms.first()(it)) { acc, function ->
            function(acc)
        }
    }
} else this.toList()

fun <T> Iterable<T>.multiMapRight(transforms: List<(T) -> T>): List<T> = if (transforms.isNotEmpty()) {
    this.map {
        transforms.dropLast(1).foldRight(transforms.last()(it)) { function, acc ->
            function(acc)
        }
    }
} else this.toList()

fun <T> Iterable<T>.multiFilter(filters: List<(T) -> Boolean>): List<T> = if (filters.isNotEmpty()) {
    this.filter {
        filters.fold(true) { acc, function ->
            acc and function(it)
        }
    }
} else this.toList()

fun Iterable<Int>.cumulativeSum(): List<Int> {
    val result = mutableListOf<Int>()
    var sum = 0
    this.mapTo(result) {
        sum += it
        sum
    }
    return result
}

fun Iterable<Number>.mean(): Double = this.map { it.toDouble() }.sum().div(count())

fun Iterable<Number>.sumAndDiv(div: Double): Double {
    return map { BigDecimal.valueOf(it.toDouble()) }
            .fold(BigDecimal.ZERO) { acc, bigDecimal -> bigDecimal + acc }
            .divide(BigDecimal.valueOf(div), 32, RoundingMode.HALF_UP)
            .toDouble()
}

fun Iterable<Number>.std(): Double {
    val mean = mean()
    return sqrt(map { (it.toDouble() - mean).pow(2).toBigDecimal() }
            .sumAndDiv(count().toDouble()))
}

/**
 * Still contains bias
 */
fun Iterable<Number>.besselCorrectedStd(): Double {
    val mean = mean()
    return sqrt(map { (it.toDouble() - mean).pow(2).toBigDecimal() }
            .sumAndDiv(count().toDouble() - 1))
}

fun Iterable<Number>.unbiasedVarianceEstimate(): Double {
    val mean = mean()
    return map { (it.toDouble() - mean).pow(2) }.sumAndDiv(count().toDouble() - 1)
}

/**
 * Assumes a normal distribution
 */
fun Iterable<Number>.unbiasedStd(): Double {
    /**
     * At infinity
     */
    fun puiseuxExpansion(size: Int): Double {
        val n = size.toDouble()
        val invN = 1.0 / n
        val sqrt2 = sqrt(2.0)
        return when (size) {
            1 -> 0.0
            2 -> 1 / sqrt(Math.PI)
            3 -> sqrt(Math.PI) / 2
            else -> {
                val c4 = sqrt(n) / sqrt2 -
                        (3 * invN.pow(1.0 / 2)) / (4 * sqrt2) -
                        (7 * invN.pow(3.0 / 2)) / (32 * sqrt2) -
                        (9 * invN.pow(5.0 / 2)) / (128 * sqrt2) +
                        (59 * invN.pow(7.0 / 2)) / (2048 * sqrt2) +
                        (483 * invN.pow(9.0 / 2)) / (8192 * sqrt2) -
                        (2323 * invN.pow(11.0 / 2)) / (65536 * sqrt2) -
                        (42801 * invN.pow(13.0 / 2)) / (262144 * sqrt2) +
                        (923923 * invN.pow(15.0 / 2)) / (8388608 * sqrt2) +
                        (30055311 * invN.pow(17.0 / 2)) / (33554432 * sqrt2) -
                        (170042041 * invN.pow(19.0 / 2)) / (268435456 * sqrt2) -
                        (8639161167 * invN.pow(21.0 / 2)) / (1073741824 * sqrt2) +
                        (99976667055 * invN.pow(23.0 / 2)) / (17179869184 * sqrt2)
                c4
            }
        }

    }
    return besselCorrectedStd() / (sqrt(2.0 / (count() - 1)) * puiseuxExpansion(count()))
}

fun Iterable<Number>.median(): Double {
    val list = this.map { it.toDouble() }.sorted()
    val size = list.size
    return if (size % 2 == 0) {
        (list[size / 2 - 1] + list[size / 2]) / 2
    } else {
        list[size / 2]
    }
}

fun <T : Number, P : Number> Iterable<T>.join(second: Iterable<P>, f: (T, P) -> Double): List<Double> = mapIndexed { index, t -> f(t, second.elementAt(index)) }

fun <T : Number, P : Number> Array<T>.join(second: Array<P>, f: (T, P) -> Double): List<Double> = mapIndexed { index, t -> f(t, second.elementAt(index)) }

fun <P : Number> FloatArray.join(second: Array<P>, f: (Float, P) -> Double): List<Double> = mapIndexed { index, t -> f(t, second.elementAt(index)) }

fun FloatArray.join(second: DoubleArray, f: (Float, Double) -> Double): FloatArray = mapIndexed { index, t -> f(t, second.elementAt(index)).toFloat() }.toFloatArray()

fun <T : Number> Iterable<T>.averageSmooth(howMany: Int = 1): List<Double> = mutableListOf<Double>()
        .plus((0 until howMany).map { elementAt(it).toDouble() })
        .plus((howMany until count() - howMany).map {
            toList().slice(it-howMany .. it+howMany).sumByDouble { it.toDouble() }.div(howMany * 2  + 1)
        })
        .plus((count() - howMany until count()).map { elementAt(it).toDouble() })
