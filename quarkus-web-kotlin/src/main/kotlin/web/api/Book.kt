package web.api

import io.quarkus.runtime.annotations.RegisterForReflection
import javax.ws.rs.core.Link

@RegisterForReflection
data class Book(
                val title: String,
                val isbn: String,
                val links: List<Link>
) {
}