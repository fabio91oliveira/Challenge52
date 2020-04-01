package oliveira.fabio.challenge52.goalcreate.presentation.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import features.goalcreate.R
import kotlinx.android.synthetic.main.fragment_goal_create_set_periods.*
import oliveira.fabio.challenge52.domain.vo.Challenge
import oliveira.fabio.challenge52.goalcreate.domain.vo.DateSuggestion
import oliveira.fabio.challenge52.goalcreate.domain.vo.MoneySuggestion
import oliveira.fabio.challenge52.goalcreate.presentation.adapter.DateSuggestionAdapter
import oliveira.fabio.challenge52.goalcreate.presentation.adapter.MoneySuggestionAdapter
import java.util.*

internal class GoalCreateSetPeriodsFragment :
    Fragment(R.layout.fragment_goal_create_set_periods),
    DateSuggestionAdapter.DateSuggestionSuggestionClickListener,
    MoneySuggestionAdapter.MoneySuggestionSuggestionClickListener {

    private val challenge by lazy {
        arguments?.getParcelable<Challenge>(CHALLENGE)
    }

    private val dateSuggestionAdapter by lazy {
        DateSuggestionAdapter(this)
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
        setupDateRecyclerView()
        setupClickListeners()
        setupListeners()
        setupObservables()
    }

    private fun setupDateRecyclerView() {
        with(rvDateStartSuggestions) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                context,
                androidx.recyclerview.widget.RecyclerView.HORIZONTAL,
                false
            )
            adapter = dateSuggestionAdapter
        }

        val today = DateSuggestion("Today", Date())
        val tomorrow = DateSuggestion("Tomorrow", Date())
        dateSuggestionAdapter.addSuggestions(listOf(today, tomorrow))
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        btnContinue.setOnClickListener {
            findNavController().navigate(
                R.id.action_go_to_fragment_goal_create_set_money
            )
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
        private const val CHALLENGE = "challenge"
    }

}