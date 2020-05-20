package web.api

import java.util.*
import javax.validation.ConstraintViolationException
import javax.validation.Path
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.core.Response.status
import javax.ws.rs.ext.Provider

@Provider
class BeanValidationExceptionMapper : ExceptionMapper<ConstraintViolationException> {
    override fun toResponse(exception: ConstraintViolationException): Response {
        val errors: MutableMap<String?, String> = HashMap()
        exception.constraintViolations
                .forEach { v -> errors[lastFieldName(v.propertyPath.iterator())] = v.message }
        return status(Response.Status.BAD_REQUEST).entity(errors).build()
    }

    private fun lastFieldName(nodes: Iterator<Path.Node>): String {
        var last: Path.Node? = null
        while (nodes.hasNext()) {
            last = nodes.next()
        }
        return last!!.getName()
    }
}