package algorithm.extractor.data

/**
 * Created by robert on 2/15/18.
 * Responsible for getting the desired data from the whole signal
 */
interface DataExtractor<in RAW, out PROCESSED> {
    fun extractData(data: RAW): PROCESSED
}