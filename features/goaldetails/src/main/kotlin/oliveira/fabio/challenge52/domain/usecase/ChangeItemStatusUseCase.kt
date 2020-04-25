package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.ItemDetail

interface ChangeItemStatusUseCase {
    suspend operator fun invoke(itemDetail: ItemDetail, goal: Goal)
}