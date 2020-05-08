package mongodb.books

import io.quarkus.mongodb.panache.PanacheMongoRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookRecordRepository() : PanacheMongoRepository<BookRecordDocument> {

    fun findByTitle(title: String): MutableList<BookRecordDocument> = list("title", title)
}