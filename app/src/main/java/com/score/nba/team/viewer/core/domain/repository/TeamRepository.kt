package com.score.nba.team.viewer.core.domain.repository

import com.score.nba.team.viewer.core.domain.model.PlayerInfo
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamRepository {
    suspend fun fetchTeamsInformation(): Flow<Resource<out List<Team>?>>
    suspend fun fetchTeamsInformationFromDB(): Flow<Resource<out List<Team>>>
    suspend fun fetchPlayersInformationFromDB(teamId: Int): Flow<Resource<out List<PlayerInfo>>>
}