package oliveira.fabio.challenge52.feature.goaldetails.vo

open class Item(var viewType: Int) {
    fun getHeader() = this as HeaderItem
    fun getDetails() = this as SubItemDetails
    fun getWeek() = this as SubItemWeek

    companion object {
        const val HEADER_ITEM = 1
        const val SUB_ITEM_DETAILS = 2
        const val SUB_ITEM_WEEK = 3
    }
}