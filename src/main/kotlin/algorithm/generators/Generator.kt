package algorithm.generators

import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.feature.constructExtractor
import algorithm.extractor.value.*
import algorithm.processor.aggregateHorizontally
import algorithm.processor.aggregateVertically
import algorithm.processor.process
import exporter.exportCSV
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import main.DataPoint
import main.DataSet
import model.Spike
import model.Trial
import model.TrialData
import model.metadata.SpikeMetadata
import reader.MetadataReader
import reader.spikes.DataSpikeReader
import reader.spikes.SpikeReader

/**
 * Created by robert on 4/23/18.
 * Helper function to generate dataSets
 */
fun generateFiles(spikeReader: SpikeReader,
                  valueExtractors: List<ValueExtractor<Spike, Float>>,
                  featureExtractors: List<FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>>>,
                  dataExtractors: List<BetweenTimestamps>,
                  preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
                  postProcessingTransformers: List<(DataSet) -> DataSet> = listOf(),
                  nameGenerator: (ValueExtractor<Spike, Float>, FeatureExtractor<TrialData, List<DataPoint>>, BetweenTimestamps) -> String) {

    runBlocking {
        async {
            val jobs = mutableListOf<Job>()
            for (fe in featureExtractors) {
                for (ve in valueExtractors) {
                    for (de in dataExtractors) {
                        jobs.add(launch {
                            exportCSV(process(spikeReader = spikeReader, valueExtractor = constructExtractor(ve::extractValue), featureExtractor = fe, dataExtractor = de, preProcessingTransformers = preProcessingTransformers, postProcessingTransformers = postProcessingTransformers), nameGenerator(ve, fe, de))
                        })
                    }
                }
            }
            jobs.forEach { it.join() }
        }.await()
    }
}

fun generateGeometricFeatures(paths: List<String>,
                              preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
                              postProcessingTransformers: List<(DataSet) -> DataSet> = listOf(),
                              spikeReaderFilters: List<Trial.() -> Boolean> = listOf(),
                              name: String) {

    val results = mutableListOf<DataSet>()
    paths.mapTo(results) {
        val mr = MetadataReader(it)
        val spktweMetadata = mr.readSPKTWE()
        val sr = DataSpikeReader(mr.readETI(), SpikeMetadata(spktweMetadata), spikeReaderFilters)

        aggregateHorizontally(
                listOf(
                        process(sr, constructExtractor(Amplitude(spktweMetadata.waveformSpikeOffset)::extractValue), BetweenStim(sr), SingleValueFeatureExtractor(), preProcessingTransformers = preProcessingTransformers),
                        process(sr, constructExtractor(Area(spktweMetadata.waveformInternalSamplingFrequency, spktweMetadata.waveformSpikeOffset)::extractValue), BetweenStim(sr), SingleValueFeatureExtractor(), preProcessingTransformers = preProcessingTransformers),
                        process(sr, constructExtractor(Perimeter(spktweMetadata.waveformInternalSamplingFrequency, spktweMetadata.waveformSpikeOffset)::extractValue), BetweenStim(sr), SingleValueFeatureExtractor(), preProcessingTransformers = preProcessingTransformers),
                        process(sr, constructExtractor(Width(spktweMetadata.waveformInternalSamplingFrequency, spktweMetadata.waveformSpikeOffset)::extractValue), BetweenStim(sr), SingleValueFeatureExtractor(), preProcessingTransformers = preProcessingTransformers)
                )
        )
    }
    exportCSV((postProcessingTransformers.fold(aggregateVertically(results).toList()) { acc, function -> function(acc) }), name)
}


