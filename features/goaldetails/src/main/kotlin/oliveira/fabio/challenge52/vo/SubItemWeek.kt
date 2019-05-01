package oliveira.fabio.challenge52.vo

import oliveira.fabio.challenge52.persistence.model.entity.Week
import oliveira.fabio.challenge52.vo.Item

data class SubItemWeek(
    var week: Week
) : Item(SUB_ITEM_WEEK)