package oliveira.fabio.challenge52.challenge.challengeoverview.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import features.newgoal.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_overview_details.*
import oliveira.fabio.challenge52.challenge.challengeoverview.presentation.vo.OverviewDetails

internal class OverviewDetailsAdapter :
    RecyclerView.Adapter<OverviewDetailsAdapter.ScreenViewHolder>() {

    private var overviewDetailsList: List<OverviewDetails>? = null

    fun addOverviewDetails(overviewDetailsList: List<OverviewDetails>) {
        this.overviewDetailsList = overviewDetailsList
        notifyDataSetChanged()
    }

    fun getFinalPage(): Int {
        overviewDetailsList?.also {
            return it.size - 1
        }
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ScreenViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_overview_details,
            parent,
            false
        )
    )

    override fun getItemCount() = overviewDetailsList?.size ?: 0

    override fun onBindViewHolder(holder: ScreenViewHolder, position: Int) {
        overviewDetailsList?.also {
            holder.bind(it[position])
        }
    }

    inner class ScreenViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(overviewDetails: OverviewDetails) {
            imgTop.setImageResource(overviewDetails.resImage)
            txtTitle.text = containerView.resources.getString(overviewDetails.resTitle)
            txtDescription.text = containerView.resources.getString(overviewDetails.resDescription)
        }
    }
}