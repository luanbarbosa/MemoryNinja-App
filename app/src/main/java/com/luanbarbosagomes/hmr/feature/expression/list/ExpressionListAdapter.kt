package com.luanbarbosagomes.hmr.feature.expression.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import kotlinx.android.synthetic.main.expression_list_item.view.*
import kotlin.math.exp

class ExpressionListAdapter(
    expressions: List<Expression>,
    val itemClickedListened: (expression: Expression) -> Unit
) : RecyclerView.Adapter<ExpressionListAdapter.ItemViewHolder>() {

    private var _expressions: MutableList<Expression> = expressions.toMutableList()

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

    fun updateList(
        expressions: List<Expression>,
        fullRefresh: Boolean
    ) {
        if (fullRefresh) {
            _expressions = expressions.toMutableList()
        } else {
            _expressions.addAll(expressions)
        }
        notifyDataSetChanged()
    }

    fun getExpression(position: Int) = _expressions[position]

    fun removeExpression(expression: Expression) {
        removeAt(position = _expressions.indexOf(expression))
    }

    private fun removeAt(position: Int) {
        _expressions.removeAt(position)
        notifyItemRemoved(position)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val expressionTv = view.expressionTv
        private val translationTv = view.translationTv
        private val levelTv = view.levelTv

        @SuppressLint("DefaultLocale")
        fun bind(expression: Expression) {
            expressionTv.text = expression.value.capitalize()
            translationTv.text = expression.translation.capitalize()
            levelTv.text = levelTv.context.getString(expression.level().string).toLowerCase()
        }
    }
}
