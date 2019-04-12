package oliveira.fabio.challenge52.feature.donegoalslist.ui.fragment

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_done_goals_list.*
import oliveira.fabio.challenge52.R
import oliveira.fabio.challenge52.feature.donegoalslist.ui.adapter.DoneGoalsAdapter
import oliveira.fabio.challenge52.feature.donegoalslist.viewmodel.DoneGoalsListViewModel
import oliveira.fabio.challenge52.feature.goaldetails.ui.activity.GoalDetailsActivity
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import org.koin.android.viewmodel.ext.android.viewModel

class DoneGoalsListFragment : Fragment(), DoneGoalsAdapter.OnClickGoalListener {

    private val doneGoalsListViewModel: DoneGoalsListViewModel by viewModel()
    private val doneGoalsAdapter by lazy { DoneGoalsAdapter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_done_goals_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        when (resultCode) {
            Activity.RESULT_OK -> when (requestCode) {
                REQUEST_CODE_DETAILS -> {
                    data?.getBooleanExtra(HAS_CHANGED, false)?.let {
                        if (it) listDoneGoals()
                    }
                }
            }
            Activity.RESULT_CANCELED -> when (requestCode) {
                REQUEST_CODE_DETAILS -> {
                    data?.getBooleanExtra(HAS_CHANGED, false)?.let {
                        if (it) listDoneGoals()
                    }
                }
            }
            ACTIVITY_ERROR -> when (requestCode) {
                REQUEST_CODE_DETAILS -> {
                    showErrorDialog(resources.getString(R.string.goal_details_list_error_message))
                }
            }
        }
    }

    override fun onRotateHasGoalSelected() {
        if (doneGoalsListViewModel.isDeleteShown) {
            fabRemove.show()
        }
    }

    override fun onClickRemove(goal: GoalWithWeeks) {
        doneGoalsListViewModel.doneGoalWithWeeksToRemove.remove(goal)
        if (doneGoalsListViewModel.doneGoalWithWeeksToRemove.isEmpty()) {
            fabRemove.hide()
            doneGoalsListViewModel.isDeleteShown = false
        }
    }

    override fun onClickAdd(goal: GoalWithWeeks) {
        doneGoalsListViewModel.doneGoalWithWeeksToRemove.add(goal)
        fabRemove.show()
        doneGoalsListViewModel.isDeleteShown = true
    }

    override fun onLongClick(goal: GoalWithWeeks) {
        doneGoalsListViewModel.doneGoalWithWeeksToRemove.add(goal)
        fabRemove.show()
        doneGoalsListViewModel.isDeleteShown = true
    }

    override fun onClickGoal(goal: GoalWithWeeks) {
        openGoalDetailsActivity(goal)
    }

    private fun init() {
        setupToolbar()
        initLiveData()
        initClickListener()
        initRecyclerView()
        listDoneGoals()
    }

    private fun setupToolbar() {
        collapsingToolbar.apply {
            val tf = ResourcesCompat.getFont(context, R.font.ubuntu_bold)
            setCollapsedTitleTypeface(tf)
            setExpandedTitleTypeface(tf)
        }
    }

