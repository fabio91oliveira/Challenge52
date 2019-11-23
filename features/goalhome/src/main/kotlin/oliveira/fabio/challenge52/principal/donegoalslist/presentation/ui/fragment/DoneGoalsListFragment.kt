package oliveira.fabio.challenge52.principal.donegoalslist.presentation.ui.fragment

import android.animation.ValueAnimator
import android.app.Activity
import android.content.DialogInterface
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
import com.google.android.material.snackbar.Snackbar
import features.goalhome.R
import kotlinx.android.synthetic.main.fragment_done_goals_list.*
import oliveira.fabio.challenge52.actions.Actions
import oliveira.fabio.challenge52.model.vo.ActivityResultVO
import oliveira.fabio.challenge52.persistence.model.vo.GoalWithWeeks
import oliveira.fabio.challenge52.principal.donegoalslist.presentation.ui.adapter.DoneGoalsAdapter
import oliveira.fabio.challenge52.principal.donegoalslist.presentation.viewmodel.DoneGoalsListViewModel
import oliveira.fabio.challenge52.principal.home.presentation.ui.activity.HomeActivity
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class DoneGoalsListFragment : Fragment(), DoneGoalsAdapter.OnClickGoalListener {

    private val doneGoalsListViewModel: DoneGoalsListViewModel by getKoin().getOrCreateScope(
        "myScope",
        named<HomeActivity>()
    ).viewModel(this)

    private val doneGoalsAdapter by lazy {
        DoneGoalsAdapter(
            this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
                    (data?.getSerializableExtra(HAS_CHANGED) as ActivityResultVO).let {
                        if (it.hasChanged) {
                            listDoneGoals()
                            showSnackBar(resources.getString(R.string.goals_list_a_goal_has_been_deleted))
                        }
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
                        hideLoading()
                        if (doneGoalsListViewModel.firstTime) {
                            doneGoalsListViewModel.firstTime = false
                            rvDoneGoalsList.scheduleLayoutAnimation()
                            expandBar(true)
                        }
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
            event?.let {
                when (it) {
                    true -> {
                        hideLoading()
                        doneGoalsAdapter.remove(doneGoalsListViewModel.doneGoalWithWeeksToRemove)
                        showSnackBar(
                            resources.getQuantityString(
                                R.plurals.goals_list_goals_has_has_been_deleted,
                                doneGoalsListViewModel.doneGoalWithWeeksToRemove.size
                            )
                        )
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
        rvDoneGoalsList.itemAnimator = null
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
        fabRemove.setOnClickListener {
            showConfirmDialog(
                resources.getQuantityString(
                    R.plurals.goal_details_are_you_sure_removes,
                    doneGoalsListViewModel.doneGoalWithWeeksToRemove.size
                ),
                DialogInterface.OnClickListener { dialog, _ ->
                    removeDoneGoals()
                    dialog.dismiss()
                })
        }
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

    private fun showSnackBar(message: String) =
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show()

    private fun showConfirmDialog(message: String, listener: DialogInterface.OnClickListener) =
        AlertDialog.Builder(requireContext()).apply {
            setTitle(resources.getString(R.string.goal_warning_title))
            setMessage(message)
            setPositiveButton(android.R.string.ok, listener)
            setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
        }.show()

    private fun expandBar(hasToExpand: Boolean) = appBar.setExpanded(hasToExpand)

    private fun openGoalDetailsActivity(goal: GoalWithWeeks) = startActivityForResult(
        Actions.openGoalDetails(requireContext()).putExtra(GOAL_TAG, goal)
            .putExtra(IS_FROM_DONE_GOALS, true),
        REQUEST_CODE_DETAILS
    )

    companion object {
        private const val REQUEST_CODE_DETAILS = 400
        private const val GOAL_TAG = "GOAL"
        private const val HAS_CHANGED = "HAS_CHANGED"
        private const val IS_FROM_DONE_GOALS = "IS_FROM_DONE_GOALS"
        const val ACTIVITY_ERROR = -3

        fun newInstance() =
            DoneGoalsListFragment()
    }

}
