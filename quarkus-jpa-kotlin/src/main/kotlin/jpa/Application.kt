package jpa

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.Quarkus.run
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import org.flywaydb.core.Flyway
import javax.inject.Inject

fun main(args: Array<String>) {
    run(*args)
}

@QuarkusMain(name = "QuarkusHibernatePanacheApp")
class Application

