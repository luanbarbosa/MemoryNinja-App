package com.luanbarbosagomes.hmr.feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.list.ExpressionListAdapter.ItemViewHolder
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.expression_list_item.view.*
import kotlinx.android.synthetic.main.expressions_empty.view.*
import kotlinx.android.synthetic.main.fragment_list_expressions.view.*

class FragListExpressions : BaseMainFragment() {

    private val expressionModel by viewModels<ExpressionsViewModel>()

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
        rootView.expressionsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = expressionListAdapter
        }
    }

    private fun setupObservation() {
        expressionModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )

        expressionModel.loadExpressions()
    }

    private fun updateUi(state: ExpressionsViewModel.State) {
        when (state) {
            ExpressionsViewModel.State.Loading ->
                rootView.progressIndicator.show()
            is ExpressionsViewModel.State.Loaded -> {
                rootView.progressIndicator.hide()
                showExpressions(state.expressions)
            }
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
        hideAllViews()
        if (expressions.isNullOrEmpty()) {
            rootView.emptyLayout.show()
        } else {
            expressionListAdapter.updateList(expressions)
            rootView.expressionsList.show()
        }
    }

    private fun hideAllViews() {
        rootView.expressionsList.hide()
        rootView.emptyLayout.hide()
    }

}

internal class ExpressionListAdapter(
    expressions: List<Expression>,
    val itemClickedListened: (expression: Expression) -> Unit
) : RecyclerView.Adapter<ItemViewHolder>() {

    private var _expressions = expressions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.expression_list_item, parent, false)
        )

    override fun getItemCount(): Int = _expressions.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val expression = _expressions[position]
        holder.bind(expression)
        holder.itemView.setOnClickListener { itemClickedListened(expression) }
    }

    fun updateList(expressions: List<Expression>) {
        _expressions = expressions
        notifyDataSetChanged()
    }

    internal class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val expressionTv = view.expressionTv
        private val translationTv = view.translationTv

        fun bind(
            expression: Expression
        ) {
            expressionTv.text = expression.value
            translationTv.text = expression.translation
        }
    }
}
