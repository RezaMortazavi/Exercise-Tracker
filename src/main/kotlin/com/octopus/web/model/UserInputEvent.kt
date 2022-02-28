package com.octopus.web.model

import java.util.*

data class UserEventInput(
    val userId: UUID,
    val exerciseId: Long,
)

data class UpdateUserEventInput(
    val progress: Long
)