package jpa.books

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.h2.H2DatabaseTestResource
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject

@TransactionalQuarkusTest
@QuarkusTestResource(H2DatabaseTestResource::class)
class BookRepositoryWithH2Test() {

    @Inject
    @field: javax.enterprise.inject.Default
    lateinit var bookRepository: BookRepository

    companion object {

        @BeforeAll
        @JvmStatic
        fun setProps() {
            System.setProperty("%test.quarkus.datasource.driver", "org.h2.Driver")
            System.clearProperty("%test.quarkus.hibernate-orm.dialect")
            System.setProperty("%test.quarkus.datasource.url", "jdbc:h2:tcp://localhost/mem:test")
        }

        @AfterAll
        @JvmStatic
        fun removeProps() {
            System.setProperty("%test.quarkus.datasource.driver", "org.testcontainers.jdbc.ContainerDatabaseDriver")
            System.setProperty("%test.quarkus.hibernate-orm.dialect", "org.hibernate.dialect.PostgreSQL10Dialect")
            System.setProperty("%test.quarkus.datasource.url", "jdbc:tc:postgresql:latest:///dbname")
        }
    }

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