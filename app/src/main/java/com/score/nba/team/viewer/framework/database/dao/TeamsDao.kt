package com.score.nba.team.viewer.framework.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.score.nba.team.viewer.framework.database.entity.TeamEntity

@Dao
interface TeamsDao {

    @Query(" SELECT * FROM team")
    suspend fun getTeams() : List<TeamEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<TeamEntity>)
}