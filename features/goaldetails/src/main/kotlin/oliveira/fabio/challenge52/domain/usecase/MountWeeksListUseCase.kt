package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.Goal
import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem

interface MountWeeksListUseCase {
    suspend operator fun invoke(goal: Goal): MutableList<AdapterItem<String, Week>>
}