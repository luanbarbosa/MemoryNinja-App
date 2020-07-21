package com.luanbarbosagomes.hmr.feature.expression.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.expression.list.ExpressionsViewModel.State
import com.luanbarbosagomes.hmr.feature.main.ActivityMain
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.fragment_list_expressions.view.*
import kotlinx.android.synthetic.main.full_screen_loading.view.*

class FragListExpressions : BaseMainFragment() {

    private val expressionViewModel by activityViewModels<ExpressionsViewModel> { viewModelFactory }

    private lateinit var rootView: View
    private var isLoadingMore: Boolean = false

    private val expressionClickListener = { exp: Expression ->
        navigateTo(
            FragListExpressionsDirections.actionFragListExpressionsToFragExpressionDetails(
                exp.uid
            )
        )
    }
    private val expressionListAdapter =
        ExpressionListAdapter(
            listOf(),
            expressionClickListener
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_list_expressions, container, false).also {
        rootView = it
        setupViews()
        setupObservation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ActivityMain).setupToolbar(
            rootView.bottomBar,
            settingsCallback = {
                navigateTo(
                    FragListExpressionsDirections.actionFragListExpressionsToSettingsBottomSheet()
                )
            },
            filterCallback = {
                navigateTo(
                    FragListExpressionsDirections.actionFragListExpressionsToExpressionFilterBottomSheet()
                )
            }
        )
    }

    override fun onResume() {
        super.onResume()
        expressionViewModel.reload()
    }

    private fun setupViews() {
        with(rootView) {
            toolbar.title = " "
            setToolbarVisibilityChangeBehavior()

            newBtn.setOnClickListener {
                navigateTo(
                    FragListExpressionsDirections.actionFragListExpressionsToFragNewExpression()
                )
            }

            expressionsList.apply {
                ItemTouchHelper(
                    object : SwipeToDeleteCallback(context) {
                        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                            val expressionToBeDeleted =
                                expressionListAdapter.getExpression(viewHolder.adapterPosition)
                            expressionViewModel.deleteExpression(expressionToBeDeleted)
                            expressionListAdapter.removeExpression(expressionToBeDeleted)
                        }
                    }
                ).attachToRecyclerView(this)
                layoutManager = LinearLayoutManager(context)
                adapter = expressionListAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener(){

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (!isLoadingMore) {
                            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() >= expressionListAdapter.itemCount / 2) {
                                isLoadingMore = true
                                expressionViewModel.loadMore()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun setToolbarVisibilityChangeBehavior() {
        with(rootView) {
            var scrollRange = -1
            toolbarLayout.addOnOffsetChangedListener(
                AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                    if (scrollRange == -1) {
                        scrollRange = appBarLayout.totalScrollRange
                    }
                    if (scrollRange + verticalOffset == 0) {
                        toolbar.toolbarTitle.show()
                        toolbar.toolbarImg.show()
                    } else {
                        toolbar.toolbarTitle.hide()
                        toolbar.toolbarImg.hide()
                    }
                }
            )
        }
    }

    private fun setupObservation() {
        expressionViewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            State.Loading -> {
                changeLoadingIndicatorVisibility(toVisible = true)
            }
            is State.Reloaded -> {
                changeLoadingIndicatorVisibility(toVisible = false)
                showExpressions(state.expressions, fullRefresh = true)
            }
            is State.LoadedMore -> {
                changeLoadingIndicatorVisibility(toVisible = false)
                showExpressions(state.expressions, fullRefresh = false)
                isLoadingMore = false
            }
            is State.Error -> {
                navigateTo(
                    FragListExpressionsDirections.actionFragListExpressionsToFragError(
                        errorMsg = state.error.localizedMessage
                    ),
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.fragListExpressions, true)
                        .build()
                )
            }
        }
    }

    private fun changeLoadingIndicatorVisibility(toVisible: Boolean) {
        with(rootView) {
            if (toVisible) {
                progressIndicator.show()
                newBtn.hide()

            } else {
                progressIndicator.hide()
                newBtn.show()
            }
        }
    }

    private fun showExpressions(
        expressions: List<Expression>,
        fullRefresh: Boolean
    ) {
        if (!expressions.isNullOrEmpty()) {
            expressionListAdapter.updateList(expressions, fullRefresh)
        }
    }

}
