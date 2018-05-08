package algorithm.processor

import main.DataPoint
import main.get
import main.orientation

/**
 * Created by robert on 4/17/18.
 * Expected to be in the same order (corresponding 1 on 1)
 */
fun merge(feature1: List<DataPoint>, feature2: List<DataPoint>, merger: (Float, Float) -> Float = { f1, f2 -> f1 * f2 }): List<DataPoint> {
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
 * Assumes all elements have the same size as well as that they are in the same order
 * Flats the feature list. Number of trials remains the same
 */
fun aggregateHorizontally(features: List<List<DataPoint>>): List<DataPoint> {
    val result = mutableListOf<DataPoint>()

    val numberOfTrials = features.first().size

    (0 until numberOfTrials).forEach { trial ->
        val mergedFeatureValues = mutableListOf<Float>()
        (0 until features.size).forEach { dataset ->
            mergedFeatureValues.addAll(features[dataset][trial].second.toList())
        }
        result.add(DataPoint(features.first()[trial].first, mergedFeatureValues.toFloatArray()))
    }

    return result
}

/**
 * Assumes all elements have the same size as well as that they are in the same order
 *
 */
fun aggregateVertically(features: List<List<DataPoint>>): List<DataPoint> {
    val result = mutableListOf<DataPoint>()

    (0 until features.size).forEach {
        result.addAll(features[it])
    }

    return result
}

/**
 * Normalize the data set using (x - min)/(max-min)
 */
fun normalize(feature: List<DataPoint>): List<DataPoint> {

    val result = mutableListOf<DataPoint>()
    result.addAll(feature)
    val numberOfUnits = feature.first().second.size
    (0 until numberOfUnits).forEach { unit ->
        val min = feature.minBy { it[unit] }!![unit]
        val max = feature.maxBy { it[unit] }!![unit]

        (0 until feature.size).forEach { trial ->
            result[trial].second[unit] = (feature[trial][unit] - min) / (max - min)
        }
    }
    return result

}

/**
 * Remove first trial of every orientation
 */
fun removeFirstOccurence(feature: List<DataPoint>): List<DataPoint> {
    val indices = mutableListOf<Int>()
    (0 until 8).mapTo(indices) {
        val orientation = it * 45
        feature.indexOfFirst { it.orientation == orientation }
    }
    val result = mutableListOf<DataPoint>()
    (0 until feature.size).forEach {
        if (!indices.contains(it)) {
            result.add(feature[it])
        }
    }
    return result
}