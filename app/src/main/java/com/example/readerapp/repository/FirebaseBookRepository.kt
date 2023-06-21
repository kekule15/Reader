package com.example.readerapp.repository

import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.BookModel
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseBookRepository @Inject constructor(private val query: Query) {

    suspend fun getAllBooksFromDB(): DataOrException<List<BookModel>, Boolean, Exception> {

        var dataOrException = DataOrException<List<BookModel>, Boolean, Exception>()

        try {
            dataOrException.loading = true
            dataOrException.data = query.get().await().documents.map { documentSnapshot ->
                documentSnapshot.toObject(BookModel::class.java)!!
            }
            if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false

        } catch (e: FirebaseFirestoreException) {
            dataOrException.e = e
        }
        return dataOrException

    }
}