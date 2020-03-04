package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem

interface ItemMapper {
    operator fun invoke(goal: Goal): MutableList<AdapterItem<String, Week>>
}