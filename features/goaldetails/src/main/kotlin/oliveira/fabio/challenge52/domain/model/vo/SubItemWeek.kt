package oliveira.fabio.challenge52.domain.model.vo

import oliveira.fabio.challenge52.persistence.model.entity.Week

data class SubItemWeek(
    var week: Week
) : Item(SUB_ITEM_WEEK)