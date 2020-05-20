package web.api

import web.business.BookRecordNotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper

import javax.ws.rs.ext.Provider

@Provider
class BookRecordNotFoundExceptionHandler : ExceptionMapper<BookRecordNotFoundException> {
    override fun toResponse(exception: BookRecordNotFoundException?) = Response
            .status(Response.Status.NOT_FOUND)
            .entity("""{"errorMessage":"${exception?.message}"}""")
            .build()
}