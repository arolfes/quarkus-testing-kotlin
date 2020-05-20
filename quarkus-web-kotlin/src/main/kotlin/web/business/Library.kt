package web.business

import java.util.UUID
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class Library {

    private val database = mutableMapOf<UUID, BookRecord>()

    fun add(book: Book): BookRecord {
        val id = UUID.randomUUID()
        val bookRecord = BookRecord(id, book)
        database[id] = bookRecord
        return bookRecord
    }

    fun delete(id: UUID) {
        database.remove(id) ?: throw BookRecordNotFoundException(id)
    }

    fun get(id: UUID): BookRecord {
        return database[id] ?: throw BookRecordNotFoundException(id)
    }

    fun getAll(): List<BookRecord> {
        return database.values.toList()
    }
}