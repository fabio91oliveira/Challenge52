package oliveira.fabio.challenge52.feature.goaldetails.vo

import oliveira.fabio.challenge52.model.entity.Week

data class SubItemWeek(
    var week: Week
) : Item(Item.SUB_ITEM_WEEK)