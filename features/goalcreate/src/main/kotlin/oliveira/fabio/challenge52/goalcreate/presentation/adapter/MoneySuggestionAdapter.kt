package oliveira.fabio.challenge52.goalcreate.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import features.goalcreate.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_suggestion.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.goalcreate.domain.vo.MoneySuggestion

internal class MoneySuggestionAdapter(
    private val moneySuggestionSuggestionClickListener: MoneySuggestionSuggestionClickListener
) : RecyclerView.Adapter<MoneySuggestionAdapter.SuggestionViewHolder>() {

    private var suggestions: List<MoneySuggestion>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SuggestionViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_suggestion,
            parent,
            false
        )
    )

    override fun getItemCount() = suggestions?.size ?: 0

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        suggestions?.also {
            holder.bind(it[position])
        }
    }

    fun addSuggestions(suggestions: List<MoneySuggestion>) {
        this.suggestions = suggestions
        notifyDataSetChanged()
    }

    inner class SuggestionViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(moneySuggestion: MoneySuggestion) {
            txtSuggestion.text = moneySuggestion.moneyPresentation
            containerView.setOnClickListener {
                it.doPopAnimation(100L) {
                    moneySuggestionSuggestionClickListener.onDateSuggestionClick(moneySuggestion)
                }
            }
        }
    }

    interface MoneySuggestionSuggestionClickListener {
        fun onDateSuggestionClick(moneySuggestion: MoneySuggestion)
    }
}