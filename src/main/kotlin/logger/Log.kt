package logger

import java.io.File

/**
 * Created by robert on 1/28/18.
 */
class Log {
    companion object {
        private val file: File by lazy { File("logv${System.currentTimeMillis()}") }
        fun v(tag: String, text: String) {
            file.appendText("$tag: $text\n")
        }
    }
}