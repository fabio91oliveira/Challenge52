package oliveira.fabio.challenge52.presentation.adapter.vo

data class AdapterItem<F, S, T>(
    val first: F? = null,
    val second: S? = null,
    val third: T? = null,
    val viewType: ViewType
) {
    enum class ViewType(val type: Int) {
        HEADER(0),
        ITEM(1),
        DETAILS(2)
    }
}