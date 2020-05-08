package mongodb.books

import de.flapdoodle.embed.mongo.MongodExecutable
import de.flapdoodle.embed.mongo.MongodStarter
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder
import de.flapdoodle.embed.mongo.config.Net
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import java.io.IOException

class MongoDbTestResource() : QuarkusTestResourceLifecycleManager {

    private lateinit var mongodb: MongodExecutable

    override fun start(): MutableMap<String, String> {
        try {
            // define explicitly localhost do run on VMs
            var net = Net("localhost", Network.getFreeServerPort(), Network.localhostIsIPv6())
            val config = MongodConfigBuilder()
                    .version(Version.Main.V4_0)
                    .net(net)
                    .build()
            mongodb = MongodStarter.getDefaultInstance().prepare(config)
            mongodb.start()
            return mutableMapOf("%test.quarkus.mongodb.connection-string" to "mongodb://" + net.bindIp + ":" + net.port)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun stop() {
        mongodb?.stop()
    }

}