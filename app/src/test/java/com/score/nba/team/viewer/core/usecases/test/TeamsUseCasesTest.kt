package com.score.nba.team.viewer.core.usecases.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.core.domain.repository.TeamRepository
import com.score.nba.team.viewer.core.usecases.SortBy
import com.score.nba.team.viewer.core.usecases.TeamsUseCases
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
@RunWith(Enclosed::class)
class TeamsUseCasesTest {

    abstract class Base {
        @get:Rule
        val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

        // subject is generally referred to as object under test.
        lateinit var subject: TeamsUseCases
        lateinit var teamRepository: TeamRepository

        val initialTeamList = mutableListOf<Team>()
        val teamListAscendingByName = mutableListOf<Team>()
        val teamListDescendingByName = mutableListOf<Team>()
        val teamListAscendingByWin = mutableListOf<Team>()

        @Before
        open fun init() {
            initialTeamList.add(Team(id = 2, full_name = "Bulls", losses = 7, players = emptyList(), wins = 15))
            initialTeamList.add(Team(id = 4, full_name = "Cats", losses = 9, players = emptyList(), wins = 10))
            initialTeamList.add(Team(id = 1, full_name = "Arizona", losses = 5, players = emptyList(), wins = 5))
            initialTeamList.add(Team(id = 6, full_name = "Dodgers", losses = 12, players = emptyList(), wins = 22))
            initialTeamList.add(Team(id = 7, full_name = "Phoenix", losses = 11, players = emptyList(), wins = 17))

            teamListAscendingByName.add(Team(id = 1, full_name = "Arizona", losses = 5, players = emptyList(), wins = 5))
            teamListAscendingByName.add(Team(id = 2, full_name = "Bulls", losses = 7, players = emptyList(), wins = 15))
            teamListAscendingByName.add(Team(id = 4, full_name = "Cats", losses = 9, players = emptyList(), wins = 10))
            teamListAscendingByName.add(Team(id = 6, full_name = "Dodgers", losses = 12, players = emptyList(), wins = 22))
            teamListAscendingByName.add(Team(id = 7, full_name = "Phoenix", losses = 11, players = emptyList(), wins = 17))

            teamListDescendingByName.add(Team(id = 7, full_name = "Phoenix", losses = 11, players = emptyList(), wins = 17))
            teamListDescendingByName.add(Team(id = 6, full_name = "Dodgers", losses = 12, players = emptyList(), wins = 22))
            teamListDescendingByName.add(Team(id = 4, full_name = "Cats", losses = 9, players = emptyList(), wins = 10))
            teamListDescendingByName.add(Team(id = 2, full_name = "Bulls", losses = 7, players = emptyList(), wins = 15))
            teamListDescendingByName.add(Team(id = 1, full_name = "Arizona", losses = 5, players = emptyList(), wins = 5))

            teamListAscendingByWin.add(Team(id = 6, full_name = "Dodgers", losses = 12, players = emptyList(), wins = 22))
            teamListAscendingByWin.add(Team(id = 7, full_name = "Phoenix", losses = 11, players = emptyList(), wins = 17))
            teamListAscendingByWin.add(Team(id = 2, full_name = "Bulls", losses = 7, players = emptyList(), wins = 15))
            teamListAscendingByWin.add(Team(id = 4, full_name = "Cats", losses = 9, players = emptyList(), wins = 10))
            teamListAscendingByWin.add(Team(id = 1, full_name = "Arizona", losses = 5, players = emptyList(), wins = 5))

            val teamsOutput = flow<Resource<out List<Team>>> {
                emit(Resource.Success(initialTeamList))
            }

            runBlocking {
                teamRepository = mock {
                    on(it.fetchTeamsInformationFromDB()) doReturn teamsOutput
                }
            }
            subject = TeamsUseCases(teamRepository)
        }
    }

    class TeamSortingTest : Base() {
        @Test
        fun `should return list sorted by name in ascending order`() {
            runBlocking {
                val sortedList = subject.getTeamsInformationLocally(SortBy.TEAM_NAME_ASCENDING).value
                assertEquals(teamListAscendingByName, sortedList?.data)
            }
        }

        @Test
        fun `should return list sorted by name in descending order`() {
            runBlocking {
                val sortedList = subject.getTeamsInformationLocally(SortBy.TEAM_NAME_DESCENDING).value
                assertEquals(teamListDescendingByName, sortedList?.data)
            }
        }

        @Test
        fun `should return list sorted by win in descending order`() {
            runBlocking {
                val sortedList = subject.getTeamsInformationLocally(SortBy.WIN_DESCENDING).value
                assertEquals(teamListAscendingByWin, sortedList?.data)
            }
        }
    }
}