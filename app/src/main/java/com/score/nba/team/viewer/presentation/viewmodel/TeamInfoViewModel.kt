package com.score.nba.team.viewer.presentation.viewmodel

import androidx.lifecycle.*
import com.score.nba.team.viewer.core.domain.model.PlayerInfo
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.core.usecases.PlayerUseCases
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TeamInfoViewModel(private val playerUseCases: PlayerUseCases) : ViewModel() {

    fun fetchPlayerInformation(teamId: Int) : LiveData<Resource<out List<PlayerInfo>?>> {
        val teamInfoObservable = MutableLiveData<Resource<out List<PlayerInfo>?>>()

        viewModelScope.launch {
            playerUseCases.fetchPlayerInformation(teamId).collect {
                teamInfoObservable.value = it
            }
        }
        return teamInfoObservable
    }
}