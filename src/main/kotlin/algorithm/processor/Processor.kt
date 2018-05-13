package algorithm.processor

import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.value.MeanAmplitude
import algorithm.extractor.value.ValueExtractor
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
            valueExtractor: ValueExtractor<Spike, Float>,
            dataExtractor: BetweenTimestamps,
            featureExtractor: FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>>,
            preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
            postProcessingTransformers: List<(DataSet) -> DataSet> = listOf()
): DataSet =
        postProcessingTransformers.fold(featureExtractor.extract(preProcessingTransformers.fold(dataExtractor.extractData(spikeReader.readTrials())) { acc, function -> function(acc) }, valueExtractor::extractValue)) { acc, function -> function(acc) }

/**
 * An utility function to simplify the whole process
 * Contains defaults for the most basic approach: single value extractor, unsorted data set, between stim and mean amplitude
 * The transformers are applied in order, starting from left
 *  - preProcessing -> after extracting the data using @param dataExtractor
 *  - postProcessing -> after computing the features with @param valueExtractor using @param featureExtractor
 */
fun process(path: String,
            preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
            postProcessingTransformers: List<(DataSet) -> DataSet> = listOf()
): DataSet {
    val mr = MetadataReader(path)
    val spktwe = mr.readSPKTWE()
    val sp = DataSpikeReader(mr.readETI(), SpikeMetadata(spktwe))
    val ve = MeanAmplitude(spktwe.waveformSpikeOffset)
    val de = BetweenStim(sp)
    val fe = SingleValueFeatureExtractor()

    return postProcessingTransformers.fold(fe.extract(preProcessingTransformers.fold(de.extractData(sp.readTrials())) { acc, function -> function(acc) }, ve::extractValue)) { acc, function -> function(acc) }
}