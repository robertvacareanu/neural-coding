package algorithm.generators

import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.value.ValueExtractor
import algorithm.processor.process
import exporter.exportCSV
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import main.DataPoint
import model.Spike
import model.TrialData
import reader.spikes.SpikeReader

/**
 * Created by robert on 4/23/18.
 * Helper function to generate datasets
 */
fun generateFiles(spikeReader: SpikeReader,
                  valueExtractors: List<ValueExtractor<Spike, Float>>,
                  featureExtractors: List<FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>>>,
                  dataExtractors: List<BetweenTimestamps>,
                  preProcessingTransformers: List<(List<TrialData>) -> List<TrialData>> = listOf(),
                  postProcessingTransformres: List<(List<DataPoint>) -> List<DataPoint>> = listOf(),
                  nameGenerator: (ValueExtractor<Spike, Float>, FeatureExtractor<TrialData, List<DataPoint>>, BetweenTimestamps) -> String) {

    runBlocking {
        async {
            val jobs = mutableListOf<Job>()
            for (fe in featureExtractors) {
                for (ve in valueExtractors) {
                    for (de in dataExtractors) {
                        jobs.add(launch {
                            exportCSV(process(spikeReader = spikeReader, valueExtractor = ve, featureExtractor = fe, dataExtractor = de, preProcessingTransformers = preProcessingTransformers, postProcessingTransformres = postProcessingTransformres), nameGenerator(ve, fe, de))
                        })
                    }
                }
            }
            jobs.forEach { it.join() }
        }.await()
    }

}
