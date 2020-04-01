package oliveira.fabio.challenge52.challenge.challengeselect.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import features.goalcreate.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_challenge.*
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.extensions.doPopAnimation

internal class ChallengeAdapter(
    private val challengeSelectListener: ChallengeSelectListener
) : RecyclerView.Adapter<ChallengeAdapter.ChallengeViewHolder>() {

    private var challenges: List<Challenge>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChallengeViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_challenge,
            parent,
            false
        )
    )

    override fun getItemCount() = challenges?.size ?: 0

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        challenges?.also {
            holder.bind(it[position])
        }
    }

    fun addChallenges(challenges: List<Challenge>) {
        this.challenges = challenges
        notifyDataSetChanged()
    }

    inner class ChallengeViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(challenge: Challenge) {
            txtTitle.text = challenge.name
            txtDescription.text = challenge.description
            containerView.setOnClickListener {
                it.doPopAnimation(100L) {
                    challengeSelectListener.onChallengeClick(challenge)
                }
            }
        }
    }

    interface ChallengeSelectListener {
        fun onChallengeClick(challenge: Challenge)
    }
}