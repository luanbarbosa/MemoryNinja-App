package com.luanbarbosagomes.hmr.feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.fragment_list_expressions.view.*

class FragListExpressions : BaseMainFragment() {

    private val expressionViewModel by viewModels<ExpressionsViewModel>()

    private lateinit var rootView: View

    private val expressionClickListener = { exp: Expression ->
        navigateTo(
            FragListExpressionsDirections.actionFragListExpressionsToFragExpressionDetails(exp.uid)
        )
    }
    private val expressionListAdapter = ExpressionListAdapter(listOf(), expressionClickListener)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_list_expressions, container, false)
        setupViews()
        setupObservation()
        return rootView
    }

    private fun setupViews() {
        with (rootView.expressionsList) {
            ItemTouchHelper(
                object : SwipeToDeleteCallback(context) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        expressionViewModel.deleteExpression(
                            expressionListAdapter.getExpression(viewHolder.adapterPosition)
                        )
                    }
                }
            ).attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(context)
            adapter = expressionListAdapter
        }
    }

    private fun setupObservation() {
        expressionViewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )

        expressionViewModel.loadExpressions()
    }

    private fun updateUi(state: ExpressionsViewModel.State) {
        when (state) {
            ExpressionsViewModel.State.Loading ->
                rootView.progressIndicator.show()
            is ExpressionsViewModel.State.Loaded -> {
                rootView.progressIndicator.hide()
                showExpressions(state.expressions)
            }
            is ExpressionsViewModel.State.Deleted ->
                expressionListAdapter.removeExpression(state.expression)
            is ExpressionsViewModel.State.Error -> {
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

    private fun showExpressions(expressions: List<Expression>) {
        if (expressions.isNullOrEmpty()) {
            rootView.expressionsList.hide()
        } else {
            expressionListAdapter.updateList(expressions)
            rootView.expressionsList.show()
        }
    }

}
