package oliveira.fabio.challenge52.goalslists.openedgoalslist.presentation.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.home.goalslists.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_goal.*
import oliveira.fabio.challenge52.extensions.doPopAnimation
import oliveira.fabio.challenge52.extensions.toStringMoney
import oliveira.fabio.challenge52.presentation.vo.Goal
import oliveira.fabio.challenge52.presentation.vo.enums.PeriodEnum
import oliveira.fabio.challenge52.presentation.vo.enums.StatusEnum

internal class OpenedGoalAdapter(private val onClickGoalListener: OnClickGoalListener) :
    RecyclerView.Adapter<OpenedGoalAdapter.GoalViewHolder>() {

    private var goalsList: MutableList<Goal> = mutableListOf()

    override fun getItemCount() = goalsList.size
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) = holder.bind(position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GoalViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
    )

    fun addList(goalsList: List<Goal>) {
        this.goalsList.addAll(goalsList)
        notifyDataSetChanged()
    }

    fun clearList() {
        goalsList.clear()
        notifyDataSetChanged()
    }

    inner class GoalViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(position: Int) {
            txtName.text = goalsList[position].name

            val period = when (goalsList[position].period) {
                PeriodEnum.DAILY -> R.string.goals_lists_days_remaining
                PeriodEnum.WEEKLY -> R.string.goals_lists_weeks_remaining
                PeriodEnum.MONTHLY -> R.string.goals_lists_months_remaining
            }

            txtCompletedItems.text = containerView.resources.getString(
                period,
                goalsList[position].getTotalItems().toString(), goalsList[position].items.size
            )

            txtMoney.text =
                goalsList[position].totalMoney.toStringMoney(currentLocale = goalsList[position].currentLocale)
            val completedPercent = goalsList[position].getTotalPercent()

            when (goalsList[position].statusEnum) {
                StatusEnum.NEW -> {
                    viewStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.color_yellow
                        )
                    )
                    txtStatus.text =
                        containerView.resources.getString(R.string.goals_lists_status_new)
                }
                StatusEnum.IN_PROGRESS -> {
                    viewStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.color_red_dark
                        )
                    )
                    txtStatus.text =
                        containerView.resources.getString(R.string.goals_lists_status_in_progress)
                }
                StatusEnum.DONE -> {
                    viewStatus.setBackgroundColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.color_green_dark
                        )
                    )
                    txtStatus.text =
                        containerView.resources.getString(R.string.goals_lists_status_done)
                }
            }

            ObjectAnimator.ofInt(
                progressBar,
                PROGRESS_TAG,
                INITIAL_VALUE, completedPercent
            ).apply {
                duration =
                    PROGRESS_ANIMATION_DURATION
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val progress = it.animatedValue as Int
                    txtPercent.text =
                        progress.toString() + containerView.context.getString(R.string.progress_value_percent)

                    when {
                        progress == FINAL_PERCENT -> {
                            val color = ContextCompat.getColor(
                                containerView.context,
                                R.color.color_green
                            )
                            progressBar.progressDrawable = ContextCompat.getDrawable(
                                containerView.context,
                                R.drawable.background_completed_progress_bar
                            )
                            txtPercent.setTextColor(color)
                            txtMoney.setTextColor(color)
                        }
                        progress > INITIAL_PERCENT -> {
                            val color = ContextCompat.getColor(
                                containerView.context,
                                R.color.color_red
                            )
                            txtPercent.setTextColor(color)
                            txtMoney.setTextColor(color)
                            progressBar.progressDrawable = ContextCompat.getDrawable(
                                containerView.context,
                                R.drawable.background_uncompleted_progress_bar
                            )
                        }
                        else -> {
                            val color = ContextCompat.getColor(
                                containerView.context,
                                android.R.color.black
                            )
                            txtPercent.setTextColor(color)
                            txtMoney.setTextColor(color)
                            progressBar.progressDrawable = ContextCompat.getDrawable(
                                containerView.context,
                                R.drawable.background_uncompleted_progress_bar
                            )
                        }
                    }
                }
                start()
            }

            containerView.setOnClickListener {
                it.doPopAnimation(POP_ANIMATION_DURATION) {
                    onClickGoalListener.onClickGoal(goalsList[position])
                }
            }
        }
    }

    interface OnClickGoalListener {
        fun onClickGoal(goal: Goal)
    }

    companion object {
        private const val PROGRESS_TAG = "progress"
        private const val INITIAL_VALUE = 0
        private const val INITIAL_PERCENT = 0
        private const val FINAL_PERCENT = 100
        private const val PROGRESS_ANIMATION_DURATION = 1000L
        private const val POP_ANIMATION_DURATION = 100L
    }
}