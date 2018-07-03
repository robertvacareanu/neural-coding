package main

import algorithm.extractor.data.AfterStim
import algorithm.extractor.data.BetweenStim
import algorithm.extractor.data.BetweenTimestamps
import algorithm.extractor.data.RandomAfterStimOn
import algorithm.extractor.feature.SingleValueFeatureExtractor
import algorithm.extractor.value.*
import algorithm.processor.*
import reader.spikes.DataSpikeReader
import exporter.exportCSV
import importer.importCSV
import model.Spike
import model.Trial
import model.metadata.SpikeMetadata
import net.sourceforge.argparse4j.ArgumentParsers
import net.sourceforge.argparse4j.impl.Arguments
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import reader.MetadataReader
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
            .defaultHelp(true)

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
        parser.addArgument("--path", "-p")
                .type(String::class.java)
                .nargs("*")
                .help("Path to the folder containing the metadata")
                .required(true)
        parser.addArgument("--export", "-e")
                .type(String::class.java)
                .help("Path to the where to store the result file")
                .required(true)
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
        addArgument("--rason")
                .required(false)
                .type(Int::class.java)
                .nargs(2)
                .help("Use random after stim on as data extractor, e.g. 2240, 9600")
    }

    val subparsers = parser.addSubparsers().help("scripts")

    val destructiveFunctionsParser = subparsers.addParser("ds").help("Destructive scripts")

    with(destructiveFunctionsParser) {
        addArgument("--rie")
                .required(false)
                .action(Arguments.storeTrue())
                .help("removeIfEmpty")
        addArgument("--rt")
                .required(false)
                .type(Int::class.java)
                .help("removeTrials")
        addArgument("--ru")
                .required(false)
                .type(Int::class.java)
                .help("removeUnits")
        addArgument("--rtwlf")
                .required(false)
                .type(Int::class.java)
                .help("removeTrialsWithLeastFeatures")
        addArgument("--balance")
                .required(false)
                .action(Arguments.storeTrue())
                .help("removeTrialsWithLeastFeatures")
        addArgument("--rfo")
                .required(false)
                .action(Arguments.storeTrue())
                .help("removeFirstOccurrence")
    }

    val constructiveFunctionsParser = subparsers.addParser("cs").help("Constructive scripts")

    with(constructiveFunctionsParser) {
        addArgument("--n")
                .required(false)
                .action(Arguments.storeTrue())
                .help("normalize")
        addArgument("--ah")
                .required(false)
                .action(Arguments.storeTrue())
                .help("aggregateHorizontally")
        addArgument("--av")
                .required(false)
                .action(Arguments.storeTrue())
                .help("aggregateVertically")
    }

    try {
        val namespace = parser.parseArgs(args)
        print(namespace)
        if (!namespace.attrs.containsKey("path")) {
            parser.printHelp()
        } else {
            val paths = namespace.getList<String>("path")

            fun produceDataSet(path: String): DataSet {
                val mr = MetadataReader(path)
                val spikeReader = if (namespace.getBoolean("sorted")!!) DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD()), listOf<Trial.() -> Boolean>({ contrast == 100 })) else DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()), listOf<Trial.() -> Boolean>({ contrast == 100 }))
                val fe = SingleValueFeatureExtractor()
                val c = valueExtractors[namespace.getString("value_extractor").toLowerCase()]!!.constructors[1]
                val spktwe = mr.readSPKTWE()
                val ve = if (c.parameterCount == 2) c.newInstance(spktwe.waveformSpikeOffset, 0f) else c.newInstance(spktwe.waveformInternalSamplingFrequency, spktwe.waveformSpikeOffset, 0f)
                var dataset = if (namespace.getList<Int>("rason") != null) {
                    val ints = namespace.getList<Int>("rason")
                    process(spikeReader, ve as ValueExtractor<Spike, Float>, RandomAfterStimOn(spikeReader, ints[0], ints[1]), fe)
                } else {
                    process(spikeReader, ve as ValueExtractor<Spike, Float>, reader[namespace.getString("reader").toLowerCase()]!!.constructors[0].newInstance(spikeReader) as BetweenTimestamps, fe)
                }

                if (namespace.getBoolean("rie")) {
                    dataset = removeIfEmpty(dataset)
                }
                if (namespace.getInt("rt") != null) {
                    dataset = removeTrials(dataset, namespace.getInt("rt"))
                }
                if (namespace.getInt("ru") != null) {
                    dataset = removeUnits(dataset, namespace.getInt("ru"))
                }
                if (namespace.getInt("rtwlf") != null) {
                    dataset = removeTrialsWithLeastFeatures(dataset, namespace.getInt("rtwlf"))
                }
                if (namespace.getBoolean("balance")) {
                    dataset = balance(dataset)
                }
                if (namespace.getBoolean("rfo")) {
                    dataset = removeFirstOccurence(dataset)
                }

                return dataset
            }

            when {
                paths.size > 1 -> {
                    println("Bigger than 2")
                    val datasets = mutableListOf<DataSet>()
                    paths.mapTo(datasets) { importCSV(it) }

                    when {
                        namespace.getBoolean("ah") -> exportCSV(aggregateHorizontally(datasets), namespace.getString("export"))
                        namespace.getBoolean("av") -> exportCSV(aggregateVertically(datasets), namespace.getString("export"))
                        else -> paths.mapTo(datasets) {
                            produceDataSet(it)
                        }
                    }
                    exportCSV(aggregateVertically(datasets), namespace.getString("export"))

                }
                paths.size == 1 -> exportCSV(produceDataSet(paths[0]), namespace.getString("export"))
                else -> println("Should be at least 1")
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
////        parser.printHelp()
////        println("HERE")
    }

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