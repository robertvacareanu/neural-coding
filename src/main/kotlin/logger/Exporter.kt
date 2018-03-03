package logger

import java.io.File

/**
 * Created by robert on 2/25/18.
 * Export the data in a csv manner. The results are put into a separate file
 */
fun exportCSV(data: List<Pair<Int, FloatArray>>, name: String) {
    File(name).bufferedWriter().use { br ->
        data.forEach {
            br.appendln(it.second.joinToString(","))
        }
    }
    File("${name}_Results").bufferedWriter().use { br ->
        br.append(data.map { it.first/45.0 }.joinToString())

    }
}