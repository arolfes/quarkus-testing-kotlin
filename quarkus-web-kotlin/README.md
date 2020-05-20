# quarkus-web-kotlin project

Demonstrate testing of REST service.

In this example we use jsonb, resteasy, hibernate-validater for implementing REST-Service.

For testing we use junit5, mockito, rest-assured. When you have installed a GraalVM you can also see how an native image is tested.

_The library itself is not tested, because it is there to demonstrate how to mock_


For more details:
* [json-rest](https://quarkus.io/guides/rest-json)
* [validation](https://quarkus.io/guides/validation)
* [openapi](https://quarkus.io/guides/openapi-swaggerui)
* [mocking](https://quarkus.io/guides/getting-started-testing)

### generate project

```
mvn io.quarkus:quarkus-maven-plugin:1.4.2.Final:create \
    -DprojectGroupId=de.novatec.aqe.cloud \
    -DprojectArtifactId=quarkus-web-kotlin \
    -DprojectVersion=1.0.0-SNAPSHOT \
    -DclassName="web.api.BookResource" \
    -Dextensions="kotlin,quarkus-resteasy,quarkus-resteasy-jsonb,quarkus-smallrye-openapi,quarkus-hibernate-validator" \
    -DbuildTool=gradle
```

## Creating a native executable

for building and testing a native executable follow this guide: [Building a Native Executable](https://quarkus.io/guides/building-native-image)

In this articel you learn how to handle reflection classes, proxy classes, third party libs etc: [Tips for writing native applications](https://quarkus.io/guides/writing-native-applications-tips)

You can create a native executable using: `./gradlew build -Dquarkus.package.type=native`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./build/quarkus-web-kotlin-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.