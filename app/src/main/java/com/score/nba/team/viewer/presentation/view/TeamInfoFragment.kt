package com.score.nba.team.viewer.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.score.nba.team.viewer.R
import com.score.nba.team.viewer.core.domain.model.PlayerInfo
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.presentation.adapter.ShowPlayerInfoAdapter
import com.score.nba.team.viewer.presentation.viewmodel.TeamInfoViewModel
import kotlinx.android.synthetic.main.fragment_team_info.*
import kotlinx.android.synthetic.main.fragment_team_info.team_name
import kotlinx.android.synthetic.main.fragment_team_info.wins
import kotlinx.android.synthetic.main.fragment_team_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TeamInfoFragment: Fragment() {
    lateinit var navController: NavController
    var team: Team? = null
    private val viewModel: TeamInfoViewModel by viewModel()
    lateinit var adapter: ShowPlayerInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        team = arguments?.getParcelable(getString(R.string.team))

        team_name.text = team?.full_name
        wins.text = "Wins: ${team?.wins}"
        loss.text = "Losses: ${team?.losses}"

        team?.id?.let {
            viewModel.fetchPlayerInformation(it).observe(viewLifecycleOwner, Observer {
                when (it) {
                    is Resource.Success -> {
                        hideSpinnerOrErrorMessage()
                        adapter = ShowPlayerInfoAdapter()
                        it.data?.let { it1 -> adapter.loadItems(it1) }
                        team_info.adapter = adapter
                    }
                    is Resource.Error -> {
                        showError(it)
                    }
                    is Resource.Loading -> {
                        showLoadingSpinner()
                    }
                }
            })
        }
    }

    private fun hideSpinnerOrErrorMessage() {
        progress_loader_team_info.visibility = View.INVISIBLE
        error_message_team_info.visibility = View.INVISIBLE
    }

    private fun showLoadingSpinner() {
        progress_loader_team_info.visibility = View.VISIBLE
        error_message_team_info.visibility = View.INVISIBLE
    }

    private fun showError(it: Resource<out List<PlayerInfo>?>) {
        error_message_team_info.visibility = View.VISIBLE
        error_message_team_info.text = it.message
    }
}