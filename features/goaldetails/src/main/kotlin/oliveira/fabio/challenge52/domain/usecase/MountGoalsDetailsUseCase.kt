package oliveira.fabio.challenge52.domain.usecase

import oliveira.fabio.challenge52.domain.model.Week
import oliveira.fabio.challenge52.presentation.adapter.vo.AdapterItem
import oliveira.fabio.challenge52.presentation.vo.TopDetails
import java.util.*

interface MountGoalsDetailsUseCase {
    suspend operator fun invoke(weeks: ArrayList<Week>): MutableList<AdapterItem<TopDetails, String, Week>>
}