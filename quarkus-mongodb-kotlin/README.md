
# quarkus-mongodb-kotlin project

Demonstrate how easy is testing a mongodb entity.

Implementation for BookRecord uses [repository pattern](https://martinfowler.com/eaaCatalog/repository.html)

For the [active record pattern](https://www.martinfowler.com/eaaCatalog/activeRecord.html) have a look at [quarkus-mongodb-java](https://github.com/arolfes/quarkus-testing-java/tree/master/quarkus-mongodb-java)

quarkus and kotlin needs a default constructor for data classes. see [working-with-kotlin-data-classes](https://quarkus.io/guides/mongodb-panache#working-with-kotlin-data-classes)

_I used the ObjectId from mongodb driver instead of java.util.UUID to avoid register a converter for it._

For more details:
* [mongodb with panache](https://quarkus.io/guides/mongodb-panache)
* [flapdoodle mongodb testcontainer](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo)


### generate project structure


```
mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create \
    -DprojectGroupId=de.novatec.aqe.cloud \
    -DprojectArtifactId=quarkus-mongodb-kotlin \
    -DprojectVersion=1.0.0-SNAPSHOT \
    -DclassName="mongodb.Application" \
    -Dextensions="kotlin,mongodb-panache" \
    -DbuildTool=gradle
```