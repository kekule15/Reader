package com.example.readerapp.repository

import com.example.readerapp.data.BookItem
import com.example.readerapp.data.DataOrException
import com.example.readerapp.data.Resource
import com.example.readerapp.network.BooksAPI

import javax.inject.Inject
import kotlin.Exception

class BookRepository @Inject constructor(private val api: BooksAPI) {

//    private val dataOrException = DataOrException<List<BookItem>, Boolean, Exception>()
//
//    private val bookInfoDataOrException = DataOrException<BookItem, Boolean, Exception>()
//    suspend fun getAllBooks(searchQuery: String): DataOrException<List<BookItem>, Boolean, Exception> {
//
//        try {
//            dataOrException.loading = true
//            dataOrException.data = api.getAllBooks(searchQuery).items
//            if (dataOrException.data!!.isNotEmpty()) dataOrException.loading = false
//
//        }catch (e: Exception){
//            dataOrException.e = e
//
//        }
//        return dataOrException
//
//    }

//    suspend fun getBookInfo(bookId: String): DataOrException<BookItem, Boolean, Exception> {
//
//        try {
//            bookInfoDataOrException.loading = true
//            bookInfoDataOrException.data = api.getBooksInfo(bookId)
//            if (bookInfoDataOrException.data.toString().isNotEmpty()) bookInfoDataOrException.loading = false
//
//        }catch (e: Exception){
//            bookInfoDataOrException.e = e
//
//        }
//        return bookInfoDataOrException
//Ï
//    }

    suspend fun getAllBooks(searchQuery: String): Resource<List<BookItem>> {

        return try {
            Resource.Loading(data = true)
            val listItems = api.getAllBooks(searchQuery).items
            if (listItems.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = listItems)
        } catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<BookItem> {

        val res = try {
            Resource.Loading(data = true)
            api.getBooksInfo(bookId = bookId)

        } catch (exception: Exception) {
          return  Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = res)
    }


}