package oliveira.fabio.challenge52.feature.help.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_question.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.help.vo.Question


class QuestionsAdapter :
    RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {
    private var list: MutableList<Question> = mutableListOf()

    override fun getItemCount() = list.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false))

    fun addList(list: List<Question>) {
        this.list.addAll(list)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bind(question: Question) {
            txtQuestion.text = question.title
            txtAnswer.text = question.answer

            if (!question.isOpen) {
                txtAnswer.visibility = View.GONE
                imgArrow.rotation = 180f
            } else {
                txtAnswer.visibility = View.VISIBLE
                imgArrow.rotation = 0f
            }

            containerView.setOnClickListener {
                if (question.isOpen) {
                    imgArrow.animate().rotation(180f)
                    txtAnswer.visibility = View.GONE
                    question.isOpen = false
                } else {
                    imgArrow.animate().rotation(0f)
                    txtAnswer.visibility = View.VISIBLE
                    question.isOpen = true
                }
            }
        }

    }
}