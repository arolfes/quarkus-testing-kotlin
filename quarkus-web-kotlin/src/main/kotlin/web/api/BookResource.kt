package web.api

import web.business.BookRecord
import web.business.Isbn
import web.business.Library
import web.business.Title
import java.util.*
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.validation.Valid
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.Link.fromUriBuilder
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo


@Path("/api/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class BookResource {

    @Inject
    @field: Default
    lateinit var library: Library

    @GET
    fun list(@Context uriInfo: UriInfo): Response? {
        val bookResourceList = library.getAll().map { mapBookRecord2BookApi(uriInfo, it) }
        val booksResource = Books(bookResourceList, listOf(
                fromUriBuilder(uriInfo.absolutePathBuilder).rel("self").type(MediaType.APPLICATION_JSON).build()
        ))
        return Response
                .ok(booksResource)
                .build()
    }


    @GET
    @Path("{id}")
    operator fun get(@Context uriInfo: UriInfo?, @PathParam("id") id: UUID?): Response? {
        return Response
                .ok(mapBookRecord2BookApi(uriInfo!!, library.get(id!!)))
                .build()
    }

    @POST
    fun add(@Context uriInfo: UriInfo?, @Valid cbr: CreateBookRequest): Response? {
        val bookRecord = library.add(
                web.business.Book(Title(cbr.title), Isbn(cbr.isbn)))
        return Response
                .status(Response.Status.CREATED)
                .entity(mapBookRecord2BookApi(uriInfo!!, bookRecord))
                .build()
    }

    @DELETE
    @Path("{id}")
    fun delete(@PathParam("id") id: UUID): Response? {
        library.delete(id)
        return Response.noContent().build()
    }

    private fun mapBookRecord2BookApi(@Context uriInfo: UriInfo, bookRecord: BookRecord): Book? {
        val absolutePathBuilder = uriInfo.absolutePathBuilder.replacePath("/api/books").path(bookRecord.id.toString())
        val links = listOf(
                fromUriBuilder(absolutePathBuilder).rel("self").type(MediaType.APPLICATION_JSON).build()
        )
        return Book(bookRecord.book.title.toString(), bookRecord.book.isbn.toString(), links)
    }

}