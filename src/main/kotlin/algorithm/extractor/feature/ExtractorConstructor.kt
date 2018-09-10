package algorithm.extractor.feature

import algorithm.extractor.feature.strategy.mean.ArithmeticMeanFeatureExtractor
import model.Spike

/**
 * Created by robert on 8/26/18.
 */
fun constructExtractor(ve: (Array<Spike>) -> Float) = ve
@JvmName("consructArrayExtractor")
fun constructExtractor(ve: (Array<Spike>) -> FloatArray, strategy: (FloatArray) -> Double): (Array<Spike>) -> Float = { strategy(ve(it)).toFloat() }
fun constructExtractor(ve: (Spike) -> Float, strategy: (FloatArray) -> Double): (Array<Spike>) -> Float = { strategy(it.map(ve).toFloatArray()).toFloat() }
@JvmName("constructExtractorDefault")
fun constructExtractor(ve: (Spike) -> Float): (Array<Spike>) -> Float = { ArithmeticMeanFeatureExtractor().extract(it.map(ve).toFloatArray()).toFloat() }
//abstract class AbstractFeatureExtractor : FeatureExtractor<TrialData, DataSet> {
//    @JvmName("extractHelperSDSV")
//    protected fun extractHelper(data: Array<Spike>, valueExtractor: (Spike) -> Float, strategy: (List<Float>) -> Double) = strategy(data.map(valueExtractor)).toFloat()
//    protected fun extractHelper(data: Array<Spike>, valueExtractor: (Array<Spike>) -> Float) = valueExtractor(data)
//    protected fun extractHelper(data: Array<Spike>, valueExtractor: (Array<Spike>) -> List<Float>, strategy: (List<Float>) -> Double) = strategy(valueExtractor(data)).toFloat()
//
//}