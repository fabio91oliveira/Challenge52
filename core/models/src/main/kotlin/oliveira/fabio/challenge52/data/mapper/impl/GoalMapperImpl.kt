package oliveira.fabio.challenge52.data.mapper.impl

import oliveira.fabio.challenge52.data.mapper.GoalMapper
import oliveira.fabio.challenge52.persistence.model.entity.GoalWithItemsEntity
import oliveira.fabio.challenge52.persistence.model.enums.GoalStatusEnum
import oliveira.fabio.challenge52.persistence.model.enums.PeriodTypeEnum
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.Item
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum
import oliveira.fabio.challenge52.presentation.vo.enums.StatusEnum

internal class GoalMapperImpl :
    GoalMapper {
    override fun invoke(goalWithItemsEntity: GoalWithItemsEntity) = Goal(
        id = goalWithItemsEntity.goal.id,
        statusEnum = getStatus(goalWithItemsEntity),
        name = goalWithItemsEntity.goal.name,
        currentLocale = goalWithItemsEntity.goal.currentLocale,
        totalMoney = goalWithItemsEntity.goal.totalMoney,
        period = getPeriod(goalWithItemsEntity),
        items = getItems(goalWithItemsEntity)
    )

    private fun getStatus(goalWithItems: GoalWithItemsEntity) =
        when (goalWithItems.goal.goalStatus) {
            GoalStatusEnum.NEW -> StatusEnum.NEW
            GoalStatusEnum.IN_PROGRESS -> StatusEnum.IN_PROGRESS
            GoalStatusEnum.DONE -> StatusEnum.DONE
        }

    private fun getPeriod(goalWithItems: GoalWithItemsEntity): PeriodEnum {
        return when (goalWithItems.goal.periodType) {
            PeriodTypeEnum.DAILY -> {
                PeriodEnum.DAILY
            }
            PeriodTypeEnum.WEEKLY -> {
                PeriodEnum.WEEKLY
            }
            PeriodTypeEnum.MONTHLY -> {
                PeriodEnum.MONTHLY
            }
        }
    }

    private fun getItems(goalWithItemsEntity: GoalWithItemsEntity) =
        mutableListOf<Item>().apply {
            goalWithItemsEntity.items.forEach {
                add(
                    Item(
                        it.id,
                        it.position,
                        it.date,
                        it.value,
                        it.isSaved
                    )
                )
            }
        }
}