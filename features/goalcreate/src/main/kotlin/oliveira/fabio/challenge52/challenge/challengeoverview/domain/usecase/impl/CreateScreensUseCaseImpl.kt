package oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.impl

import features.goalcreate.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.challenge.challengeoverview.domain.usecase.CreateScreensUseCase
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo.OverviewDetails
import oliveira.fabio.challenge52.domain.vo.Challenge

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
        OverviewDetails(
            resImage = if (challenge.isAccumulative) R.drawable.ic_business_man else R.drawable.ic_add_circle,
            resTitle = if (challenge.isAccumulative) R.string.goal_create_challenge_overview_first_page_accumulative_title else R.string.goal_create_challenge_overview_first_page_title,
            resDescription = if (challenge.isAccumulative) R.string.goal_create_challenge_overview_first_page_accumulative_description else R.string.goal_create_challenge_overview_first_page_description
        )

    private fun createSecondScreen() =
        OverviewDetails(
            resImage = R.drawable.ic_business_man,
            resTitle = R.string.goal_create_challenge_overview_second_page_title,
            resDescription = R.string.goal_create_challenge_overview_second_page_description
        )

    private fun createThirdScreen() =
        OverviewDetails(
            resImage = R.drawable.ic_business_man,
            resTitle = R.string.goal_create_challenge_overview_third_page_title,
            resDescription = R.string.goal_create_challenge_overview_third_page_description
        )
}