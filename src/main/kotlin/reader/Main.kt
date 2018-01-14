package reader

/**
 * Created by robert on 12/9/17.
 * The path to the folder containing the metadata files with extension ".epd", ".ssd" and ".spktwe" is expected.
 * They are not required to be all in the same folder.
 * Each one of these metadata files contains inside relative paths (relative to the corresponding metadata file) to other files.
 * Those paths are expected to be valid.
 */
fun main(args: Array<String>) {
    println(args[0])
    val metadataReader = MetadataReader(args[0])
    println(metadataReader.readEPD())
    println("\n\n")
    println(metadataReader.readSPKTWE())
    println("\n\n")
    println(metadataReader.readSSD())
}