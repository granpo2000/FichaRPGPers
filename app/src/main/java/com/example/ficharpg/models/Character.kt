package com.example.ficharpg.models

data class Character(
    val id: Long,
    val name: String,
    val raceId: Int,
    val subRaceId: Int?,
    val classId: Int,
    val level: Int
)
