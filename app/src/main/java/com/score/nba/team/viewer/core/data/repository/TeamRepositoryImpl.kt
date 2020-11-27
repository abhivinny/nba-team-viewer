package com.score.nba.team.viewer.core.data.repository

import com.score.nba.team.viewer.core.domain.model.PlayerInfo
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.core.domain.repository.TeamRepository
import com.score.nba.team.viewer.framework.TeamRemoteService
import com.score.nba.team.viewer.framework.database.dao.PlayerDao
import com.score.nba.team.viewer.framework.database.dao.TeamsDao
import com.score.nba.team.viewer.framework.database.entity.PlayerEntity
import com.score.nba.team.viewer.framework.database.entity.TeamEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class TeamRepositoryImpl(
    private val teamRemoteService: TeamRemoteService,
    private val teamsDao: TeamsDao,
    private val playerDao: PlayerDao
) : TeamRepository {

    override suspend fun fetchTeamsInformation(): Flow<Resource<out List<Team>?>> = flow {
        emit(Resource.Loading())
        try {
            fetchTeamsInformationFromDB().collect {
                when(it) {
                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty()) {
                            val data = teamRemoteService.getTeams()
                            insertDataLocally(data)
                            emit(Resource.Success(data))
                        } else emit(Resource.Success(it.data))
                    }
                    is Resource.Error -> {
                        val data = teamRemoteService.getTeams()
                        insertDataLocally(data)
                        emit(Resource.Success(data))
                    }
                }
            }

        } catch (exception: IOException) {
            emit(Resource.Error(exception.message.toString(), null))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun insertDataLocally(data: List<Team>) {
        val teamEntities = mutableListOf<TeamEntity>()
        val playerEntities = mutableListOf<PlayerEntity>()
        data.forEach {
            teamEntities.add(TeamEntity(it.id, it.full_name, it.losses, it.wins))
            it.players.forEach { pl ->
                playerEntities.add(
                    PlayerEntity(
                        pl.id,
                        it.id,
                        pl.first_name,
                        pl.last_name,
                        pl.number,
                        pl.position
                    )
                )
            }
        }
        teamsDao.insertAll(teamEntities)
        playerDao.insertAll(playerEntities)
    }

    override suspend fun fetchTeamsInformationFromDB(): Flow<Resource<out List<Team>>> = flow<Resource<out List<Team>>> {
        emit(Resource.Loading())
        try {
            val data = teamsDao.getTeams()
            val teams = mutableListOf<Team>()
            data.forEach {
                teams.add(
                    Team(
                        it.id,
                        it.full_name,
                        it.wins,
                        emptyList<PlayerInfo>(),
                        it.losses
                    )
                )
            }
            emit(Resource.Success(teams))
        } catch (exception: Exception) {
                emit(Resource.Error(exception.message.toString(), null))
            }
    }.flowOn(Dispatchers.IO)


    override suspend fun fetchPlayersInformationFromDB(teamId: Int): Flow<Resource<out List<PlayerInfo>>> = flow<Resource<out List<PlayerInfo>>> {
        emit(Resource.Loading())
        try {
            val playersEntity = playerDao.getPlayers(teamId)
            val teams = mutableListOf<PlayerInfo>()
            playersEntity.forEach {
                teams.add(
                    PlayerInfo(
                        it.id,
                        it.first_name,
                        it.last_name,
                        it.number,
                        it.position
                    )
                )
            }
            emit(Resource.Success(teams))
        } catch (exception: Exception) {
            emit(Resource.Error(exception.message.toString(), null))
        }
    }.flowOn(Dispatchers.IO)
}