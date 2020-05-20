package web.api

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.json.bind.Jsonb
import org.junit.jupiter.api.assertThrows
import javax.validation.Validation.buildDefaultValidatorFactory
import org.assertj.core.api.Assertions.assertThat
import javax.json.bind.JsonbException

@QuarkusTest
class CreateBookRequestTest {

    private val validator = buildDefaultValidatorFactory().validator

    @Inject
    @field: Default
    lateinit var mapper: Jsonb

    @Test
    fun `can be deserialized from JSON`() {
        assertThat(read("""{"title": "Clean Code","isbn": "9780132350884"}""")).isEqualTo(
                CreateBookRequest("Clean Code", "9780132350884"
                ))
    }
    @Test
    fun `'title' property is required when deserializing`() {
        assertThrows<JsonbException> {read("""{"isbn": "9780132350884"}""")}
    }

    @Test fun `'isbn' property is required when deserializing`() {
        assertThrows<JsonbException> {read("""{ "title": "Clean Code" }""")}
    }

    private fun read(json: String) = mapper.fromJson(json, CreateBookRequest::class.java)

    @Test
    fun `'isbn' property allows 13 character ISBN`() {
        val request = CreateBookRequest("Clean Code", "9780132350884")
        assertThat(validator.validate(request)).isEmpty()
    }

    @ValueSource(strings = ["1234567890", "123456789012", "12345678901234"])
    @ParameterizedTest
    fun `'isbn' property allows only 13 character ISBN`(isbn: String) {
        val request = CreateBookRequest("My Book", isbn)
        val problems = validator.validate(request).map { it.message }

        assertThat(problems).containsOnly("""must match "[0-9]{13}"""")
    }

    @ValueSource(strings = [" ", ""])
    @ParameterizedTest
    fun `title of bookrequest can't be empty`(title: String) {
        val request = CreateBookRequest(title, "9780132350884")
        val problems = validator.validate(request).map { it.message }
        assertThat(problems).containsOnly("Title may not be blank")
    }

}