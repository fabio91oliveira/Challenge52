package oliveira.fabio.challenge52.goalcreate.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import features.goalcreate.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_suggestion.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.goalcreate.domain.vo.DateSuggestion

internal class DateSuggestionAdapter(
    private val dateSuggestionSuggestionClickListener: DateSuggestionSuggestionClickListener
) : RecyclerView.Adapter<DateSuggestionAdapter.SuggestionViewHolder>() {

    private var suggestions: List<DateSuggestion>? = null

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

    fun addSuggestions(suggestions: List<DateSuggestion>) {
        this.suggestions = suggestions
        notifyDataSetChanged()
    }

    inner class SuggestionViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(dateSuggestion: DateSuggestion) {
            txtSuggestion.text = dateSuggestion.presentationName
            containerView.setOnClickListener {
                it.doPopAnimation(100L) {
                    dateSuggestionSuggestionClickListener.onDateSuggestionClick(dateSuggestion)
                }
            }
        }
    }

    interface DateSuggestionSuggestionClickListener {
        fun onDateSuggestionClick(dateSuggestion: DateSuggestion)
    }
}