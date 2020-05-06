package jpa.books

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import javax.enterprise.inject.Default
import javax.inject.Inject
import java.util.UUID

import org.assertj.core.api.Assertions.assertThat

@TransactionalQuarkusTest
class BookRepositoryTest() {

    @Inject
    @field: Default
    lateinit var bookRepository: BookRepository

    @BeforeEach
    fun clearDatabase() {bookRepository.deleteAll()}

    @Test
    fun `entity can be persist and found by id`() {
        val id = UUID.randomUUID()
        val entity = BookEntity(id, "Clean Code", "9780132350884")
        bookRepository.persist(entity)
        val foundEntity = bookRepository.findById(id)
        assertThat(foundEntity).isEqualTo(entity)
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
        assertThat(foundEntities)
                .contains(e1, e3)
                .doesNotContainSequence(e2)
    }

}