package com.luanbarbosagomes.hmr.feature.expression.add

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.expression.add.NewExpressionViewModel.State
import com.luanbarbosagomes.hmr.utils.withDelay
import kotlinx.android.synthetic.main.fragment_new_expression.*
import kotlinx.android.synthetic.main.fragment_new_expression.view.*
import kotlinx.android.synthetic.main.logo_view.view.*
import timber.log.Timber

class FragNewExpression : BaseMainFragment() {

    private val viewModel by viewModels<NewExpressionViewModel>()

    private lateinit var rootView: View

    private var currentlySelectedChipId = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_new_expression, container, false).also {
        rootView = it
        setupUi()
        observeData()
    }

    private fun setupUi() {
        with (rootView) {
            chipNew.isChecked = true

            // -----------------------------------------------------------------------------
            // The ChipGroup component has no built-in option for "always one selected" case
            currentlySelectedChipId = levelGroup.checkedChipId
            levelGroup.setOnCheckedChangeListener { chipGroup, selectedChipId ->
                if (selectedChipId == -1) { // trying to un-select chip
                    chipGroup.check(currentlySelectedChipId)
                } else {
                    currentlySelectedChipId = selectedChipId
                }
            }
            // -----------------------------------------------------------------------------

            saveBtn.setOnClickListener {
                viewModel.saveExpression(
                    expressionEt.text.toString(),
                    translationEt.text.toString(),
                    getSelectedLevel()
                )
            }
        }
    }

    private fun getSelectedLevel(): Level? {
        with (rootView) {
            return when (levelGroup.checkedChipId) {
                chipNew.id -> Level.NEW
                chipBasic.id -> Level.BASIC
                chipIntermediate.id -> Level.INTERMEDIATE
                chipAdvanced.id -> Level.ADVANCED
                chipKnown.id -> Level.KNOWN
                else -> null
            }
        }
    }

    private fun observeData() {
        viewModel.state.observe(
            viewLifecycleOwner,
            Observer { updateUi(it) }
        )
    }

    private fun updateUi(state: State) {
        when (state) {
            State.Success -> {
                showSuccessStatus()
                clearFields()
            }
            is State.Error -> {
                navigateTo(
                    FragNewExpressionDirections.actionFragNewExpressionToFragError(
                        errorMsg = state.error.localizedMessage
                    )
                )
            }
        }
    }

    private fun showSuccessStatus() {
        (rootView.logoView as LottieAnimationView).apply {
            repeatCount = 0
            setAnimation("success.json")
            playAnimation()
            addAnimatorListener(object : AnimationEndedListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    showLogo()
                }
            })
        }
    }

    private fun showLogo() {
        (rootView.logoView as LottieAnimationView).apply {
            repeatCount = LottieDrawable.INFINITE
            setAnimation("logo.json")
            playAnimation()
            startAnimation(
                AlphaAnimation(0f, 1f).apply {
                    interpolator = DecelerateInterpolator()
                    duration = 800
                }
            )
        }
    }

    private fun clearFields() {
        with (rootView) {
            expressionEt.text?.clear()
            translationEt.text?.clear()
            expressionEt.requestFocus()
        }
    }

    open class AnimationEndedListener: Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {}
    }
}
