package com.example.bookshelf.network

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.QueryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * A public interface that exposes the [getBooks] method
 */
interface BookshelfApiService {

    companion object {
        const val BASE_URL = "https://api.punkapi.com/v2/ "
    }

    /**
     * Returns a [List] of [Book] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "volumes" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("beers")
    suspend fun getBooks(@Query("beer_name") query: String,
                         @Query("per_page") perPage: Int = 25): Response<List<Book>>

    @GET("beers/{id}")
    suspend fun getBook(@Path("id") id: Int): Response<Book>
}