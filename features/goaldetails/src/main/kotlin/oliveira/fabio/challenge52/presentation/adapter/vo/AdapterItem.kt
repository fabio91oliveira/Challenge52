package oliveira.fabio.challenge52.presentation.adapter.vo

data class AdapterItem<K, T>(
    val first: K? = null,
    val second: T? = null,
    val viewType: ViewType
) {
    enum class ViewType(val type: Int) {
        HEADER(0),
        ITEM(1)
    }
}