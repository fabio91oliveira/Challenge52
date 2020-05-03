package oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal data class OverviewDetails(
    @DrawableRes val resImage: Int,
    @StringRes val resTitle: Int,
    @StringRes val resDescription: Int
)