package com.octopus.web.model

data class UserInput(
    val email: String,
    val firstName: String,
    val lastName: String,
    val notifiers: NotifierInput? = null
)
