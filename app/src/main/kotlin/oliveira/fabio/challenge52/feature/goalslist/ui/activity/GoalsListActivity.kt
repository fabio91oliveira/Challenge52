package oliveira.fabio.challenge52.feature.goalslist.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_goals_list.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.goalcreate.ui.activity.GoalCreateActivity
import oliveira.fabio.challenge52.feature.goaldetails.ui.activity.GoalDetailsActivity
import oliveira.fabio.challenge52.feature.goalslist.ui.adapter.GoalsAdapter
import oliveira.fabio.challenge52.feature.goalslist.viewmodel.GoalsListViewModel
import oliveira.fabio.challenge52.model.vo.GoalWithWeeks
import org.koin.android.viewmodel.ext.android.viewModel


class GoalsListActivity : AppCompatActivity(), GoalsAdapter.OnClickGoalListener {

    private val goalsListViewModel: GoalsListViewModel by viewModel()
    private val goalsAdapter by lazy { GoalsAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals_list)

        savedInstanceState?.let {
            setupToolbar()
            initLiveData()
            initClickListener()
            initRecyclerView()
        } ?: run {
            init()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LIST -> {
                    resetAnimation()
                    goalsListViewModel.listGoals()
                }
                REQUEST_CODE_DETAILS -> {
                    data?.getBooleanExtra(HAS_CHANGED, false)?.let {
                        if (it) goalsListViewModel.listGoals()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            when (requestCode) {
                REQUEST_CODE_LIST -> {
                    resetAnimation()
                }
                REQUEST_CODE_DETAILS -> {
                    data?.getBooleanExtra(HAS_CHANGED, false)?.let {
                        if (it) goalsListViewModel.listGoals()
                    }
                }
            }
        }
    }

    override fun onClickAdd(goal: GoalWithWeeks) {
        goalsListViewModel.goalWithWeeksToRemove.add(goal)
        fabAdd.hide()
        fabRemove.show()
        goalsListViewModel.isDeleteShown = true
    }

    override fun onClickRemove(goal: GoalWithWeeks) {
        goalsListViewModel.goalWithWeeksToRemove.remove(goal)
        if (goalsListViewModel.goalWithWeeksToRemove.isEmpty()) {
            fabRemove.hide()
            fabAdd.show()
            goalsListViewModel.isDeleteShown = false
        }
    }

    override fun onLongClick(goal: GoalWithWeeks) {
        goalsListViewModel.goalWithWeeksToRemove.add(goal)
        fabAdd.hide()
        fabRemove.show()
        goalsListViewModel.isDeleteShown = true
    }

    override fun onClickGoal(goal: GoalWithWeeks) {
        openGoalDetailsActivity(goal)
    }

    private fun init() {
        setupToolbar()
        initLiveData()
        initClickListener()
        initRecyclerView()
        showLoading()
        goalsListViewModel.listGoals()
    }

    private fun setupToolbar() {
        collapsingToolbar.apply {
            val tf = ResourcesCompat.getFont(context, R.font.ubuntu_bold)
            setCollapsedTitleTypeface(tf)
            setExpandedTitleTypeface(tf)
        }
    }

    private fun initLiveData() {
        goalsListViewModel.mutableLiveDataGoals.observe(this, Observer {
            hideGoalsList()
            showLoading()
            goalsAdapter.clearList()
            it?.let { list ->
                when (list.isNotEmpty()) {
                    true -> {
                        goalsAdapter.addList(it)
                        hideError()
                        hideNoGoals()
                        showGoalsList()
                        expandBar(true)
                        hideLoading()
                    }
                    false -> {
                        hideGoalsList()
                        hideError()
                        expandBar(false)
                        showNoGoals()
                        hideLoading()
                    }
                }
            } ?: run {
                hideGoalsList()
                hideLoading()
                showError()
            }
        })
        goalsListViewModel.mutableLiveDataRemoved.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> {
                        goalsAdapter.remove(goalsListViewModel.goalWithWeeksToRemove)
                        goalsListViewModel.goalWithWeeksToRemove.clear()
                        fabRemove.hide()
                        fabAdd.show()
                        goalsListViewModel.isDeleteShown = false

                        if (goalsAdapter.itemCount == 0) {
                            hideGoalsList()
                            showNoGoals()
                            expandBar(false)
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
                    if (goalsListViewModel.isDeleteShown) fabRemove.hide() else fabAdd.hide()
                else if (dy < 0)
                    if (goalsListViewModel.isDeleteShown) fabRemove.show() else fabAdd.show()
            }
        })
    }

    private fun initClickListener() {
        fabAdd.setOnClickListener { revealButton() }
        fabRemove.setOnClickListener { goalsListViewModel.removeGoals() }
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

    private fun showError() {
        // TODO
    }

    private fun hideError() {
        // TODO
    }

    private fun expandBar(hasToExpand: Boolean) = appBar.setExpanded(hasToExpand)

    private fun openGoalCreateActivity() = Intent(this, GoalCreateActivity::class.java).apply {
        startActivityForResult(this, REQUEST_CODE_LIST)
    }

    private fun openGoalDetailsActivity(goal: GoalWithWeeks) = Intent(this, GoalDetailsActivity::class.java).apply {
        putExtra(GOAL_TAG, goal)
        startActivityForResult(this, REQUEST_CODE_DETAILS)
    }

    private fun revealButton() {
        revealView.visibility = View.VISIBLE
        fabAdd.hide()

        val cx = revealView.width
        val cy = revealView.height


        val x = (getButtonSize() / 2 + fabAdd.x)
        val y = (getButtonSize() / 2 + fabAdd.y)

        val finalRadius = Math.max(cx, cy) * 1.2f

        val reveal = ViewAnimationUtils
            .createCircularReveal(
                revealView,
                x.toInt(),
                y.toInt(),
                getButtonSize(), finalRadius
            )

        reveal.duration = 350
        reveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                openGoalCreateActivity()
            }
        })
        reveal.start()
    }

    private fun resetAnimation() {
        revealView.visibility = View.INVISIBLE
        fabAdd.show()
    }

    private fun getButtonSize() = resources.getDimension(R.dimen.button_height)

    companion object {
        private const val REQUEST_CODE_LIST = 300
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
    }

}
