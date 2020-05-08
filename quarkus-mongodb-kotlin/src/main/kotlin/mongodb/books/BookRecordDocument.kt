package mongodb.books

import io.quarkus.mongodb.panache.MongoEntity;
import org.bson.types.ObjectId;

@MongoEntity(collection = "book_records")
data class BookRecordDocument(
        var id: ObjectId = ObjectId.get(),
        var title: String = "",
        var isbn: String = "")