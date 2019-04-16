package oliveira.fabio.challenge52.feature.goaldetails.vo

data class SubItemDetails(
    var totalPercent: Int,
    var remainingWeeks: Int,
    var totalWeeks: Int,
    var totalAccumulated: Float,
    var totalMoney: Float
) : Item(SUB_ITEM_DETAILS)