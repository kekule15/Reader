package com.example.readerapp.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.data.DataOrException
import com.example.readerapp.model.BookModel
import com.example.readerapp.repository.FirebaseBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: FirebaseBookRepository) :
    ViewModel() {
    val data: MutableState<DataOrException<List<BookModel>, Boolean, Exception>> = mutableStateOf(
        DataOrException(
            data = listOf(),
            true,
            Exception("")
        )
    )

    init {
        getAllBooksFromDB()
    }

     fun getAllBooksFromDB() {
       viewModelScope.launch {
           data.value.loading = true
           data.value = repository.getAllBooksFromDB()
           if (!data.value.data.isNullOrEmpty()) data.value.loading = false
       }
    }
    private val uiScope = CoroutineScope(Dispatchers.Main)

    fun launchDataLoad() {
        uiScope.launch {
            getAllBooksFromDB() // happens on the background
            // Modify UI
        }
    }

}