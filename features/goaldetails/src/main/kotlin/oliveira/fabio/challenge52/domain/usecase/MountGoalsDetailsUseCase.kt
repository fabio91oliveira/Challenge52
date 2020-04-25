package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.presentation.adapter.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.ItemDetail
import oliveira.fabio.challenge52.presentation.vo.TopDetails

interface MountGoalsDetailsUseCase {
    suspend operator fun invoke(goal: Goal): MutableList<AdapterItem<TopDetails, String, ItemDetail>>
}