package com.score.nba.team.viewer.framework.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "team")
data class TeamEntity(
    @PrimaryKey val id: Int,
    val full_name: String,
    val losses: Int,
    val wins: Int
)

@Entity(tableName = "players")
data class PlayerEntity(
    @PrimaryKey val id: Int,
    val teamId: Int,
    val first_name: String,
    val last_name: String,
    val number: Int,
    val position: String
)