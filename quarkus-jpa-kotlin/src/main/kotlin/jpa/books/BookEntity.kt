package jpa.books

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity(name = "BookRecord")
@Table(name = "book_records")
data class BookEntity(
        @Id
        var id: UUID,
        var title: String,
        var isbn: String
)