package main

import algorithm.extractor.data.AfterStim
import algorithm.extractor.data.AfterStimOn
import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.feature.FeatureExtractor
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.value.*
import algorithm.processor.process
import exporter.exportCSV
import model.Spike
import model.TrialData
import model.metadata.SpikeMetadata
import reader.spikes.DataSpikeReader
import reader.MetadataReader
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.impl.Arguments
import net.sourceforge.argparse4j.inf.ArgumentParserException
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.io.File
import kotlin.math.abs

/**
 * Created by robert on 12/9/17.
 * The path to the folder containing the metadata files with extension ".epd", ".ssd" and ".spktwe" is expected.
 * They are not required to be all in the same folder.
 * Each one of these metadata files contains inside relative paths (relative to the corresponding metadata file) to other files.
 * Those paths are expected to be valid.
 */
fun main(args: Array<String>) {
    val parser = ArgumentParsers.newFor("Neural-Coding")
            .build()
            .description("Arguments for generating the feature file")

    val valueExtractors = mapOf(
            "meanamplitude" to MeanAmplitude::class.java,
            "ma" to MeanAmplitude::class.java,
            "meanamplitudeoffirstspike" to MeanAmplitudeOfFirstSpike::class.java,
            "mafs" to MeanAmplitudeOfFirstSpike::class.java,
            "meanarea" to MeanArea::class.java,
            "marea" to MeanArea::class.java,
            "meanperimeter" to MeanPerimeter::class.java,
            "mp" to MeanPerimeter::class.java,
            "meanwidth" to MeanWidth::class.java,
            "mw" to MeanWidth::class.java
    )

    val featureExtractors = mapOf(
            "singlevalueextractor" to SingleValueFeatureExtractor::class.java,
            "sve" to SingleValueFeatureExtractor::class.java
    )

    val reader = mapOf(
            "betweenstim" to BetweenStim::class.java,
            "bs" to BetweenStim::class.java,
            "afterstim" to AfterStim::class.java,
            "as" to BetweenStim::class.java
    )

    with(parser) {
        addArgument("--path", "-p")
                .type(String::class.java)
                .help("Path to the folder containing the metadata")
        addArgument("--export", "-e")
                .type(String::class.java)
                .help("Path to the where to store the result file")
        addArgument("--reader", "-r")
                .type(String::class.java)
                .required(false)
                .setDefault("BetweenStim")
                .help("Type of spike reader to use. Default is BetweenStim")
        addArgument("--value-extractor", "-ve")
                .type(String::class.java)
                .required(false)
                .setDefault("MeanAmplitude")
                .help("Type of value extractor to use. Default is MeanAmplitude")
        addArgument("--feature-extractor", "--fe")
                .type(String::class.java)
                .required(false)
                .setDefault("SingleValueExtractor")
                .help("Type of feature extractor to use. Default is SingleValueExtractor")
        addArgument("--sorted", "-s")
                .action(Arguments.storeTrue())
                .help("Process on sorted data")
    }
    val namespace = parser.parseArgs(args)
    println(namespace)
    with(namespace) {
        if(attrs["value_extractor"]!!.toString().toLowerCase() !in valueExtractors) {
            parser.handleError(ArgumentParserException("Value extractor not valid. Should be one of: ${valueExtractors.keys.joinToString()}. Not case sensitive", parser))
        }
        if(attrs["feature_extractor"]!!.toString().toLowerCase() !in featureExtractors) {
            parser.handleError(ArgumentParserException("Feature extractor not valid. Should be one of: ${featureExtractors.keys.joinToString()}. Not case sensitive", parser))
        }
        if(attrs["reader"]!!.toString().toLowerCase() !in reader) {
            parser.handleError(ArgumentParserException("Reader not valid. Should be one of: ${reader.keys.joinToString()}. Not case sensitive", parser))
        }
    }
    val mr = MetadataReader(namespace["path"])
    val spktwe = mr.readSPKTWE()
    val dsr = if(namespace["sorted"]) DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD())) else DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

    val c = valueExtractors[namespace["value_extractor"]]!!.constructors[1]
    if((c.parameterCount != 2) and (c.parameterCount != 3)) {
        return
    }
    val ve = if(c.parameterCount == 2) c.newInstance(spktwe.waveformSpikeOffset, 0f) else c.newInstance(spktwe.waveformInternalSamplingFrequency, spktwe.waveformSpikeOffset, 0f)
    exportCSV(
            process(dsr, ve as ValueExtractor<Spike, Float>, reader[namespace["reader"]]!!.constructors[0].newInstance(dsr) as BetweenTimestamps, featureExtractors[namespace.getString("feature_extractor")!!.toLowerCase()]!!.newInstance() as FeatureExtractor<TrialData, DataSet>),
            namespace["export"]
    )

}

/**
 * A utility function that outputs an image representing the spike
 */
fun Spike.toGraph(name: String, width: Int = 500, height: Int = 500) {
    val dataset = XYSeriesCollection()
    val series = XYSeries("Data")
    for (float in 0 until waveform.size) {
        series.add(float, waveform[float])
    }
    dataset.addSeries(series)

    val ds = ChartFactory.createXYLineChart("Spike", "Sample number", "Amplitude (uv)", dataset, PlotOrientation.VERTICAL, true, true, false)

    ChartUtilities.saveChartAsPNG(File(name), ds, width, height)
}

typealias DataPoint = Pair<Int, FloatArray>
typealias DataSet = List<DataPoint>

val DataSet.numberOfUnits: Int
    get() = firstOrNull()?.second?.size ?: 0

/**
 * A utility function to avoid calls like: data[0].second[3]
 */
operator fun Pair<Int, FloatArray>.get(index: Int) = second[index]

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

/**
 * Providing an alias for first
 * It improves readability
 */
val Pair<Int, FloatArray>.orientation: Int
    get() = first

/**
 * Providing an alias for first
 * It improves readability
 */
val Pair<Int, FloatArray>.values: FloatArray
    get() = second

/**
 * Commonly used in tests to compare doubles
 * Be aware of the precision of the used type
 */
infix fun Number.almostEqual(number: Number): Boolean {
    return abs(this.toDouble() - number.toDouble()) < 0.00001
}