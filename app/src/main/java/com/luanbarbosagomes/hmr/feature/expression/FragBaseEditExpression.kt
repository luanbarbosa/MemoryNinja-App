package com.luanbarbosagomes.hmr.feature.expression

import android.animation.Animator
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.luanbarbosagomes.hmr.data.Level
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.utils.hide
import com.luanbarbosagomes.hmr.utils.hideKeyboard
import com.luanbarbosagomes.hmr.utils.show
import kotlinx.android.synthetic.main.full_screen_success_indicator.*
import kotlinx.android.synthetic.main.full_screen_success_indicator.view.*

@Suppress("PropertyName")
abstract class FragBaseEditExpression : BaseMainFragment() {

    internal lateinit var rootView: View

    private var currentlySelectedChipId = -1

    lateinit var _chipNew: Chip
    lateinit var _chipBasic: Chip
    lateinit var _chipIntermediate: Chip
    lateinit var _chipAdvanced: Chip
    lateinit var _chipKnown: Chip
    lateinit var _levelGroup: ChipGroup
    lateinit var _saveBtn: Button
    lateinit var _expressionEt: EditText
    lateinit var _translationEt: EditText

    abstract fun save()

    abstract fun afterSuccessfulSave()

    internal open fun setupUi() {
        _levelGroup.check(_chipNew.id)

        // -----------------------------------------------------------------------------
        // The ChipGroup component has no built-in option for "always one selected" case
        currentlySelectedChipId = _levelGroup.checkedChipId
        _levelGroup.setOnCheckedChangeListener { chipGroup, selectedChipId ->
            if (selectedChipId == -1) { // trying to un-select chip
                chipGroup.check(currentlySelectedChipId)
            } else {
                currentlySelectedChipId = selectedChipId
            }
        }
        // -----------------------------------------------------------------------------

        _saveBtn.setOnClickListener {
            save()
        }
    }

    internal fun getSelectedLevel(): Level? =
        when (_levelGroup.checkedChipId) {
            _chipNew.id -> Level.NEW
            _chipBasic.id -> Level.BASIC
            _chipIntermediate.id -> Level.INTERMEDIATE
            _chipAdvanced.id -> Level.ADVANCED
            _chipKnown.id -> Level.KNOWN
            else -> null
        }

    internal fun showSuccessStatus() {
        rootView.let {
            successIndicatorContainer.show()
            successIndicatorAnimView.apply {
                addAnimatorListener(object : AnimationEndedListener() {
                    override fun onAnimationCancel(animation: Animator?) {
                        afterSuccessfulSave()
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        afterSuccessfulSave()
                    }
                })
                playAnimation()
            }
        }
    }

    internal fun clearFields() {
        _expressionEt.text?.clear()
        _translationEt.text?.clear()
        _expressionEt.requestFocus()
        context?.hideKeyboard(_translationEt)
    }

    internal fun hideSuccessfulIndicator() {
        rootView.successIndicatorContainer.hide()
    }

    internal fun selectLevel(level: Level) {
        when (level) {
            Level.NEW -> _levelGroup.check(_chipNew.id)
            Level.BASIC -> _levelGroup.check(_chipBasic.id)
            Level.INTERMEDIATE -> _levelGroup.check(_chipIntermediate.id)
            Level.ADVANCED -> _levelGroup.check(_chipAdvanced.id)
            Level.KNOWN -> _levelGroup.check(_chipKnown.id)
        }
    }

    internal open class AnimationEndedListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {}
    }
}