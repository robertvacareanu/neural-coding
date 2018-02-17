package algorithm.extractor.data

/**
 * Created by robert on 2/15/18.
 * Responsible for getting the desired data from the whole signal.
 * Make use of the fact that each trial consists of exactly 4 event codes:
 * Trial start, Stim ON, Stim OFF, Trial end
 */
interface DataExtractor<in RAW, out PROCESSED> {
    fun extractData(data: RAW): PROCESSED
}