package com.rwitczyk.dto

data class UserDTO(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val age: Int
)
