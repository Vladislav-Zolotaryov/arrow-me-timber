package arrow.me.timber.extension

interface JsonScope<T> {    // <- dispatcher receiver
    fun T.toJson(): String  // <- extension function receiver
    // 'this' type in 'toJson' function is JsonScope<T> & T
}

fun <T> JsonScope<T>.printAsJson(objs: List<T>) =
    objs.joinToString(separator = ", ", prefix = "[", postfix = "]") { it.toJson() }

data class Job(val name: String)

val jobJsonScope = object : JsonScope<Job> {
    override fun Job.toJson(): String {
        return """
            {
                "name": $name
            }
        """.trimIndent()
    }
}

val JOBS_DATABASE = listOf(Job("Manager"), Job("Not Manager"))

fun main() {
    with(jobJsonScope) {
        println(printAsJson(JOBS_DATABASE.toList()))
    }
}