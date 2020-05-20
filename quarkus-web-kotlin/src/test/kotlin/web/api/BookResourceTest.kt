package web.api

import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.mockito.InjectMock
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.reset
import org.mockito.invocation.InvocationOnMock
import web.business.*
import web.business.Book
import java.util.*
import java.util.UUID.randomUUID
import javax.ws.rs.core.Response


@QuarkusTest
class BookResourceTest {

    @InjectMock
    lateinit var library: Library

    private val bookCleanCode = Book(Title("Clean Code"), Isbn("9780132350884"))

    @BeforeEach
    fun clearLibrary() {
        reset(library)
    }
    @Test
    @DisplayName("GET /api/books - getting all books when there are none returns empty resource list")
    fun testGetBooksEmpty() {
        `when`(library.getAll()).thenReturn(emptyList())
        given()
                .`when`()["/api/books"]
                .then()
                .log().all()
                .statusCode(Response.Status.OK.statusCode)
                .contentType(ContentType.JSON)
                .body("books", empty<Any>())
                .body("links[0].rel", `is`("self"))
                .body("links[0].uri", endsWith("/api/books"))
    }

    @Test
    @DisplayName("GET /api/books - getting all books returns their resource representation as a resource list")
    fun testGetBooks() {
        val bookRecordCleanCode = BookRecord(randomUUID(), bookCleanCode)
        val bookRecordCleanArchitecture = BookRecord(randomUUID(), Book(Title("Clean Architecture"), Isbn("9780134494166")))
        `when`(library.getAll()).thenReturn(listOf(bookRecordCleanCode, bookRecordCleanArchitecture))
        given()
                .`when`()["/api/books"]
                .then()
                .log().all()
                .statusCode(Response.Status.OK.statusCode)
                .contentType(ContentType.JSON)
                .body("books[0].title", `is`("${bookCleanCode.title}"))
                .body("books[0].isbn", `is`("${bookCleanCode.isbn}"))
                .body("books[0].links[0].rel", `is`("self"))
                .body("books[0].links[0].uri", endsWith("/api/books/${bookRecordCleanCode.id}"))
                .body("books[1].title", `is`("${bookRecordCleanArchitecture.book.title}"))
                .body("books[1].isbn", `is`("${bookRecordCleanArchitecture.book.isbn}"))
                .body("books[1].links[0].rel", `is`("self"))
                .body("books[1].links[0].uri", endsWith("/api/books/${bookRecordCleanArchitecture.id}"))
                .body("links[0].rel", `is`("self"))
                .body("links[0].uri", endsWith("/api/books"))
    }

    @Test
    @DisplayName("GET /api/books/{id} - getting a book by its id returns resource representation")
    fun testGetBookById() {
        val bookRecordCleanCode = BookRecord(randomUUID(), bookCleanCode)
        `when`(library.get(bookRecordCleanCode.id)).thenReturn(bookRecordCleanCode)
        given()
                .`when`()["/api/books/{id}", bookRecordCleanCode.id]
                .then()
                .log().all()
                .statusCode(Response.Status.OK.statusCode)
                .contentType(ContentType.JSON)
                .body("title", `is`("${bookCleanCode.title}"))
                .body("isbn", `is`("${bookCleanCode.isbn}"))
                .body("links[0].rel", `is`("self"))
                .body("links[0].uri", endsWith("/api/books/${bookRecordCleanCode.id}"))
    }

    @Test
    @DisplayName("GET /api/books/{id} - getting an unknown book by its id responds with status 404")
    fun testGetUnknownBookId() {
        `when`(library.get(any(UUID::class.java))).thenAnswer { i: InvocationOnMock -> throw BookRecordNotFoundException(i.getArgument(0)) }
        val id = randomUUID()
        given()
                .`when`()["/api/books/{id}", id]
                .then()
                .log().all()
                .statusCode(Response.Status.NOT_FOUND.statusCode)
                .contentType(ContentType.JSON)
                .body("errorMessage", `is`("Book [$id] not found!"))
    }

    @Test
    @DisplayName("POST /api/books - posting a book adds it to the library and returns resource representation")
    fun testPostBookAddToLibraryAndReturnBook() {
        val id = randomUUID()
        `when`(library.add(any(Book::class.java))).thenAnswer { i -> BookRecord(id, i.getArgument(0)) }
        given()
                .`when`()
                .contentType(ContentType.JSON)
                .body("""{"title":"Clean Code","isbn":"9780132350884"}""")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.CREATED.statusCode)
                .contentType(ContentType.JSON)
                .body("title", `is`("Clean Code"))
                .body("isbn", `is`("9780132350884"))
                .body("links[0].rel", `is`("self"))
                .body("links[0].uri", endsWith("/api/books/$id"))
    }

    @Test
    @DisplayName("POST /api/books - posting a book with a malformed 'isbn' property responds with status 400")
    fun testPostBookMalformedIsbn() {
        given()
                .`when`()
                .contentType(ContentType.JSON)
                .body("""{"title":"Clean Code","isbn":"0132350884"}""")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.statusCode)
    }

    @Test
    @DisplayName("POST /api/books - posting a book with blank 'title' property responds with status 400")
    fun testPostBookBlankTitle() {
        given()
                .`when`()
                .contentType(ContentType.JSON)
                .body("""{"title":" ","isbn":"9780132350884"}""")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.statusCode)
    }

    @Test
    @DisplayName("POST /api/books - posting a book with missing 'title' property responds with status 400")
    fun testPostBookMissingTitle() {
        given()
                .`when`()
                .contentType(ContentType.JSON)
                .body("""{"isbn":"9780132350884"}""")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.statusCode)
    }

    @Test
    @DisplayName("POST /api/books - posting a book with missing 'isbn' property responds with status 400")
    fun testPostBookMissingIsbn() {
        given()
                .`when`()
                .contentType(ContentType.JSON)
                .body("""{"title":"Clean Code"}""")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(Response.Status.BAD_REQUEST.statusCode)
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - deleting a book by its id returns status 204")
    fun testDeleteBook() {
        val id = randomUUID()
        given()
                .`when`()
                .delete("/api/books/$id")
                .then()
                .log().all()
                .statusCode(Response.Status.NO_CONTENT.statusCode)
    }

    @Test
    @DisplayName("DELETE /api/books/{id} - deleting an unknown book by its id responds with status 404")
    fun testDeleteBookNotFound() {
        val id = randomUUID()
        doAnswer { i: InvocationOnMock -> throw BookRecordNotFoundException(i.getArgument(0)) }.`when`(library).delete(any(UUID::class.java))
        given()
                .`when`()
                .delete("/api/books/$id")
                .then()
                .log().all()
                .statusCode(Response.Status.NOT_FOUND.statusCode)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}