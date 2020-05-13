package oliveira.fabio.challenge52.challenge.challengeoverview.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.newgoal.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.CreateScreensUseCase
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.action.ChallengeOverviewActions
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.viewstate.ChallengeOverviewViewState
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo.OverviewDetails
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge
import timber.log.Timber

internal class ChallengeOverviewViewModel(
    challenge: Challenge,
    private val createScreensUseCase: CreateScreensUseCase
) : ViewModel() {

    private val _challengeOverviewActions by lazy { MutableLiveData<ChallengeOverviewActions>() }
    val challengeOverviewActions by lazy { _challengeOverviewActions }

    private val _challengeOverviewViewState by lazy { MutableLiveData<ChallengeOverviewViewState>() }
    val challengeOverviewViewState by lazy { _challengeOverviewViewState }

    init {
        showScreens(challenge)
    }

    private fun showScreens(challenge: Challenge) {
        viewModelScope.launch {
            ChallengeOverviewViewState(
                isLoading = true
            ).setState()
            SuspendableResult.of<List<OverviewDetails>, Exception> {
                createScreensUseCase(challenge)
            }.fold(success = {
                ChallengeOverviewActions.ShowScreens(
                    it
                ).sendAction()
                ChallengeOverviewViewState(
                    isLoading = false
                ).setState()
            }, failure = {
                ChallengeOverviewActions.Error(
                    R.string.new_goal_challenge_overview_error_title,
                    R.string.new_goal_challenge_overview_error_description
                ).sendAction()
                ChallengeOverviewViewState(
                    isLoading = false
                ).setState()
                Timber.e(it)
            })
        }
    }

    private fun ChallengeOverviewActions.sendAction() {
        _challengeOverviewActions.value = this
    }

    private fun ChallengeOverviewViewState.setState() {
        _challengeOverviewViewState.value = this
    }
}