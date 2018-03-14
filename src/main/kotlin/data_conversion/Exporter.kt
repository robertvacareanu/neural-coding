package data_conversion

import reader.orientation
import java.io.File

/**
 * Created by robert on 2/25/18.
 * Export the data in a csv manner. The results are put into a separate file
 */
fun exportCSV(data: List<Pair<Int, FloatArray>>, name: String) {
    File(name).bufferedWriter().use { br ->
        data.forEach {
            br.append(it.second.joinToString(",").plus(","))
            br.appendln(it.orientation.toString())
        }
    }
}