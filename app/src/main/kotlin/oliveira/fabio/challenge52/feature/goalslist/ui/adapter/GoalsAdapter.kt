package oliveira.fabio.challenge52.feature.goalslist.ui.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_goal.*
import kotlinx.android.synthetic.main.item_goal.view.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.util.extension.toCurrency
import oliveira.fabio.challenge52.util.extension.toCurrentDateSystemString
import java.text.DateFormat


class GoalsAdapter(private val onClickGoalListener: OnClickGoalListener) :
    RecyclerView.Adapter<GoalsAdapter.GoalViewHolder>() {

    private var lastPosition = 0
    private var goalsList: MutableList<GoalWithWeeks> = mutableListOf()

    override fun getItemCount() = goalsList.size
    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) = holder.bind(position)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GoalViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_goal, parent, false)
    )

    fun addList(goalsList: List<GoalWithWeeks>) {
        this.goalsList.addAll(goalsList)
        notifyDataSetChanged()
    }

    fun remove(goalsList: List<GoalWithWeeks>) {
        this.goalsList.removeAll(goalsList)
        notifyDataSetChanged()
    }

    fun clearList() {
        lastPosition = goalsList.size
        goalsList.clear()
        notifyDataSetChanged()
    }

    inner class GoalViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(position: Int) {
            if (goalsList[position].isSelected) {
                onClickGoalListener.onRotateHasGoalSelected()
                containerView.contentCard.setBackgroundColor(
                    ContextCompat.getColor(
                        containerView.context,
                        R.color.colorSofterGrey
                    )
                )
            } else {
                containerView.contentCard.setBackgroundColor(
                    ContextCompat.getColor(
                        containerView.context,
                        R.color.colorWhite
                    )
                )
            }
            txtName.text = goalsList[position].goal.name
            txtValue.text = goalsList[position].goal.totalValue.toCurrency()
            txtWeeksValue.text = goalsList[position].getRemainingWeeksCount().toString()
            txtStartDateValue.text = goalsList[position].getStartDate()
                .toCurrentDateSystemString(DateFormat.SHORT)
            txtEndDateValue.text =
                goalsList[position].getEndDate().toCurrentDateSystemString(DateFormat.SHORT)

            val progress = goalsList[position].getPercentOfConclusion()
            when (progress > 0) {
                true -> txtDoneValue.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorGreen))
                false -> txtDoneValue.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))
            }

            val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, progress)
            animation.duration = 1000
            animation.interpolator = DecelerateInterpolator()
            animation.addUpdateListener {
                val prog = it.animatedValue as Int
                txtDoneValue.text =
                    prog.toString() + containerView.context.getString(R.string.progress_value_percent)
            }
            animation.start()

            if (position >= lastPosition) animate()

            containerView.setOnLongClickListener {
                if (!goalsList[position].isSelected) {
                    goalsList[position].isSelected = true
                    containerView.contentCard.setBackgroundColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.colorSofterGrey
                        )
                    )
                    onClickGoalListener.onLongClick(goalsList[position])
                    true
                } else {
                    false
                }
            }

            containerView.setOnClickListener {
                if (goalsList[position].isSelected) {
                    containerView.contentCard.setBackgroundColor(
                        ContextCompat.getColor(
                            containerView.context,
                            R.color.colorWhite
                        )
                    )
                    onClickGoalListener.onClickRemove(goalsList[position])
                    goalsList[position].isSelected = false
                } else {
                    var hasAtLeastOneSelected = false
                    for (i in 0 until goalsList.size) {
                        if (goalsList[i].isSelected) {
                            hasAtLeastOneSelected = true
                            break
                        }
                    }

                    if (hasAtLeastOneSelected) {
                        goalsList[position].isSelected = true
                        containerView.contentCard.setBackgroundColor(
                            ContextCompat.getColor(
                                containerView.context,
                                R.color.colorSofterGrey
                            )
                        )
                        onClickGoalListener.onClickAdd(goalsList[position])
                    } else {
                        onClickGoalListener.onClickGoal(goalsList[position])
                    }
                }
            }
        }

        private fun animate() {
            val valueAnimator = ValueAnimator.ofFloat(-500f, 0f)
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.duration = 500
            valueAnimator.addUpdateListener {
                val progress = it.animatedValue as Float
                containerView.cardGoal.translationX = progress
            }
            valueAnimator.start()
            lastPosition++
        }
    }

    interface OnClickGoalListener {
        fun onClickGoal(goal: GoalWithWeeks)
        fun onLongClick(goal: GoalWithWeeks)
        fun onClickAdd(goal: GoalWithWeeks)
        fun onClickRemove(goal: GoalWithWeeks)
        fun onRotateHasGoalSelected()
    }
}