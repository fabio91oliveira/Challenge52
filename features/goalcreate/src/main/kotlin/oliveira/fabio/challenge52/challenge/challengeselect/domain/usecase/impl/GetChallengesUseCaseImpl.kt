package oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.challenge.challengeselect.domain.usecase.GetChallengesUseCase
import oliveira.fabio.challenge52.domain.vo.Challenge

internal class GetChallengesUseCaseImpl :
    GetChallengesUseCase {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
//        val e = Challenge(
//            id = 0,
//            name = "Custom Challenge",
//            description = "You need to accumulate some money per week, this is value that you are saving is accumulative. The most important reason of this challenge is the knowledge of saving money."
//        )

        val a = Challenge(
            id = 1,
            name = "Challenge 52 Weeks",
            description = "You need to accumulate some money per week, this is value that you are saving is accumulative. The most important reason of this challenge is the knowledge of saving money.",
            isAccumulative = true,
            quantity = 52,
            type = Challenge.Type.WEEKLY
        )

        val b = Challenge(
            id = 2,
            name = "Challenge 31 Days",
            description = "You need to accumulate some money per week, this is value that you are saving is accumulative. The most important reason of this challenge is the knowledge of saving money.",
            isAccumulative = true,
            quantity = 31,
            type = Challenge.Type.DAILY
        )

        val c = Challenge(
            id = 3,
            name = "Challenge 7 Days",
            description = "You need to accumulate some money per week, this is value that you are saving is accumulative. The most important reason of this challenge is the knowledge of saving money.",
            isAccumulative = false,
            quantity = 7,
            type = Challenge.Type.DAILY
        )

        val d = Challenge(
            id = 4,
            name = "Challenge 10 Months",
            description = "You need to accumulate some money per week, this is value that you are saving is accumulative. The most important reason of this challenge is the knowledge of saving money.",
            isAccumulative = false,
            quantity = 10,
            type = Challenge.Type.MONTHLY
        )

        return@withContext mutableListOf<Challenge>().apply {
//            add(e)
            add(a)
            add(b)
            add(c)
            add(d)
        }.toList()
    }
}