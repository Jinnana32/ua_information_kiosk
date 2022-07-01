package com.antique.events.data.model

data class GetActivitiesResponse(
    val `data`: List<ActivityData>,
    val success: Boolean
)