package com.example.readerapp.di

import com.example.readerapp.network.BooksAPI
import com.example.readerapp.repository.BookRepository
import com.example.readerapp.repository.FirebaseBookRepository
import com.example.readerapp.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideFirebaseBookRepository() =
        FirebaseBookRepository(query = FirebaseFirestore.getInstance().collection("books"))

    @Singleton
    @Provides
    fun provideBookRepository(api: BooksAPI) = BookRepository(api)

    @Singleton
    @Provides
    fun provideBookAPI(): BooksAPI {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(BooksAPI::class.java)
    }
}