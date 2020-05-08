package mongodb

import io.quarkus.runtime.Quarkus.run
import io.quarkus.runtime.annotations.QuarkusMain

fun main(args: Array<String>) {
    run(*args)
}

@QuarkusMain(name = "QuarkusMongodbPanacheApp")
class Application

