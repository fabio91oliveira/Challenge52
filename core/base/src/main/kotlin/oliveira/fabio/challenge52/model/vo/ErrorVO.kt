package br.com.brmalls.store.androidapp.vo

data class ErrorVO(
    val statusCode: Int,
    val icon: Int,
    val title: String? = null,
    val description: String? = null,
    val buttonText: String? = null
)