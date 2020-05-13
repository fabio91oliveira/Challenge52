package oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.impl

import features.newgoal.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.CreateScreensUseCase
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo.OverviewDetails
import oliveira.fabio.challenge52.challenge.selectchallenge.presentation.vo.Challenge

internal class CreateScreensUseCaseImpl :
    CreateScreensUseCase {
    override suspend fun invoke(challenge: Challenge) = withContext(Dispatchers.Default) {
        val firstScreen = createFirstScreen(challenge)
        val secondScreen = createSecondScreen()
        val thirdScreen = createThirdScreen()

        return@withContext mutableListOf<OverviewDetails>().apply {
            add(firstScreen)
            add(secondScreen)
            add(thirdScreen)
        }
    }

    private fun createFirstScreen(challenge: Challenge) =
        challenge.type?.let {
            challenge.isAccumulative.let {
                OverviewDetails(
                    resImage = if (it) R.drawable.ic_accumulative_challenge else R.drawable.ic_fixed_challenge,
                    resTitle = if (it) R.string.new_goal_challenge_overview_first_page_accumulative_title else R.string.new_goal_challenge_overview_first_page_title,
                    resDescription = if (it) R.string.new_goal_challenge_overview_first_page_accumulative_description else R.string.new_goal_challenge_overview_first_page_description
                )
            }
        } ?: throw IllegalArgumentException()

    private fun createSecondScreen() =
        OverviewDetails(
            resImage = R.drawable.ic_done_goal_item,
            resTitle = R.string.new_goal_challenge_overview_second_page_title,
            resDescription = R.string.new_goal_challenge_overview_second_page_description
        )

    private fun createThirdScreen() =
        OverviewDetails(
            resImage = R.drawable.ic_business_man,
            resTitle = R.string.new_goal_challenge_overview_third_page_title,
            resDescription = R.string.new_goal_challenge_overview_third_page_description
        )
}