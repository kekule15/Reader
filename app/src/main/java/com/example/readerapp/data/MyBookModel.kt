package com.example.readerapp.data

data class MyBookModel(
    val items: List<BookItem>,
    val kind: String,
    val totalItems: Int
)