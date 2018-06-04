package importer

import main.DataPoint
import main.DataSet
import java.io.File

/**
 * Created by robert on 5/30/18.
 * Read the file from the given path into a dataSet
 */
fun importCSV(path: String): DataSet {
    val file = File(path)
    val result = mutableListOf<DataPoint>()
    file.readLines().mapTo(result) {
        val values = it.split(",")
        DataPoint(values.last().toInt(), values.dropLast(1).map { it.toFloat() }.toFloatArray())
    }

    return result
}