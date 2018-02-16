package reader

import logger.Log
import model.Spike
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.io.File
import java.io.FileOutputStream

/**
 * Created by robert on 12/9/17.
 * The path to the folder containing the metadata files with extension ".epd", ".ssd" and ".spktwe" is expected.
 * They are not required to be all in the same folder.
 * Each one of these metadata files contains inside relative paths (relative to the corresponding metadata file) to other files.
 * Those paths are expected to be valid.
 */
fun main(args: Array<String>) {
    Log.v("main", "In main now")
    println(args[0])
    val metadataReader = MetadataReader(args[0])
    println(metadataReader.readEPD())
    println("\n\n")
    println(metadataReader.readSPKTWE())
    println("\n\n")
    println(metadataReader.readSSD())
    Log.v("main", "Exit main now")
}

fun Spike.toGraph(name: String, width: Int = 500, height: Int = 500) {
    val dataset = XYSeriesCollection()
    val series = XYSeries("Data")
    for (float in 0 until waveform.size) {
        series.add(float, waveform[float])
    }
    dataset.addSeries(series)

    val ds = ChartFactory.createXYLineChart("Spike", "Sample number", "Amplitude (uv)", dataset, PlotOrientation.VERTICAL, true, true, false)

    ChartUtilities.writeChartAsPNG(FileOutputStream(File(name)), ds, width, height)
}