package oliveira.fabio.challenge52.domain.mapper

import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.TopDetails

interface DetailsMapper {
    operator fun invoke(goal: Goal): MutableList<AdapterItem<TopDetails, String, ItemDetail>>
}