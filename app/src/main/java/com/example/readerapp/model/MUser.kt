package com.example.readerapp.model

data class MUser(
    val fullName: String,
    val userId: String,
    val email: String,
    val quote: String,
    val avatar: String,
    val profession: String,
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "id" to userId,
            "fullName" to fullName,
            "email" to email,
            "quote" to quote,
            "avatar" to avatar,
            "profession" to profession

        )
    }
}
