package oliveira.fabio.challenge52.goalcreate.domain.usecase.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import oliveira.fabio.challenge52.goalcreate.domain.usecase.GetGoalSuggestionsUseCase
import oliveira.fabio.challenge52.goalcreate.domain.vo.GoalSuggestion

internal class GetGoalSuggestionsUseCaseImpl : GetGoalSuggestionsUseCase {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
        val z = GoalSuggestion(
            id = null,
            icon = null,
            name = null,
            type = GoalSuggestion.Type.NOT_A_SUGGESTION
        )
        val a = GoalSuggestion(
            id = 1,
            icon = "",
            name = "Buy a car",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val b = GoalSuggestion(
            id = 2,
            icon = "",
            name = "Buy a computer",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val c = GoalSuggestion(
            id = 3,
            icon = "",
            name = "Make a trip",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val d = GoalSuggestion(
            id = 4,
            icon = "",
            name = "Buy a shoes",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val e = GoalSuggestion(
            id = 5,
            icon = "",
            name = "Do some investment",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val f = GoalSuggestion(
            id = 6,
            icon = "",
            name = "Buy an apartment or a house",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val g = GoalSuggestion(
            id = 7,
            icon = "",
            name = "By a smartphone",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val h = GoalSuggestion(
            id = 8,
            icon = "",
            name = "Buy a musical instrument",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val i = GoalSuggestion(
            id = 9,
            icon = "",
            name = "Pay a bill",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val j = GoalSuggestion(
            id = 10,
            icon = "",
            name = "Buy a TV",
            type = GoalSuggestion.Type.SUGGESTION
        )

        val k = GoalSuggestion(
            id = 11,
            icon = "",
            name = "Just save money",
            type = GoalSuggestion.Type.SUGGESTION
        )

        return@withContext mutableListOf<GoalSuggestion>().apply {
            add(z)
            add(a)
            add(b)
            add(c)
            add(d)
            add(e)
            add(f)
            add(g)
            add(h)
            add(i)
            add(j)
            add(k)
        }.toList()
    }

}