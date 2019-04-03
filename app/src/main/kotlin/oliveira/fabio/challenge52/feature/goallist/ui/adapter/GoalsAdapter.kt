package oliveira.fabio.challenge52.feature.goallist.ui.adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_goal.*
import kotlinx.android.synthetic.main.item_goal.view.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.util.extension.toCurrentFormat
import java.text.NumberFormat

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
            containerView.contentCard.setBackgroundColor(
                ContextCompat.getColor(
                    containerView.context,
                    R.color.colorWhite
                )
            )
            val progress = goalsList[position].getPercentOfConclusion()
            val textConclusion =
                progress.toString() + containerView.context.getString(R.string.goal_list_progress_value_percent)

            txtName.text = goalsList[position].goal.name
            txtValue.text = NumberFormat.getCurrencyInstance().format((goalsList[position].goal.totalValue / 100))
            txtWeeksValue.text = goalsList[position].getRemainingWeeksCount().toString()
            txtStartDateValue.text = goalsList[position].getStartDate().toCurrentFormat(containerView.context)
            txtEndDateValue.text = goalsList[position].getEndDate().toCurrentFormat(containerView.context)
            txtDoneValue.text = textConclusion
            if (progress > 0) {
                txtDoneValue.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorGreen))
            }
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
                    }
                }
            }
        }

        private fun animate() {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator()
            fadeIn.duration = 900

            val animation = AnimationSet(false)
            animation.addAnimation(fadeIn)
            containerView.animation = animation

            val valueAnimator = ValueAnimator.ofFloat(-300f, 0f)
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.duration = 500
            valueAnimator.addUpdateListener {
                val progress = it.animatedValue as Float
                containerView.translationX = progress
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
    }
}