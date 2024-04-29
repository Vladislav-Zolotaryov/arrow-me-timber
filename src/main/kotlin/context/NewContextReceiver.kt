package arrow.me.timber.context


interface JsonScope<T> {
    fun T.toJson(): String
}

interface Logger {
    fun log(any: Any)
}

val loggerScope = object : Logger {
    override fun log(any: Any) {
        println(any)
    }
}

context (JsonScope<T>, Logger)
fun <T> printAsJson(objs: List<T>) =
    objs.joinToString(separator = ", ", prefix = "[", postfix = "]") {
        val result = it.toJson()
        log(result)
        result
    }

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

fun <T, R> withJobAndLog(receiver: T, block: T.() -> R) : R  {
    with(jobJsonScope) {
        with(loggerScope) {
            block.invoke(receiver)
        }
    }
}

fun main() {
    withJobAndLog<Job, Unit> {
        printAsJson(JOBS_DATABASE)
    }
}