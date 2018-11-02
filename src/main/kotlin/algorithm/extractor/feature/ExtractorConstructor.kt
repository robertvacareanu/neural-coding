package algorithm.extractor.feature

import algorithm.extractor.feature.strategy.mean.ArithmeticMeanFeatureExtractor
import model.Spike

/**
 * Created by robert on 8/26/18.
 */

/**
 * The value extractor maps a whole array to a single value -- @see [TimeToFirstspke]
 */
fun constructExtractor(ve: (Array<Spike>) -> Float) = ve

/**
 * Ex: [InterSpikeInterval]
 */
@JvmName("consructArrayExtractor")
fun constructExtractor(ve: (Array<Spike>) -> FloatArray, strategy: (FloatArray) -> Double): (Array<Spike>) -> Float = { strategy(ve(it)).toFloat() }

/**
 * Constructs a (Array<Spike>) -> Float function from (Spike) -> Float and (FloatArray) -> Double
 */
fun constructExtractor(ve: (Spike) -> Float, strategy: (FloatArray) -> Double): (Array<Spike>) -> Float = { strategy(it.map(ve).toFloatArray()).toFloat() }

/**
 * Uses [ArithmeticMeanFeatureExtractor] as default
 */
@JvmName("constructExtractorDefault")
fun constructExtractor(ve: (Spike) -> Float): (Array<Spike>) -> Float = { ArithmeticMeanFeatureExtractor().extract(it.map(ve).toFloatArray()).toFloat() }
