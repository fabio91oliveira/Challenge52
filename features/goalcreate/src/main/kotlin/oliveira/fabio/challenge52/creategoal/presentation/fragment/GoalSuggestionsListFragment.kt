package oliveira.fabio.challenge52.creategoal.presentation.fragment

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import features.goalcreate.R
import kotlinx.android.synthetic.main.fragment_goal_suggestions_list.*
import oliveira.fabio.challenge52.creategoal.presentation.vo.GoalSuggestion
import oliveira.fabio.challenge52.creategoal.presentation.action.GoalSuggestionsListActions
import oliveira.fabio.challenge52.creategoal.presentation.adapter.GoalSuggestionAdapter
import oliveira.fabio.challenge52.creategoal.presentation.viewmodel.GoalSuggestionsListViewModel
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.extensions.isVisible
import oliveira.fabio.challenge52.presentation.dialogfragment.FullScreenDialog
import org.koin.androidx.viewmodel.ext.android.viewModel


internal class GoalSuggestionsListFragment :
    Fragment(R.layout.fragment_goal_suggestions_list),
    GoalSuggestionAdapter.GoalSuggestionClickListener {

    private val challenge by lazy {
        arguments?.getParcelable<Challenge>(CHALLENGE)
    }

    private val goalSuggestionAdapter by lazy { GoalSuggestionAdapter(this) }
    private val goalSuggestionsListViewModel: GoalSuggestionsListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onSuggestionsClick(goalSuggestion: GoalSuggestion) {
        goalSuggestionsListViewModel.goToNameSetupScreen(goalSuggestion, challenge)
    }

    private fun init() {
        setupRecyclerView()
        setupObservables()
        initAnimations()
    }

    private fun setupRecyclerView() {
        with(rvSuggestions) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.RecyclerView.VERTICAL,
                false
            )
            adapter = goalSuggestionAdapter
        }
    }

    private fun setupObservables() {
        with(goalSuggestionsListViewModel) {
            goalSuggestionsListViewState.observe(viewLifecycleOwner, Observer {
                showSuggestions(it.isSuggestionsVisible)
                showLoading(it.isLoading)
            })
            goalSuggestionsListActions.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is GoalSuggestionsListActions.SuggestionsList -> {
                        goalSuggestionAdapter.addSuggestions(it.suggestions)
                    }
                    is GoalSuggestionsListActions.GoToGoalChooseNameScreen -> {
                        findNavController().navigate(
                            R.id.action_go_to_goal_create_choose_name,
                            bundleOf(
                                GOAL to it.goalToSave
                            )
                        )
                    }
                    is GoalSuggestionsListActions.Error -> {
                        showFullScreenDialog(it.titleMessageRes, it.errorMessageRes)
                    }
                }
            })
        }
    }

    private fun initAnimations() {
        val valueAnimator = ValueAnimator.ofFloat(-700f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 800
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            viewTop.translationY = progress
        }
        valueAnimator.start()

        val valueAnimator2 = ValueAnimator.ofFloat(-700f, 0f)
        valueAnimator2.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator2.duration = 800
        valueAnimator2.addUpdateListener {
            val progress = it.animatedValue as Float
            txtTitle?.translationY = progress
        }
        valueAnimator2.start()
    }

    private fun showLoading(hasToShow: Boolean) {
        loading.isVisible = hasToShow
    }

    private fun showSuggestions(hasToShow: Boolean) {
        rvSuggestions.isVisible = hasToShow
    }

    private fun showFullScreenDialog(@StringRes titleRes: Int, @StringRes descriptionRes: Int) {
        FullScreenDialog.Builder()
            .setTitle(titleRes)
            .setSubtitle(descriptionRes)
            .setCloseAction(object : FullScreenDialog.FullScreenDialogCloseListener {
                override fun onClickCloseButton() {
                    activity?.finish()
                }
            })
            .setupConfirmButton(
                R.string.all_button_ok,
                object : FullScreenDialog.FullScreenDialogConfirmListener {
                    override fun onClickConfirmButton() {
                        activity?.finish()
                    }
                })
            .setupCancelButton(
                R.string.all_button_go_back,
                object : FullScreenDialog.FullScreenDialogCancelListener {
                    override fun onClickCancelButton() {
                        activity?.finish()
                    }
                })
            .build()
            .show(childFragmentManager, FullScreenDialog.TAG)
    }

    companion object {
        private const val GOAL = "goal"
        private const val CHALLENGE = "challenge"
    }
}