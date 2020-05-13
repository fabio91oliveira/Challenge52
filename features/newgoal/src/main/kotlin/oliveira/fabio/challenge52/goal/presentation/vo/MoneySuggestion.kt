package oliveira.fabio.challenge52.goal.presentation.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MoneySuggestion(
    val currency: String,
    val moneyPresentation: String,
    val moneyValue: Float
) : Parcelable