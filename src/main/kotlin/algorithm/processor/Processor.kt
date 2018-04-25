package algorithm.processor

import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.value.MeanAmplitude
import algorithm.extractor.value.ValueExtractor
import main.DataPoint
import model.Spike
import model.TrialData
import model.metadata.SpikeMetadata
import reader.MetadataReader
import reader.spikes.DataSpikeReader
import reader.spikes.SpikeReader

/**
 * Created by robert on 4/19/18.
 * An utility function to simplify the whole process
 * Contains defaults for the most basic approach: single value extractor, unsorted data set, between stim and mean amplitude
 */
fun process(spikeReader: SpikeReader,
            valueExtractor: ValueExtractor<Spike, Float>,
            dataExtractor: BetweenTimestamps,
            featureExtractor: FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>>,
            preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
            postProcessingTransformres: List<(List<DataPoint>) -> List<DataPoint>> = listOf()
): List<DataPoint> =
        postProcessingTransformres.fold(featureExtractor.extract(preProcessingTransformers.fold(dataExtractor.extractData(spikeReader.readTrials())) { acc, function -> function(acc) }, valueExtractor::extractValue)) { acc, function -> function(acc) }


fun process(path: String,
            preProcessingTransformers: List<(TrialData) -> TrialData> = listOf(),
            postProcessingTransformres: List<(Pair<Int, FloatArray>) -> Pair<Int, FloatArray>> = listOf()
): List<DataPoint> {
    val mr = MetadataReader(path)
    val spktwe = mr.readSPKTWE()
    val sp = DataSpikeReader(mr.readETI(), SpikeMetadata(spktwe))
    val ve = MeanAmplitude(spktwe.waveformSpikeOffset)
    val de = BetweenStim(sp)
    val fe = SingleValueFeatureExtractor()

    return fe.extract(de.extractData(sp.readTrials()), ve::extractValue)
}