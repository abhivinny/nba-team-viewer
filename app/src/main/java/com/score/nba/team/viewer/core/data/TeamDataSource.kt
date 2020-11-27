package com.score.nba.team.viewer.core.data

import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import kotlinx.coroutines.flow.Flow

interface TeamDataSource {
    suspend fun fetchTeamsInformation() : Flow<Resource<out List<Team>>>
}