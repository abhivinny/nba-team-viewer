package com.score.nba.team.viewer.core.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlayerInfo(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val number: Int,
    val position: String
): Parcelable