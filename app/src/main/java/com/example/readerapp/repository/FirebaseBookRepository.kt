package com.example.readerapp.repository

import android.util.Log
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.BookModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject

class FirebaseBookRepository @Inject constructor(private val query: Query) {

    suspend fun getAllBooksFromDB(): DataOrException<List<BookModel>, Boolean, Exception> {

        val dataOrException = DataOrException<List<BookModel>, Boolean, Exception>()

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

     suspend fun getUser(): Flow<DataOrException<List<BookModel>, Boolean, Exception>> =
        callbackFlow {

            val dataOrException = DataOrException<List<BookModel>, Boolean, Exception>()

            val listener = object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshot: QuerySnapshot?,
                    exception: FirebaseFirestoreException?,
                ) {
                    dataOrException.loading = true
                    if (exception != null) {
                        // An error occurred
                        //cancel()
                        Log.w("Firebase Event", "Listen failed.", exception)
                        dataOrException.loading = false
                        dataOrException.e = exception
                        return
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        Log.w("Firebase Event", "Listen Succeeded.")
                        // The user document has data
                        //val user = snapshot.toObject(BookModel::class.java)
                        dataOrException.data = snapshot.documents.map {
                                documentSnapshot ->
                            documentSnapshot.toObject(BookModel::class.java)!!
                        }
                        Log.w("Firebase Event", "Listen Succeeded with data. ${ dataOrException.data}")
                        if (!dataOrException.data.isNullOrEmpty()) dataOrException.loading = false

                        trySend(dataOrException)
                    } else {
                        dataOrException.loading = false
                        // The user document does not exist or has no data
                    }
                }
            }


            val registration = query.addSnapshotListener(listener)
            awaitClose { registration.remove() }
        }

}