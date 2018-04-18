package algorithm.processor

import main.get

/**
 * Created by robert on 4/17/18.
 * Expected to be in the same order (corresponding 1 on 1)
 */
fun merge(feature1: List<Pair<Int, FloatArray>>, feature2: List<Pair<Int, FloatArray>>, merger: (Float, Float) -> Float = { f1, f2 -> f1 * f2 }): List<Pair<Int, FloatArray>> {
    val result = mutableListOf<Pair<Int, FloatArray>>()
    (0 until feature1.size).mapTo(result) { trial ->
        val floatArray = mutableListOf<Float>()
        (0 until feature1[trial].second.size).mapTo(floatArray) {
            merger(feature1[trial].second[it], feature2[trial].second[it])
        }
        Pair(feature1[trial].first, floatArray.toFloatArray())
    }
    return result
}

/**
 * Normalize the data set using (x - min)/(max-min)
 */
fun normalize(feature: List<Pair<Int, FloatArray>>): List<Pair<Int, FloatArray>> {
    val min = feature.minBy { it.second.min()!! }!!.second.min()!!
    val max = feature.maxBy { it.second.max()!! }!!.second.max()!!

    val result = mutableListOf<Pair<Int, FloatArray>>()
    (0 until feature.size).mapTo(result) { trial ->
        val floatArray = mutableListOf<Float>()
        (0 until feature[trial].second.size).mapTo(floatArray) {
            (feature[trial][it] - min)/(max - min)
        }
        Pair(feature[trial].first, floatArray.toFloatArray())
    }
    return result
}