package com.score.nba.team.viewer.presentation.view

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.score.nba.team.viewer.R
import com.score.nba.team.viewer.core.domain.model.Resource
import com.score.nba.team.viewer.core.domain.model.Team
import com.score.nba.team.viewer.presentation.adapter.ShowTeamListAdapter
import com.score.nba.team.viewer.presentation.viewmodel.TeamsViewModel
import kotlinx.android.synthetic.main.fragment_team_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TeamListFragment: Fragment() {

    private val viewModel by sharedViewModel<TeamsViewModel>()
    lateinit var navController: NavController
    lateinit var adapter: ShowTeamListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_team_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        adapter = ShowTeamListAdapter()
        team_list.adapter = adapter
        fetchTeamsInformation()
    }

    private fun fetchTeamsInformation() {
        viewModel.fetchTeamsInformation().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    hideLoadingSpinnerOrErrorMessage()
                    loadItems(it)
                }
                is Resource.Error -> { showErrorMessage(it) }
                is Resource.Loading -> { showLoadingSpinner() }
            }
        })
    }

    private fun loadItems(it: Resource<List<Team>?>) {
        it.data?.let { it1 ->
            adapter.loadItems(it1) {
                val bundle = bundleOf(getString(R.string.team) to it)
                navController.navigate(R.id.action_teamListFragment_to_teamFragment, bundle)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_sort -> { }
        }
        val navigated = NavigationUI.onNavDestinationSelected(item, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    private fun showLoadingSpinner() {
        progress_loader.visibility = View.VISIBLE
        error_message_team_list.visibility = View.INVISIBLE
    }

    private fun hideLoadingSpinnerOrErrorMessage() {
        progress_loader.visibility = View.INVISIBLE
        error_message_team_list.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(it: Resource<List<Team>?>) {
        error_message_team_list.visibility = View.VISIBLE
        error_message_team_list.text = it.message
    }
}