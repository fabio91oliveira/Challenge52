package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks

interface GoalMapper {
    operator fun invoke(goalWithWeeksList: GoalWithWeeks): Goal
}