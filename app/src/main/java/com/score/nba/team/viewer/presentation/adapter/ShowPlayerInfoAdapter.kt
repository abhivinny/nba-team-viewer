package com.score.nba.team.viewer.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.score.nba.team.viewer.R
import com.score.nba.team.viewer.core.domain.model.PlayerInfo
import kotlinx.android.synthetic.main.item_player_list.view.*

class ShowPlayerInfoAdapter(): RecyclerView.Adapter<ShowPlayerInfoAdapterViewHolder>() {

    private var player: List<PlayerInfo> = listOf()
    fun loadItems(newItems: List<PlayerInfo>) {
        player = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowPlayerInfoAdapterViewHolder {
        return ShowPlayerInfoAdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_player_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShowPlayerInfoAdapterViewHolder, position: Int) {
        val team = player[position]
        holder.bindView(team)
    }

    override fun getItemCount(): Int {
        return  player.size
    }
}

class ShowPlayerInfoAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindView(playerInfo: PlayerInfo) {
        itemView.player_number.text = playerInfo.number.toString()
        itemView.player_name.text = "${playerInfo.first_name} ${playerInfo.last_name}"
        itemView.position.text = playerInfo.position
    }
}