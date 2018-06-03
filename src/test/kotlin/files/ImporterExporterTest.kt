package files

import exporter.exportCSV
import main.DataPoint
import main.orientation
import org.junit.Test
import java.io.File
import java.util.*

/**
 * Created by robert on 6/3/18.
 * Test for functions that are working with files
 */
class ImporterExporterTest {

    @Test
    fun importCSVExportCSVTest() {
        val dataSet = listOf(
                DataPoint(1, floatArrayOf(29.0f, 30.0f, 31.0f)),
                DataPoint(2, floatArrayOf(32.0f, 33.0f, 34.0f)),
                DataPoint(3, floatArrayOf(35.0f, 36.0f, 37.0f)),
                DataPoint(4, floatArrayOf(38.0f, 39.0f, 40.0f)),
                DataPoint(5, floatArrayOf(41.0f, 42.0f, 43.0f))
        )
        exportCSV(dataSet, "importCSVExportCSVTestFile")
        val resultDataSet = importer.read("importCSVExportCSVTestFile")

        assert(Arrays.equals(resultDataSet[0].second, dataSet[0].second))
        assert(Arrays.equals(resultDataSet[1].second, dataSet[1].second))
        assert(Arrays.equals(resultDataSet[2].second, dataSet[2].second))
        assert(Arrays.equals(resultDataSet[3].second, dataSet[3].second))
        assert(Arrays.equals(resultDataSet[4].second, dataSet[4].second))

        assert(resultDataSet[0].orientation == dataSet[0].orientation)
        assert(resultDataSet[1].orientation == dataSet[1].orientation)
        assert(resultDataSet[2].orientation == dataSet[2].orientation)
        assert(resultDataSet[3].orientation == dataSet[3].orientation)
        assert(resultDataSet[4].orientation == dataSet[4].orientation)

        File("importCSVExportCSVTestFile").delete()

    }

}