package oliveira.fabio.challenge52.goalcreate.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import features.goalcreate.R
import kotlinx.android.synthetic.main.fragment_goal_create_set_money.*
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.goalcreate.domain.vo.DateSuggestion
import oliveira.fabio.challenge52.goalcreate.domain.vo.MoneySuggestion
import oliveira.fabio.challenge52.goalcreate.presentation.adapter.DateSuggestionAdapter
import oliveira.fabio.challenge52.goalcreate.presentation.adapter.MoneySuggestionAdapter

internal class GoalCreateSetMoneyFragment :
    Fragment(R.layout.fragment_goal_create_set_money),
    DateSuggestionAdapter.DateSuggestionSuggestionClickListener,
    MoneySuggestionAdapter.MoneySuggestionSuggestionClickListener {

    private val challenge by lazy {
        arguments?.getParcelable<Challenge>(CHALLENGE)
    }

    private val moneySuggestionAdapter by lazy {
        MoneySuggestionAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onDateSuggestionClick(dateSuggestion: DateSuggestion) {
    }

    override fun onDateSuggestionClick(moneySuggestion: MoneySuggestion) {
    }

    private fun init() {
        setupMoneyRecyclerView()
        setupClickListeners()
        setupListeners()
        setupObservables()
    }

    private fun setupMoneyRecyclerView() {
        with(rvMoneySuggestions) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
                false
            )
            adapter = moneySuggestionAdapter
        }

        val a = MoneySuggestion("$ 1,00")
        val b = MoneySuggestion("$ 2,00")
        moneySuggestionAdapter.addSuggestions(
            listOf(
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b,
                a,
                b
            )
        )
    }

    private fun setupClickListeners() {
        btnContinue.setOnClickListener {
            activity?.finishAffinity()
        }
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun setupListeners() {
    }

    private fun setupObservables() {
    }

    private fun enableBtnContinue(isEnabled: Boolean) {
//        btnContinue.isEnabled = isEnabled
    }

    companion object {
        private const val GOAL_NAME = "goal_name"
        private const val CHALLENGE = "challenge"
    }

}