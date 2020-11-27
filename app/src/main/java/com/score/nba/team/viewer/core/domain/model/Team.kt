package com.score.nba.team.viewer.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val id: Int,
    val full_name: String,
    val losses: Int,
    val players: List<PlayerInfo>,
    val wins: Int
): Parcelable