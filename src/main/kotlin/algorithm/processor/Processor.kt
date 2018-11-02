package algorithm.processor

import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.feature.constructExtractor
import algorithm.extractor.feature.strategy.mean.ArithmeticMeanFeatureExtractor
import algorithm.extractor.value.Amplitude
import main.DataSet
import model.Spike
import model.TrialData
import model.metadata.SpikeMetadata
import reader.MetadataReader
import reader.spikes.DataSpikeReader
import reader.spikes.SpikeReader

/**
 * Created by robert on 4/19/18.
 * An utility function to simplify the whole process
 * The transformers are applied in order, starting from left
 *  - preProcessing -> after extracting the data using @param dataExtractor
 *  - postProcessing -> after computing the features with @param valueExtractor using @param featureExtractor
 */
fun process(spikeReader: SpikeReader,
            valueExtractor: (Array<Spike>) -> Float,
            dataExtractor: BetweenTimestamps,
            featureExtractor: FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>>,
            preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
            postProcessingTransformers: List<(DataSet) -> DataSet> = listOf()
): DataSet =
        postProcessingTransformers.fold(featureExtractor.extract(preProcessingTransformers.fold(dataExtractor.extractData(spikeReader.readTrials())) { acc, function -> function(acc) }, valueExtractor)) { acc, function -> function(acc) }

/**
 * An utility function to simplify the whole process of generating feature file using unsorted dataset
 * Contains defaults for the most basic approach: single value extractor, unsorted data set, between stim and mean amplitude
 * The transformers are applied in order, starting from left
 *  - preProcessing -> after extracting the data using @param dataExtractor
 *  - postProcessing -> after computing the features with @param valueExtractor using @param featureExtractor
 */
fun processUnsorted(path: String,
                    preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
                    postProcessingTransformers: List<(DataSet) -> DataSet> = listOf()
): DataSet {
    val mr = MetadataReader(path)
    val spktwe = mr.readSPKTWE()
    val sp = DataSpikeReader(mr.readETI(), SpikeMetadata(spktwe))
    val ve = constructExtractor(Amplitude(spktwe.waveformSpikeOffset)::extractValue, ArithmeticMeanFeatureExtractor()::extract)
    val de = BetweenStim(sp)
    val fe = SingleValueFeatureExtractor()

    return postProcessingTransformers.fold(fe.extract(preProcessingTransformers.fold(de.extractData(sp.readTrials())) { acc, function -> function(acc) }, ve)) { acc, function -> function(acc) }
}

/**
 * An utility function to simplify the whole process of generating feature file using sorted dataset
 * Contains defaults for the most basic approach: single value extractor, unsorted data set, between stim and mean amplitude
 * The transformers are applied in order, starting from left
 *  - preProcessing -> after extracting the data using @param dataExtractor
 *  - postProcessing -> after computing the features with @param valueExtractor using @param featureExtractor
 */
fun processSorted(path: String,
                  preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
                  postProcessingTransformers: List<(DataSet) -> DataSet> = listOf()
): DataSet {
    val mr = MetadataReader(path)
    val spktwe = mr.readSPKTWE()
    val sp = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD()))
    val ve = constructExtractor(Amplitude(spktwe.waveformSpikeOffset)::extractValue, ArithmeticMeanFeatureExtractor()::extract)
    val de = BetweenStim(sp)
    val fe = SingleValueFeatureExtractor()

    return postProcessingTransformers.fold(fe.extract(preProcessingTransformers.fold(de.extractData(sp.readTrials())) { acc, function -> function(acc) }, ve)) { acc, function -> function(acc) }
}