package algorithm.processor

import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.value.MeanAmplitude
import algorithm.extractor.value.ValueExtractor
import algorithm.multiMap
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
fun process(path: String,
            sp: SpikeReader = DataSpikeReader(MetadataReader(path).readETI(), SpikeMetadata(MetadataReader(path).readSPKTWE())),
            valueExtractor: ValueExtractor<Spike, Float> = MeanAmplitude(MetadataReader(path).readSPKTWE().waveformSpikeOffset),
            dataExtractor: BetweenTimestamps = BetweenStim(sp),
            fe: FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> = SingleValueFeatureExtractor(),
            preProcessingTransformers: List<(TrialData) -> TrialData> = listOf(),
            postProcessingTransformres: List<(Pair<Int, FloatArray>) -> Pair<Int, FloatArray>> = listOf()
): List<Pair<Int, FloatArray>> = fe.extract(dataExtractor.extractData(sp.readTrials()).multiMap(preProcessingTransformers), valueExtractor::extractValue).multiMap(postProcessingTransformres)
