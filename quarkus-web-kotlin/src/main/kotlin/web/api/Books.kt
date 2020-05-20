package web.api

import io.quarkus.runtime.annotations.RegisterForReflection
import javax.ws.rs.core.Link

@RegisterForReflection
data class Books(val books: List<Book?>,
                 val links: List<Link>) {
}