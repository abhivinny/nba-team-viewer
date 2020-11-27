package com.score.nba.team.viewer.presentation.viewmodel

import androidx.lifecycle.*
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.core.usecases.SortBy
import com.score.nba.team.viewer.core.usecases.TeamsUseCases
import kotlinx.coroutines.launch

class TeamsViewModel(private val teamsUseCases: TeamsUseCases) : ViewModel() {

    val teamListObservable = MutableLiveData<Resource<List<Team>?>>()

    fun fetchTeamsInformation() : LiveData<Resource<List<Team>?>> {
        viewModelScope.launch {
            teamListObservable.value = teamsUseCases.fetchTeamsInformation().value
        }
        return teamListObservable
    }

    fun sortBy(sortBy: SortBy) {
        viewModelScope.launch {
            teamListObservable.value = teamsUseCases.getTeamsInformationLocally(sortBy).value
        }
    }
}

