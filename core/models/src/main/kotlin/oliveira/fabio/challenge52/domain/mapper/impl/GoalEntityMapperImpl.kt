package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.GoalEntityMapper
import oliveira.fabio.challenge52.persistence.model.entity.GoalEntity
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum
import oliveira.fabio.challenge52.persistence.model.enums.PeriodTypeEnum
import oliveira.fabio.challenge52.presentation.vo.GoalToSave
import oliveira.fabio.challenge52.presentation.vo.PeriodEnum

internal class GoalEntityMapperImpl :
    GoalEntityMapper {
    override fun invoke(goalToSave: GoalToSave) = createGoalEntity(goalToSave)

    private fun createGoalEntity(goalToSave: GoalToSave) = GoalEntity(
        idChallenge = goalToSave.idChallenge,
        goalStatus = GoalStatusEnum.NEW,
        name = goalToSave.name,
        currentLocale = goalToSave.currentLocale,
        periodType = getPeriodTime(goalToSave.period),
        totalPeriod = goalToSave.totalPeriod,
        totalMoney = goalToSave.totalMoney
    )

    private fun getPeriodTime(periodEnum: PeriodEnum?): PeriodTypeEnum {
        return when (periodEnum) {
            PeriodEnum.DAILY -> PeriodTypeEnum.DAILY
            PeriodEnum.WEEKLY -> PeriodTypeEnum.WEEKLY
            PeriodEnum.MONTHLY -> PeriodTypeEnum.MONTHLY
            else -> throw IllegalArgumentException()
        }
    }
}