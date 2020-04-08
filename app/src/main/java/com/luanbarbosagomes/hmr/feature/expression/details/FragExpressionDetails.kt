package com.luanbarbosagomes.hmr.feature.expression.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.navArgs
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Expression
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel
import com.luanbarbosagomes.hmr.feature.expression.ExpressionViewModel.State
import com.luanbarbosagomes.hmr.feature.expression.details.FragExpressionDetailsDirections.Companion.actionFragExpressionDetailsToFragEditExpression
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.fragment_expression_details.view.*
import kotlinx.android.synthetic.main.full_screen_loading.view.*

class FragExpressionDetails : BaseMainFragment() {

    private val model by viewModels<ExpressionViewModel>()

    private val args by navArgs<FragExpressionDetailsArgs>()

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_expression_details, container, false).also {
        rootView = it
        setupUi()
        observeData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.loadExpression(args.expressionUid)
    }

    private fun setupUi() {
        rootView.editBtn.setOnClickListener {
            model.currentExpression?.let {
                navigateTo(
                    actionFragExpressionDetailsToFragEditExpression(expressionUid = it.uid),
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.fragExpressionDetails, true)
                        .build()
                )
            }
        }
    }

    private fun observeData() {
        model.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            is State.Loading -> rootView.progressIndicator.show()
            is State.Loaded -> {
                showExpression(state.expression)
            }
            is State.Error -> {
                navigateTo(
                    FragExpressionDetailsDirections.actionFragExpressionDetailsToFragError(
                        errorMsg = state.error.localizedMessage
                    ),
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.fragExpressionDetails, true)
                        .build()
                )
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun showExpression(expression: Expression) {
        rootView.apply {
            expressionTv.text = expression.value.capitalize()
            translationTv.text = expression.translation.capitalize()
            editBtn.show()
            progressIndicator.hide()
        }
    }

}
