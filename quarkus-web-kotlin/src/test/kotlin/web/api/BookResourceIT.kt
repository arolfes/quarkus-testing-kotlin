package web.api

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.http.ContentType.JSON
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status.*

@QuarkusTest
class BookResourceIT {

    @Test
    fun `Creating a book, getAllBooks, deleteBook, getAllBooks`() {
        val extract = given()
                .`when`()
                .contentType(JSON)
                .body("""{"title":"Clean Code", "isbn":"9780132350884"}""")
                .post("/api/books")
                .then()
                .log().all()
                .statusCode(CREATED.statusCode)
                .contentType(JSON)
                .body("title", `is`("Clean Code"))
                .body("isbn", `is`("9780132350884"))
                .extract()
        val uriAsString = extract.body().jsonPath().get<String>("links[0].uri")
        given()
                .`when`()["/api/books"]
                .then()
                .log().all()
                .statusCode(OK.statusCode)
                .contentType(JSON)
                .body("books[0].title", `is`("Clean Code"))
                .body("books[0].isbn", `is`("9780132350884"))
                .body("books[0].links[0].rel", `is`("self"))
                .body("books[0].links[0].uri", `is`(uriAsString))
        val uuid = uriAsString.substring(uriAsString.lastIndexOf('/') + 1)
        given()
                .`when`()["/api/books/{id}", uuid]
                .then()
                .log().all()
                .statusCode(OK.statusCode)
                .contentType(JSON)
                .body("title", `is`("Clean Code"))
                .body("isbn", `is`("9780132350884"))
                .body("links[0].rel", `is`("self"))
                .body("links[0].uri", `is`(uriAsString))
        given()
                .`when`()
                .delete("/api/books/{id}", uuid)
                .then()
                .log().all()
                .statusCode(NO_CONTENT.statusCode)
        given()
                .`when`()["/api/books"]
                .then()
                .log().all()
                .statusCode(OK.statusCode)
                .contentType(JSON)
                .body("books", empty<Any>())
                .body("links[0].rel", `is`("self"))
                .body("links[0].uri", endsWith("/api/books"))
    }
}