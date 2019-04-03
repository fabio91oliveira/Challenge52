package oliveira.fabio.challenge52.feature.goallist.ui.activity

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_goal_list.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.goalcreate.ui.activity.GoalCreateActivity
import oliveira.fabio.challenge52.feature.goallist.ui.adapter.GoalsAdapter
import oliveira.fabio.challenge52.feature.goallist.viewmodel.GoalListViewModel
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import org.koin.android.viewmodel.ext.android.viewModel


class GoalListActivity : AppCompatActivity(), GoalsAdapter.OnClickGoalListener {
    override fun onClickAdd(goal: GoalWithWeeks) {
        goalListViewModel.goalWithWeeksToRemove.add(goal)
        fabAdd.hide()
        fabRemove.show()
        goalListViewModel.isDeleteShown = true
    }

    override fun onClickRemove(goal: GoalWithWeeks) {
        goalListViewModel.goalWithWeeksToRemove.remove(goal)
        if (goalListViewModel.goalWithWeeksToRemove.isEmpty()) {
            fabRemove.hide()
            fabAdd.show()
            goalListViewModel.isDeleteShown = false
        }
    }


    override fun onLongClick(goal: GoalWithWeeks) {
        goalListViewModel.goalWithWeeksToRemove.add(goal)
        fabAdd.hide()
        fabRemove.show()
        goalListViewModel.isDeleteShown = true
    }

    override fun onClickGoal(goal: GoalWithWeeks) {
    }

    private val goalListViewModel: GoalListViewModel by viewModel()
    private val goalsAdapter by lazy { GoalsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal_list)
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        goalListViewModel.onCleared()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_LIST) {
            goalListViewModel.listGoals()
        }
    }

    private fun init() {
        setupToolbar()
        initLiveData()
        initClickListener()
        initRecyclerView()
        showLoading()
        goalListViewModel.listGoals()
    }

    private fun initLiveData() {
        goalListViewModel.mutableLiveDataGoals.observe(this, Observer { event ->
            goalsAdapter.clearList()
            event.getContentIfNotHandled()?.let {
                when (it.isNotEmpty()) {
                    true -> {
                        goalsAdapter.addList(it)
                        hideNoGoals()
                        showGoalsList()
                        hideLoading()
                    }
                    false -> {
                        hideGoalsList()
                        showNoGoals()
                        hideLoading()
                    }
                }
            }
        })
        goalListViewModel.mutableLiveDataRemoved.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> {
                        goalsAdapter.remove(goalListViewModel.goalWithWeeksToRemove)
                        goalListViewModel.goalWithWeeksToRemove.clear()
                        fabRemove.hide()
                        fabAdd.show()
                        goalListViewModel.isDeleteShown = false

                        if (goalsAdapter.itemCount == 0) {
                            hideGoalsList()
                            showNoGoals()
                        }
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        rvGoalsList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvGoalsList.adapter = goalsAdapter
        rvGoalsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    if (goalListViewModel.isDeleteShown) fabRemove.hide() else fabAdd.hide()
                else if (dy < 0)
                    if (goalListViewModel.isDeleteShown) fabRemove.show() else fabAdd.show()
            }
        })
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.goal_List_my_goals)
    }

    private fun initClickListener() {
        fabAdd.setOnClickListener { openGoalCreateActivity() }
        fabRemove.setOnClickListener { goalListViewModel.removeGoals() }
    }

    private fun initAnimationsNoGoals() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        txtNoGoalsFirst.animation = animation
        imgNoGoals.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            txtNoGoalsFirst.translationY = progress
            imgNoGoals.translationY = progress
        }
        valueAnimator.start()
        fabAdd.show()
    }

    private fun showGoalsList() {
        rvGoalsList.visibility = View.VISIBLE
    }

    private fun hideGoalsList() {
        rvGoalsList.visibility = View.GONE
    }

    private fun showNoGoals() {
        initAnimationsNoGoals()
        noGoalsGroup.visibility = View.VISIBLE
    }

    private fun hideNoGoals() {
        noGoalsGroup.visibility = View.GONE
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    private fun openGoalCreateActivity() = Intent(this, GoalCreateActivity::class.java).apply {
        startActivityForResult(this, REQUEST_CODE_LIST)
    }

    companion object {
        private const val REQUEST_CODE_LIST = 300
    }

}
