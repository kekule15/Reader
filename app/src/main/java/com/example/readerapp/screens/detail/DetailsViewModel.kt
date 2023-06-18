package com.example.readerapp.screens.detail

import androidx.lifecycle.ViewModel
import com.example.readerapp.data.BookItem
import com.example.readerapp.data.Resource
import com.example.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(private val repository: BookRepository) : ViewModel() {

    suspend fun getBook(bookId:String):Resource<BookItem>{
        return repository.getBookInfo(bookId)
    }

}