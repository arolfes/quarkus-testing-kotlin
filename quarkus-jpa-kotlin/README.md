# quarkus-jpa-kotlin project

demonstrate quarkus panache with flyway and 2 different test containers.

`jpa.books.BookRepositoryTest` starts a generic db Container before the test is run and runs flyway when container is started. 

`jpa.books.BookRepositoryWithH2Test` starts a H2 in memory Container before the test is run and runs flyway when in memory db is ready .

`jpa.books.BookRepositoryWithPostgresTest` starts lates postgres test container before the test is run and runs flyway when in postgres container is ready .

in application.properties are 3 properties defined which are only used on test stage. 
```
# starts a generic db container during test
%test.quarkus.datasource.driver=org.testcontainers.jdbc.ContainerDatabaseDriver
# dialect must be set explicitly
%test.quarkus.hibernate-orm.dialect=org.hibernate.dialect.PostgreSQL10Dialect
# Testcontainers JDBC URL
# leads to a Postgres DB Container
%test.quarkus.datasource.url=jdbc:tc:postgresql:latest:///dbname
```