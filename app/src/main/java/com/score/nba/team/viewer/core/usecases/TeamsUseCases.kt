package com.score.nba.team.viewer.core.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.core.domain.repository.TeamRepository
import kotlinx.coroutines.flow.collect

class TeamsUseCases(private val teamRepository: TeamRepository) {

    suspend fun fetchTeamsInformation(): LiveData<Resource<List<Team>?>>  {
        val teamListObservable = MutableLiveData<Resource<List<Team>?>>()
        teamRepository.fetchTeamsInformation().collect {
            when (it) {
                is Resource.Success -> {
                    val sortedList = it.data?.sortedBy { it.full_name }
                    teamListObservable.value = Resource.Success(sortedList)
                }
                is Resource.Error -> {
                    teamListObservable.value = Resource.Error("Something went wrong, Sorry can't fetch the team list")
                }
                is Resource.Loading -> {
                    teamListObservable.value = Resource.Loading()
                }
            }
        }
        return teamListObservable
    }

    suspend fun getTeamsInformationLocally(sortBy: SortBy): MutableLiveData<Resource<List<Team>?>> {
        val sortListObservable = MutableLiveData<Resource<List<Team>?>>()
        teamRepository.fetchTeamsInformationFromDB().collect {
            when (it) {
                is Resource.Success -> {
                    when (sortBy) {
                        SortBy.TEAM_NAME_ASCENDING -> {
                            sortListObservable.value = Resource.Success(it.data?.sortedBy { it.full_name })
                        }
                        SortBy.TEAM_NAME_DESCENDING -> {
                            sortListObservable.value = Resource.Success(it.data?.sortedByDescending { it.full_name })
                        }
                        SortBy.WIN_ASCENDING -> {
                            sortListObservable.value = Resource.Success(it.data?.sortedBy { it.wins })
                        }
                        SortBy.WIN_DESCENDING -> {
                            sortListObservable.value = Resource.Success(it.data?.sortedByDescending { it.wins })
                        }
                        SortBy.LOSS_ASCENDING -> {
                            sortListObservable.value = Resource.Success(it.data?.sortedBy { it.losses })
                        }
                        SortBy.LOSS_DESCENDING -> {
                            sortListObservable.value = Resource.Success(it.data?.sortedByDescending { it.losses })
                        }
                    }
                }
                is Resource.Error -> {
                    sortListObservable.value = Resource.Error("Something went wrong, Sorry can't sort the team list")
                }
                is Resource.Loading -> {
                    sortListObservable.value = Resource.Loading()
                }
            }
        }
        return sortListObservable
    }
}

enum class SortBy {
    TEAM_NAME_ASCENDING,
    TEAM_NAME_DESCENDING,
    WIN_ASCENDING,
    WIN_DESCENDING,
    LOSS_ASCENDING,
    LOSS_DESCENDING,
}