package com.example.readerapp.screens.search

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.data.BookItem
import com.example.readerapp.data.DataOrException
import com.example.readerapp.data.Resource
import com.example.readerapp.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookSearchViewModel @Inject constructor(private val repository: BookRepository) :
    ViewModel() {

   var list: List<BookItem> by mutableStateOf(listOf())

    var isLoading :Boolean by mutableStateOf(true)

    init {
        loadBooks()
    }

    private fun loadBooks() {
        searchBooks("flutter")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch(Dispatchers.Default) {

            if (query.isEmpty()){
                isLoading = false
                return@launch

            }
           try {
               when(val response = repository.getAllBooks(query)){
                   is Resource.Success -> {
                       list = response.data!!
                       if (list.isNotEmpty()) isLoading = false
                   }
                   is Resource.Error -> {
                       isLoading = false
                       Log.d("Error", "searchBooks: Error from repository")
                   }
                   else -> {
                       isLoading = false
                   }
               }

           }catch (exception: Exception){
               isLoading = false
               Log.d("Exception", "searchBooks: ${exception.message}")
           }

        }

    }
}