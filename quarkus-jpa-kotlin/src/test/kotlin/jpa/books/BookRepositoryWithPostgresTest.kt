package jpa.books

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import javax.inject.Inject

@Testcontainers
@TransactionalQuarkusTest
class BookRepositoryWithPostgresTest {

    companion object {
        @Container
        private val db = PostgreSQLContainer<Nothing>("postgres").apply {
            withDatabaseName("quarkusjdbc")
            withUsername("quarkus")
            withPassword("changeme")
        }

        @BeforeAll
        @JvmStatic
        fun setProps() {
            db.waitingFor(HostPortWaitStrategy())
            db.start()
            System.setProperty("%test.quarkus.datasource.driver", db.driverClassName)
            System.clearProperty("%test.quarkus.hibernate-orm.dialect")
            System.setProperty("%test.quarkus.datasource.url", db.jdbcUrl)
            System.setProperty("%test.quarkus.datasource.username", db.username)
            System.setProperty("%test.quarkus.datasource.password", db.password)
        }

        @AfterAll
        @JvmStatic
        fun removeProps() {
            System.setProperty("%test.quarkus.datasource.driver", "org.testcontainers.jdbc.ContainerDatabaseDriver")
            System.setProperty("%test.quarkus.hibernate-orm.dialect", "org.hibernate.dialect.PostgreSQL10Dialect")
            System.setProperty("%test.quarkus.datasource.url", "jdbc:tc:postgresql:latest:///dbname")
            System.clearProperty("%test.quarkus.datasource.username")
            System.clearProperty("%test.quarkus.datasource.password")
            if (db.isCreated) {
                db.close()
            }
        }
    }

    @Inject
    @field: javax.enterprise.inject.Default
    lateinit var bookRepository: BookRepository

    @BeforeEach
    fun clearDatabase() {bookRepository.deleteAll()}

    @Test
    fun `entity can be persist and found by id`() {
        val id = java.util.UUID.randomUUID()
        val entity = jpa.books.BookEntity(id, "Clean Code", "9780132350884")
        bookRepository.persist(entity)
        val foundEntity = bookRepository.findById(id)
        org.assertj.core.api.Assertions.assertThat(foundEntity).isEqualTo(entity)
    }

    @Test
    fun `entity can be found by title`() {

        val e1 = BookEntity(UUID.randomUUID(), "Clean Code", "9780132350884")
        bookRepository.persist(e1)
        val e2 = BookEntity(UUID.randomUUID(), "Clean Architecture", "9780134494166")
        bookRepository.persist(e2)
        val e3 =BookEntity(UUID.randomUUID(), "Clean Code", "9780132350884")
        bookRepository.persist(e3)


        val foundEntities = bookRepository.findByTitle("Clean Code")
        Assertions.assertThat(foundEntities)
                .contains(e1, e3)
                .doesNotContainSequence(e2)
    }

}