package com.score.nba.team.viewer.framework.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.score.nba.team.viewer.framework.database.entity.PlayerEntity

@Dao
interface PlayerDao {
    @Query(" SELECT * FROM players where teamId = :teamId")
    suspend fun getPlayers(teamId: Int) : List<PlayerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(players: List<PlayerEntity>)
}