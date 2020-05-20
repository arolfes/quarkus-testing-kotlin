package web.api

import io.quarkus.runtime.annotations.RegisterForReflection
import javax.json.bind.annotation.JsonbCreator
import javax.json.bind.annotation.JsonbProperty
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@RegisterForReflection
data class CreateBookRequest @JsonbCreator constructor(
        @field:NotBlank(message = "Title may not be blank")
        val title: String,
        @field:Pattern(regexp = "[0-9]{13}")
        @field:NotNull
        val isbn: String
)