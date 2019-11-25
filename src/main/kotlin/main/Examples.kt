package main

import algorithm.extractor.data.BetweenStim
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.feature.WindowValueFeatureExtractor
import algorithm.extractor.feature.constructExtractor
import algorithm.extractor.feature.strategy.mean.GeometricMeanFeatureExtractor
import algorithm.extractor.value.Amplitude
import algorithm.extractor.value.Perimeter
import algorithm.processor.process
import algorithm.processor.removeIfEmpty
import algorithm.processor.removeTrials
import exporter.exportCSV
import model.Trial
import model.metadata.SpikeMetadata
import reader.MetadataReader
import reader.spikes.DataSpikeReader

/**
 * Created by robert on 11/2/18.
 * Contains different examples for producing a file with desired features
 */

/**
 * @param path: path to a folder containing the necessary files (.epd, .spktwe, .ssd etc)
 * Generate a file "data" with:
 *      Trials with contrast 100
 *      Only 315 and 180 orientation
 *      Sorted version
 *      Amplitude as value extractor
 *      Arithmetic mean
 *      BetweenStim (spikes that appeared only when the stimulus was on)
 *      SingleValueFeatureExtractor (the array of spikes for a particular unit for a trial gives only one feature)
 *      Removes units that do not contains spikes in all trals
 */
fun example1(path: String) {
    val mr = MetadataReader(path) // in order to read the metadata files
    // create an object that will read the spikes (sorted version) and trials. Only the trials that respect the constraint are read
    val sr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD()), listOf<Trial.() -> Boolean>({ contrast == 100 }, { (orientation == 315) or (orientation == 180) }))
    val spktweMetadata = mr.readSPKTWE() // read the spike metadata associated with this recording
    // Export to "data" the DataSet resulted from process. The DataSet was created with: Amplitude as value extractor, ArithmeticMean (implicit) as a way to reduce a vector to a single value (see simple pipeline explanation), BetweenStim as a way to read the desired spikes for a particular unit (see trial description)
    exportCSV(
            process(sr,
                    constructExtractor(Amplitude(spktweMetadata.waveformSpikeOffset)::extractValue), // identical to constructExtractor(Amplitude(spktweMetadata.waveformSpikeOffset)::extractValue, ArithmeticMeanFeatureExtractor()::extract),
                    BetweenStim(sr),
                    SingleValueFeatureExtractor(),
                    postProcessingTransformers = listOf(::removeIfEmpty)) // removing units that do not contain spikes in all trials
            , "data")
    /**
     * exportCSV -> takes a DataSet and outputs it to a file
     * process -> takes the necessary parameters and returns a DataSet
     */
}

/**
 * @param path: path to a folder containing the necessary files (.epd, .spktwe, .ssd etc)
 * Generate a file "data" with:
 *      All trials and all orientations
 *      Unsorted version
 *      Amplitude as value extractor
 *      Geometric mean
 *      BetweenStim (spikes that appeared only when the stimulus was on)
 *      SingleValueFeatureExtractor (the array of spikes for a particular unit for a trial gives only one feature)
 *
 */
fun example2(path: String) {
    val mr = MetadataReader(path) // in order to read the metadata files
    // create an object that will read the spikes (sorted version) and trials
    val sr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))
    val spktweMetadata = mr.readSPKTWE() // read the spike metadata associated with this recording
    // Export to "data" the DataSet resulted from process. The DataSet was created with: Amplitude as value extractor, ArithmeticMean (implicit) as a way to reduce a vector to a single value (see simple pipeline explanation), BetweenStim as a way to read the desired spikes for a particular unit (see trial description)
    exportCSV(
            process(sr,
                    constructExtractor(Amplitude(spktweMetadata.waveformSpikeOffset)::extractValue, GeometricMeanFeatureExtractor()::extract),
                    BetweenStim(sr),
                    SingleValueFeatureExtractor())
            , "data")
    /**
     * exportCSV -> takes a DataSet and outputs it to a file
     * process -> takes the necessary parameters and returns a DataSet
     */
}

/**
 * @param path: path to a folder containing the necessary files (.epd, .spktwe, .ssd etc)
 * Generate a file "data" with:
 *      Trials with contrast 100 and orientation 0 or 45 or 90
 *      Unsorted version
 *      Amplitude as value extractor
 *      Geometric mean
 *      BetweenStim (spikes that appeared only when the stimulus was on)
 *      WindowValueFeatureExtractor (the array of spikes for a particular unit for a trial gives multiple features)
 *
 */
fun example3(path: String) {
    val mr = MetadataReader(path) // in order to read the metadata files
    // create an object that will read the spikes (sorted version) and trials. Only the trials that respect the constrain are read
    val sr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD()), listOf<Trial.() -> Boolean>({ contrast == 100 }, { (orientation == 45) or (orientation == 90) or (orientation == 0) }))
    val spktweMetadata = mr.readSPKTWE() // read the spike metadata associated with this recording
    // Export to "data" the DataSet resulted from process. The DataSet was created with: Amplitude as value extractor, ArithmeticMean (implicit) as a way to reduce a vector to a single value (see simple pipeline explanation), BetweenStim as a way to read the desired spikes for a particular unit (see trial description)
    exportCSV(
            process(sr,
                    constructExtractor(Perimeter(spktweMetadata.waveformInternalSamplingFrequency, spktweMetadata.waveformSpikeOffset)::extractValue),
                    BetweenStim(sr),
                    WindowValueFeatureExtractor((spktweMetadata.waveformInternalSamplingFrequency / 5.0f).toInt(), 0.5), // waveformInternalSamplingFrequency / 5 => windows of 0.2 second length. 0.5 => 50% overlap (between windows)
                    postProcessingTransformers = listOf(::removeIfEmpty)) // removing units that do not contain spikes in all trials
            , "data")
    /**
     * exportCSV -> takes a DataSet and outputs it to a file
     * process -> takes the necessary parameters and returns a DataSet
     */
}

fun generateMeanAmplitudeRecording1(path: String) {
    val mr = MetadataReader(path) // in order to read the metadata files
    val removeTrials: (DataSet) -> DataSet = { removeTrials(it, (20 until 80).toList()) } // remove trials: 20,21,..78,79 (dataset 2,3,4)
    // create an object that will read the spikes (sorted version) and trials. Only the trials that respect the constrain are read
    val sr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD()), listOf<Trial.() -> Boolean>({ contrast == 100 }, { (orientation == 90) or (orientation == 0) }))
    val spktweMetadata = mr.readSPKTWE() // read the spike metadata associated with this recording
    exportCSV(
            process(sr,
                    constructExtractor(Perimeter(spktweMetadata.waveformInternalSamplingFrequency, spktweMetadata.waveformSpikeOffset)::extractValue),
                    BetweenStim(sr),
                    WindowValueFeatureExtractor((spktweMetadata.waveformInternalSamplingFrequency / 5.0f).toInt(), 0.5), // waveformInternalSamplingFrequency / 5 => windows of 0.2 second length. 0.5 => 50% overlap (between windows)
                    postProcessingTransformers = listOf(removeTrials, ::removeIfEmpty)) // removing units that do not contain spikes in all trials
            , "data")
}
