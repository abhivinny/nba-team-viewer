package com.score.nba.team.viewer.framework

import com.score.nba.team.viewer.core.domain.model.Team

import retrofit2.http.GET

interface TeamRemoteService {
    @GET("scoremedia/nba-team-viewer/master/input.json")
    suspend fun getTeams(): List<Team>
}