package python

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.*

/**
 * Created by robert on 3/18/18.
 * Functions for interacting with python directly from kotlin
 */
fun callPythonScript(scriptPath: String, args: Map<String, String>) {
    Runtime.getRuntime().exec("python $scriptPath ${args.keys.zip(args.values).joinToString(" ") { "${it.first} ${it.second}" }}")
}

suspend fun applyPythonScript(path: String, scriptPath: String, args: Map<String, String>) {
    async {
        val jobs = mutableListOf<Job>()
        File(path).listFiles().map { it.path }.mapTo(jobs) {
            launch {
                callPythonScript(scriptPath, args.plus(Pair("--path", it)).plus(Pair("--save", "${it.split("/").first()}/${with(Calendar.getInstance()) { "${get(Calendar.DAY_OF_MONTH)}${get(Calendar.MONTH) + 1}${get(Calendar.YEAR)}" }}_${it.split("/").last()}_${scriptPath.split("/").last().split(".").first()}.png")))
            }
        }
        jobs.forEach { it.join() }
    }.await()
}