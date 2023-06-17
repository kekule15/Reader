package com.example.readerapp.network

import com.example.readerapp.data.BookItem
import com.example.readerapp.data.MyBookModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksAPI {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): MyBookModel

    @GET("volumes/{bookId}")
    suspend fun getBooksInfo(@Path("bookId") bookId: String): BookItem
}