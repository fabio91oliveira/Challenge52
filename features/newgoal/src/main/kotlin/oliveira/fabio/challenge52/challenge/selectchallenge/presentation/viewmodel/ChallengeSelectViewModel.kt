package oliveira.fabio.challenge52.challenge.selectchallenge.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import features.newgoal.R
import kotlinx.coroutines.launch
import oliveira.fabio.challenge52.challenge.selectchallenge.domain.usecase.GetChallengesUseCase
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.action.ChallengeSelectActions
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.viewstate.ChallengeSelectViewState
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge
import timber.log.Timber

internal class ChallengeSelectViewModel(
    private val getChallengesUseCase: GetChallengesUseCase
) : ViewModel() {

    private val _challengeSelectActions by lazy { MutableLiveData<ChallengeSelectActions>() }
    val challengeSelectActions by lazy { _challengeSelectActions }

    private val _challengeSelectViewState by lazy { MutableLiveData<ChallengeSelectViewState>() }
    val challengeSelectViewState by lazy { _challengeSelectViewState }

    init {
        getChallengesList()
    }

    private fun getChallengesList() {
        viewModelScope.launch {
            ChallengeSelectViewState(
                isLoading = true
            ).setState()
            SuspendableResult.of<List<Challenge>, Exception> {
                getChallengesUseCase()
            }.fold(
                success = {
                    ChallengeSelectActions.Challenges(it).sendAction()
                    ChallengeSelectViewState(
                        isLoading = false,
                        isChallengesVisible = true
                    ).setState()
                },
                failure = {
                    ChallengeSelectActions.Error(
                        R.string.new_goal_select_challenge_error_title,
                        R.string.new_goal_select_challenge_error_description
                    ).sendAction()
                    Timber.e(it)
                }
            )
        }
    }

    private fun ChallengeSelectActions.sendAction() {
        _challengeSelectActions.value = this
    }

    private fun ChallengeSelectViewState.setState() {
        _challengeSelectViewState.value = this
    }
}