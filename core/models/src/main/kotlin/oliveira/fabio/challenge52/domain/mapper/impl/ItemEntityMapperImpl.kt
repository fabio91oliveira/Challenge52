package oliveira.fabio.challenge52.domain.mapper.impl

import oliveira.fabio.challenge52.domain.mapper.ItemEntityMapper
import oliveira.fabio.challenge52.persistence.model.entity.ItemEntity
import oliveira.fabio.challenge52.presentation.vo.GoalToSave

internal class ItemEntityMapperImpl :
    ItemEntityMapper {
    override fun invoke(goalToSave: GoalToSave, idGoal: Long) = createItemsList(goalToSave, idGoal)

    private fun createItemsList(
        goalToSave: GoalToSave,
        idGoal: Long
    ) =
        mutableListOf<ItemEntity>().apply {
            goalToSave.items?.forEach {
                add(
                    ItemEntity(
                        idGoal = idGoal,
                        position = it.position,
                        date = it.date,
                        value = it.money
                    )
                )
            } ?: throw Exception()
        }
}