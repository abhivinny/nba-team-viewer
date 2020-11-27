package com.score.nba.team.viewer.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.score.nba.team.viewer.framework.database.dao.PlayerDao
import com.score.nba.team.viewer.framework.database.dao.TeamsDao
import com.score.nba.team.viewer.framework.database.entity.PlayerEntity
import com.score.nba.team.viewer.framework.database.entity.TeamEntity

@Database(entities = [TeamEntity::class, PlayerEntity::class], version = 1)
abstract class TeamsDatabase : RoomDatabase() {
    abstract fun teamDao(): TeamsDao
    abstract fun playerDao(): PlayerDao
}