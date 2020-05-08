package mongodb.books

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.enterprise.inject.Default
import javax.inject.Inject

@QuarkusTest
@QuarkusTestResource(MongoDbTestResource::class)
class BookRecordRepositoryTest() {

    @Inject
    @field: Default
    private lateinit var bookRepository: BookRecordRepository

    @BeforeEach
    fun clearDatabase() {
        bookRepository.deleteAll()
    }

    @Test
    fun `entity can be persist and found by id`() {

        val id = ObjectId.get()
        val entity = BookRecordDocument(id, "Clean Code", "9780132350884")
        bookRepository.persist(entity)
        val foundEntity = bookRepository.findById(id)
        assertThat(foundEntity).isEqualToComparingFieldByField(entity)
    }

    @Test
    fun `entity can be found by title`() {

        val e1 = BookRecordDocument(ObjectId.get(), "Clean Code", "9780132350884")
        bookRepository.persist(e1)
        val e2 = BookRecordDocument(ObjectId.get(), "Clean Architecture", "9780134494166")
        bookRepository.persist(e2)
        val e3 =BookRecordDocument(ObjectId.get(), "Clean Code", "9780132350884")
        bookRepository.persist(e3)


        val foundEntities = bookRepository.findByTitle("Clean Code")
        assertThat(foundEntities)
                .contains(e1, e3)
                .doesNotContainSequence(e2)
    }
}