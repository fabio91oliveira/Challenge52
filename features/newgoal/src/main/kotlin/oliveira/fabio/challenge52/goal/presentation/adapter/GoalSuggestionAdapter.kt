package oliveira.fabio.challenge52.goal.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import features.newgoal.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_goal_suggestion.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.goal.presentation.vo.GoalSuggestion

internal class GoalSuggestionAdapter(
    private val goalSuggestionClickListener: GoalSuggestionClickListener
) : RecyclerView.Adapter<GoalSuggestionAdapter.ViewHolder>() {

    private var suggestions: List<GoalSuggestion>? = null

    override fun getItemViewType(position: Int): Int {
        suggestions?.apply {
            this[position].type?.also {
                return if (it == GoalSuggestion.Type.SUGGESTION)
                    VIEW_TYPE_SUGGESTION else VIEW_TYPE_NO_SUGGESTION
            }
        }
        return VIEW_TYPE_SUGGESTION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            VIEW_TYPE_SUGGESTION -> SuggestionViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_goal_suggestion,
                    parent,
                    false
                )
            )
            else -> NoSuggestionViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_goal_not_a_suggestion,
                    parent,
                    false
                )
            )
        }

    override fun getItemCount() = suggestions?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        suggestions?.also {
            holder.bind(it[position])
        }
    }

    fun addSuggestions(suggestions: List<GoalSuggestion>) {
        this.suggestions = suggestions
        notifyDataSetChanged()
    }

    inner class SuggestionViewHolder(override val containerView: View) :
        ViewHolder(containerView) {

        override fun bind(goalSuggestion: GoalSuggestion) {
            txtName.text = goalSuggestion.name
            containerView.setOnClickListener {
                it.doPopAnimation(100L) {
                    goalSuggestionClickListener.onSuggestionsClick(goalSuggestion)
                }
            }
        }
    }

    inner class NoSuggestionViewHolder(override val containerView: View) :
        ViewHolder(containerView) {

        override fun bind(goalSuggestion: GoalSuggestion) {
            txtName.text =
                containerView.resources.getString(R.string.goal_suggestions_list_item_not_a_suggestion)
            containerView.setOnClickListener {
                it.doPopAnimation(100L) {
                    goalSuggestionClickListener.onSuggestionsClick(goalSuggestion)
                }
            }
        }
    }

    abstract inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        abstract fun bind(goalSuggestion: GoalSuggestion)
    }

    interface GoalSuggestionClickListener {
        fun onSuggestionsClick(goalSuggestion: GoalSuggestion)
    }

    companion object {
        private const val VIEW_TYPE_SUGGESTION = 1
        private const val VIEW_TYPE_NO_SUGGESTION = 2
    }
}