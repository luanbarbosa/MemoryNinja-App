package com.luanbarbosagomes.hmr.feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luanbarbosagomes.hmr.LoadStatus
import com.luanbarbosagomes.hmr.LoadStatus.FAILED
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.list.ExpressionListAdapter.ItemViewHolder
import com.luanbarbosagomes.hmr.feature.main.MainViewModel
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.expression_list_item.view.*
import kotlinx.android.synthetic.main.expressions_empty.view.*
import kotlinx.android.synthetic.main.expressions_error.view.*
import kotlinx.android.synthetic.main.fragment_list_expressions.view.*

class FragListExpressions : BaseMainFragment() {

    private val expressionModel by viewModels<ExpressionsViewModel>()
    private val mainModel by viewModels<MainViewModel>()

    private lateinit var rootView: View

    private val expressionClickListener = { exp: Expression -> mainModel.detailExpression(exp) }
    private val expressionListAdapter = ExpressionListAdapter(listOf(), expressionClickListener)

    private val loadStatusObserver = Observer<LoadStatus> { status ->
        when (status) {
            FAILED -> showErrorState()
            else -> {
            }
        }
    }

    private val expressionsObserver = Observer<List<Expression>> { expressions ->
        if (expressions.isNullOrEmpty()) {
            showEmptyState()
        } else {
            showExpressions(expressions)
        }
    }

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
        expressionModel
            .state
            .observe(viewLifecycleOwner, loadStatusObserver)

        expressionModel
            .expressionsData
            .observe(viewLifecycleOwner, expressionsObserver)

        expressionModel.loadExpressions()
    }

    private fun showExpressions(expressions: List<Expression>) {
        hideAllViews()
        expressionListAdapter.updateList(expressions)
        rootView.expressionsList.show()
    }

    private fun showEmptyState() {
        hideAllViews()
        rootView.emptyLayout.show()
    }

    private fun showErrorState() {
        hideAllViews()
        rootView.errorLayout.show()
    }

    private fun hideAllViews() {
        rootView.expressionsList.hide()
        rootView.errorLayout.hide()
        rootView.emptyLayout.hide()
    }

    companion object {
        val new = FragListExpressions()
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
