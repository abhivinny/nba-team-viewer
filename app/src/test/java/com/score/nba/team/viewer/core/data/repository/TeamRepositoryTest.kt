package com.score.nba.team.viewer.core.data.repository

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.score.nba.team.viewer.core.domain.model.PlayerInfo
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.framework.TeamRemoteService
import com.score.nba.team.viewer.framework.database.dao.PlayerDao
import com.score.nba.team.viewer.framework.database.dao.TeamsDao
import com.score.nba.team.viewer.framework.database.entity.PlayerEntity
import com.score.nba.team.viewer.framework.database.entity.TeamEntity
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import kotlinx.coroutines.flow.collect
import java.io.IOException
import kotlin.test.assertEquals

@RunWith(Enclosed::class)
class TeamRepositoryTest {
    abstract class Base {

        // subject is generally referred to as object under test.
        lateinit var subject: TeamRepositoryImpl

        lateinit var  teamRemoteService: TeamRemoteService
        lateinit var playerDao: PlayerDao
        lateinit var teamDao: TeamsDao
        private val teamList = mutableListOf<Team>()

        private val team1PlayerEntities = mutableListOf<PlayerEntity>()
        private val team2PlayerEntities = mutableListOf<PlayerEntity>()

        val teamEntity = mutableListOf<TeamEntity>()

        private val team1PlayerList = mutableListOf<PlayerInfo>()
        private val team2PlayerList = mutableListOf<PlayerInfo>()
        protected val ERROR_MESSAGE = "Cannot process request"
        protected var teamTeamRemoteError = false
        protected var teamTeamLocalError = false

        @Before
        open fun init() {
            team1PlayerList.add(PlayerInfo(1, "James", "Rogers", 23, "G-F"))
            team1PlayerList.add(PlayerInfo(2, "May", "Fox", 16, "F"))

            team2PlayerList.add(PlayerInfo(1, "Terry", "Miles", 33, "T-G"))
            team2PlayerList.add(PlayerInfo(4, "Mike", "Magic", 11, "M-N"))

            teamList.add(Team(id = 1, full_name = "Bulls", losses = 5, players = team1PlayerList, wins = 10))
            teamList.add(Team(id = 2, full_name = "Cats", losses = 9, players = team2PlayerList, wins = 15))

            team1PlayerEntities.add(PlayerEntity(id = 1, teamId = 1, first_name = "James",
                last_name = "Rogers", number = 23, position = "G-F"
            ))
            team1PlayerEntities.add(PlayerEntity(id = 2, teamId = 1, first_name = "May",
                last_name = "Fox", number = 16, position = "F"
            ))

            team1PlayerEntities.add(PlayerEntity(id = 1, teamId = 2, first_name = "Terry",
                last_name = "Miles", number = 33, position = "T-G"
            ))
            team1PlayerEntities.add(PlayerEntity(id = 2, teamId = 2, first_name = "Mike",
                last_name = "Magic", number = 11, position = "M-N"
            ))

            teamEntity.add(TeamEntity(id = 1, full_name = "Bulls", losses = 5, wins = 10))
            teamEntity.add(TeamEntity(id = 2, full_name = "Cats", losses = 9, wins = 15))

            runBlocking {
                teamRemoteService = mock {
                    on(it.getTeams()) doAnswer {
                        if (teamTeamRemoteError) throw IOException(ERROR_MESSAGE) else teamList
                    }
                }

                playerDao = mock {
                    on(it.getPlayers(1)) doReturn team1PlayerEntities
                    on(it.getPlayers(2)) doReturn team2PlayerEntities
                }

                teamDao = mock {
                    on(it.getTeams()) doAnswer {
                        if (teamTeamLocalError) throw IOException(ERROR_MESSAGE) else teamEntity
                    }
                }
            }

            subject = TeamRepositoryImpl(teamRemoteService, teamDao, playerDao)
        }
    }

    class FetchTeamSuccessTest: Base() {
        @Before
        override fun init() {
            super.init()
        }

        @Test
        fun `should return loading followed by success response`() {
            runBlocking {
                subject.fetchTeamsInformation().collect {
                    when (it) {
                        is Resource.Success -> {
                            assertEquals(it.data?.get(0)?.id, 1)
                        }
                        is Resource.Loading -> {
                            assertEquals(it.data?.get(0)?.id, null)
                        }
                    }
                }
            }
        }
    }

    class FetchTeamErrorTest: Base() {
        @Before
        override fun init() {
            teamTeamLocalError = true
            super.init()
        }

        @Test
        fun `should return loading followed by success response`() {
            runBlocking {
                subject.fetchTeamsInformation().collect {
                    when (it) {
                        is Resource.Success -> {
                            assertEquals(it.data?.get(0)?.id, 1)
                        }
                        is Resource.Loading -> {
                            assertEquals(it.data?.get(0)?.id, null)
                        }
                    }
                }
            }
        }

        @Test
        fun `should return loading followed by error response when exception occurs`() {
            runBlocking {
                teamTeamRemoteError = true
                subject.fetchTeamsInformation().collect {
                    when (it) {
                        is Resource.Success -> {
                            assertEquals(it.data, null)
                        }
                        is Resource.Error -> {
                            assertEquals(it.message, ERROR_MESSAGE)
                        }
                        is Resource.Loading -> {
                            assertEquals(it.data?.get(0)?.id, null)
                        }
                    }
                }
            }
        }
    }

    class LocalDBTest : Base() {
        @Before
        override fun init() {
            super.init()
        }

        @Test
        fun `verify if data is inserted after fetching from remote`() {
            teamTeamLocalError = true
            runBlocking {
                subject.fetchTeamsInformation().collect {  }
                verify(teamDao).insertAll(teamEntity)
            }
        }
    }
}