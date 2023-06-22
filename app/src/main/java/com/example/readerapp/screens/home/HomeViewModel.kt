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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        getUser()
       // getAllBooksFromDB()
    }

    private fun getAllBooksFromDB() {
        viewModelScope.launch {
            data.value.loading = true
            data.value = repository.getAllBooksFromDB()
            if (!data.value.data.isNullOrEmpty()) data.value.loading = false
        }
    }

    private val moreData = MutableStateFlow<DataOrException<List<BookModel>, Boolean, Exception>>(
        DataOrException(
            data = listOf(),
            true,
            Exception("")
        )
    )
    val user: StateFlow<DataOrException<List<BookModel>, Boolean, Exception>> get() = moreData


    private fun getUser() {
        viewModelScope.launch {

            repository.getUser().collect { user ->
                moreData.value = user
                if (!moreData.value.data.isNullOrEmpty()) moreData.value.loading = false
            }
        }
    }

}