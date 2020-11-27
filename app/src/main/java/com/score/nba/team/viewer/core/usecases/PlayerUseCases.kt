package com.score.nba.team.viewer.core.usecases

import com.score.nba.team.viewer.core.domain.repository.TeamRepository

class PlayerUseCases(private val teamRepository: TeamRepository) {
    suspend fun fetchPlayerInformation(teamId: Int) = teamRepository.fetchPlayersInformationFromDB(teamId)
}