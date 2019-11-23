package oliveira.fabio.challenge52.principal.help.presentation.ui.adapter

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import features.goalhome.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.*
import oliveira.fabio.challenge52.principal.help.domain.model.vo.Question


class QuestionsAdapter :
    RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {
    private var list: MutableList<Question> = mutableListOf()
    private var lastPosition = 0

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false))

    fun addList(list: List<Question>) {
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(question: Question) {
            txtQuestion.text = question.title
            txtAnswer.text = question.answer

            if (!question.isOpen) {
                txtQuestion.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorBlack))
                txtAnswer.visibility = View.GONE
                btnAction.setBackgroundResource(R.drawable.ic_add_circle)
            } else {
                txtQuestion.setTextColor(ContextCompat.getColor(containerView.context, R.color.colorAccent))
                txtAnswer.visibility = View.VISIBLE
                btnAction.setBackgroundResource(R.drawable.ic_remove_circle)
            }

//            if (adapterPosition >= lastPosition) animate()

            containerView.setOnClickListener {
                if (question.isOpen) {
                    btnAction.setBackgroundResource(R.drawable.ic_add_circle)
                    fadeText(txtAnswer, 1f, 0f, 200)
                    txtAnswer.visibility = View.GONE
                    question.isOpen = false
                    changeTextQuestionColor(
                        ContextCompat.getColor(containerView.context, R.color.colorAccent),
                        ContextCompat.getColor(containerView.context, R.color.colorBlack)
                    )

                } else {
                    btnAction.setBackgroundResource(R.drawable.ic_remove_circle)
                    txtAnswer.visibility = View.VISIBLE
                    fadeText(txtAnswer, 0f, 1f, 700)
                    transformationY(txtAnswer, -50f, 0f, 200)
                    question.isOpen = true
                    changeTextQuestionColor(
                        ContextCompat.getColor(containerView.context, R.color.colorBlack),
                        ContextCompat.getColor(containerView.context, R.color.colorAccent)
                    )
                }
            }
        }

        private fun changeTextQuestionColor(firstColor: Int, secondColor: Int) = ObjectAnimator.ofObject(
            txtQuestion,
            "textColor",
            ArgbEvaluator(),
            firstColor,
            secondColor
        )
            .setDuration(250)
            .start()

        private fun fadeText(textView: AppCompatTextView, from: Float, to: Float, duration: Long) {
            val fadeIn = AlphaAnimation(from, to)
            fadeIn.interpolator = DecelerateInterpolator()
            fadeIn.duration = duration

            val animation = AnimationSet(false)
            animation.addAnimation(fadeIn)
            textView.animation = animation
        }

        private fun transformationY(view: View, from: Float, to: Float, duration: Long) {
            val valueAnimator = ValueAnimator.ofFloat(from, to)
            valueAnimator.interpolator = AccelerateDecelerateInterpolator()
            valueAnimator.duration = duration
            valueAnimator.addUpdateListener {
                val progress = it.animatedValue as Float
                view.translationY = progress
            }
            valueAnimator.start()
        }

        private fun animate() {
            val valueAnimator = ValueAnimator.ofFloat(-500f, 0f)
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
}