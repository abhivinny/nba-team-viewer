package com.score.nba.team.viewer.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.score.nba.team.viewer.R
import com.score.nba.team.viewer.core.usecases.SortBy
import com.score.nba.team.viewer.presentation.viewmodel.TeamsViewModel
import kotlinx.android.synthetic.main.dialog_sort.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SortDialogFragment : DialogFragment() {
    private val viewModel by sharedViewModel<TeamsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_sort, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var sortBy = SortBy.TEAM_NAME_ASCENDING
        save.setOnClickListener {
            when (team_sort_group.checkedRadioButtonId) {
                R.id.nameAsc -> {
                    sortBy = SortBy.TEAM_NAME_ASCENDING
                }
                R.id.nameDsc -> {
                    sortBy = SortBy.TEAM_NAME_DESCENDING
                }
                R.id.winAsc -> {
                    sortBy = SortBy.WIN_ASCENDING
                }
                R.id.winDsc -> {
                    sortBy = SortBy.WIN_DESCENDING
                }
                R.id.lossAsc -> {
                    sortBy = SortBy.LOSS_ASCENDING
                }
                R.id.lossDsc -> {
                    sortBy = SortBy.LOSS_DESCENDING
                }
            }
            viewModel.sortBy(sortBy)
            this.dismiss()
        }
        cancel.setOnClickListener {
            this.dismiss()
        }
    }
}