    private fun initLiveData() {
        doneGoalsListViewModel.mutableLiveDataDoneGoals.observe(this, Observer {
            hideDoneGoalsList()
            showLoading()
            doneGoalsAdapter.clearList()
            it?.let { list ->
                when (list.isNotEmpty()) {
                    true -> {
                        doneGoalsAdapter.addList(it)
                        hideError()
                        hideNoDoneGoals()
                        showDoneGoalsList()
                        expandBar(true)
                        hideLoading()
                    }
                    false -> {
                        hideDoneGoalsList()
                        hideError()
                        expandBar(false)
                        showNoDoneGoals()
                        hideLoading()
                    }
                }
            } ?: run {
                hideDoneGoalsList()
                hideLoading()
                showError()
            }
        })
        doneGoalsListViewModel.mutableLiveDataRemoved.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    true -> {
                        hideLoading()
                        doneGoalsAdapter.remove(doneGoalsListViewModel.doneGoalWithWeeksToRemove)
                        doneGoalsListViewModel.doneGoalWithWeeksToRemove.clear()
                        fabRemove.hide()
                        doneGoalsListViewModel.isDeleteShown = false

                        if (doneGoalsAdapter.itemCount == 0) {
                            hideDoneGoalsList()
                            showNoDoneGoals()
                            expandBar(false)
                        } else {
                            showDoneGoalsList()
                        }
                    }
                    false -> showErrorDialog(resources.getString(R.string.goals_list_error_delete))
                }
            }
        })
    }

    private fun initRecyclerView() {
        rvDoneGoalsList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvDoneGoalsList.adapter = doneGoalsAdapter
        rvDoneGoalsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    if (doneGoalsListViewModel.isDeleteShown) fabRemove.hide()
                    else if (dy < 0)
                        if (doneGoalsListViewModel.isDeleteShown) fabRemove.show()
            }
        })
    }

    private fun initClickListener() {
        fabRemove.setOnClickListener { removeDoneGoals() }
        txtError.setOnClickListener { listDoneGoals() }
        imgError.setOnClickListener { listDoneGoals() }
    }

    private fun initAnimationsNoDoneGoals() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        txtNoDoneGoalsFirst?.animation = animation
        imgNoDoneGoals?.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            txtNoDoneGoalsFirst?.translationY = progress
            imgNoDoneGoals?.translationY = progress
        }
        valueAnimator.start()
    }

    private fun initAnimationsError() {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = 2000

        val animation = AnimationSet(false)
        animation.addAnimation(fadeIn)
        txtError?.animation = animation
        imgError?.animation = animation

        val valueAnimator = ValueAnimator.ofFloat(-100f, 0f)
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.duration = 1000
        valueAnimator.addUpdateListener {
            val progress = it.animatedValue as Float
            txtError?.translationY = progress
            imgError?.translationY = progress
        }
        valueAnimator.start()
    }

    fun listDoneGoals() {
        hideError()
        hideNoDoneGoals()
        showLoading()
        doneGoalsListViewModel.listDoneGoals()
    }

    private fun removeDoneGoals() {
        doneGoalsListViewModel.removeDoneGoals()
    }

    private fun showDoneGoalsList() {
        rvDoneGoalsList.visibility = View.VISIBLE
    }

    private fun hideDoneGoalsList() {
        rvDoneGoalsList.visibility = View.GONE
    }

    private fun showNoDoneGoals() {
        initAnimationsNoDoneGoals()
        txtNoDoneGoalsFirst.visibility = View.VISIBLE
        imgNoDoneGoals.visibility = View.VISIBLE
    }

    private fun hideNoDoneGoals() {
        txtNoDoneGoalsFirst.visibility = View.GONE
        imgNoDoneGoals.visibility = View.GONE
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
    }

    private fun showError() {
        initAnimationsError()
        txtError.visibility = View.VISIBLE
        imgError.visibility = View.VISIBLE
    }

    private fun hideError() {
        txtError.visibility = View.GONE
        imgError.visibility = View.GONE
    }

    private fun showErrorDialog(message: String) = AlertDialog.Builder(requireContext()).apply {
        setTitle(resources.getString(R.string.goal_warning_title))
        setMessage(message)
        setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
    }.show()

    private fun expandBar(hasToExpand: Boolean) = appBar.setExpanded(hasToExpand)

    private fun openGoalDetailsActivity(goal: GoalWithWeeks) = Intent(context, GoalDetailsActivity::class.java).apply {
        putExtra(GOAL_TAG, goal)
        startActivityForResult(this, REQUEST_CODE_DETAILS)
    }

    companion object {
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        const val ACTIVITY_ERROR = -3

        fun newInstance() = DoneGoalsListFragment()
    }

}
