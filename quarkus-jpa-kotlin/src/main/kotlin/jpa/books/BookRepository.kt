package jpa.books

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class BookRepository : PanacheRepositoryBase<BookEntity, UUID> {

    fun findByTitle(title: String): List<BookEntity> = find("title", title).list()
}