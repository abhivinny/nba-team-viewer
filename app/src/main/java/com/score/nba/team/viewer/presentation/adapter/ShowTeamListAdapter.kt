package com.score.nba.team.viewer.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.score.nba.team.viewer.R
import com.score.nba.team.viewer.core.domain.model.Team
import kotlinx.android.synthetic.main.item_team_list.view.*

class ShowTeamListAdapter: RecyclerView.Adapter<ShowTeamViewHolder>() {

    private lateinit var onItemSelected: (Team) -> Unit
    private var teams: List<Team> = listOf()

    fun loadItems(newItems: List<Team>, onClick: (Team) -> Unit) {
        teams = newItems
        onItemSelected = onClick
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowTeamViewHolder {
        return ShowTeamViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_team_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShowTeamViewHolder, position: Int) {
        val team = teams[position]
        holder.bindView(team, onItemSelected)
    }

    override fun getItemCount(): Int {
        return  teams.size
    }
}

class ShowTeamViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    fun bindView(team: Team, onItemSelected: (Team) -> Unit) {
        itemView.team_name.text = team.full_name
        itemView.wins.text = team.wins.toString()
        itemView.losses.text = team.losses.toString()
        itemView.setOnClickListener { onItemSelected(team) }
    }
}