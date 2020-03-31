package com.luanbarbosagomes.hmr.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.data.repository.QuizRepository
import com.luanbarbosagomes.hmr.feature.BaseMainFragment
import com.luanbarbosagomes.hmr.feature.expression.list.ExpressionsViewModel
import com.luanbarbosagomes.hmr.feature.preference.PreferenceViewModel
import com.luanbarbosagomes.hmr.utils.toastIt
import com.luanbarbosagomes.hmr.work.QuizWorker
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class FragMain : BaseMainFragment() {

    private val preferenceViewModel by viewModels<PreferenceViewModel>()

    private val expressionsViewModel by viewModels<ExpressionsViewModel>()

    private val notOnStackOption = NavOptions.Builder()
        .setPopUpTo(R.id.fragMain, true)
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false).also { setupUi(it) }

    private fun setupUi(rootView: View) {
        with(rootView) {
            addBtn.setOnClickListener {
                navigateTo(FragMainDirections.actionFragMainToFragNewExpression())
            }
            listBtn.setOnClickListener {
                navigateTo(FragMainDirections.actionFragMainToFragListExpressions())
            }
            logoutBtn.setOnClickListener {
                preferenceViewModel.logout()
                navigateTo(
                    FragMainDirections.actionFragMainToFragSplash(),
                    navOptions = notOnStackOption
                )
            }
            randomBtn.setOnClickListener {
                lifecycleScope.launch {
                    val exp = QuizRepository(expressionsViewModel.expressionRepository).nextQuiz()

                    WorkManager
                        .getInstance(context)
                        .enqueue(
                            OneTimeWorkRequestBuilder<QuizWorker>()
                                .setInputData(
                                    workDataOf(
                                        QuizWorker.CorrectAnswer to false,
                                        QuizWorker.ExpressionId to exp?.uid
                                    )
                                )
                                .build()
                        )


                    "${exp ?: "NOT FOUND!"}".toastIt(short = true)
                }
            }

            // TODO - temporary code ----------------------------
            clearDbBtn.setOnClickListener { expressionsViewModel.deleteAll() }
            // TODO - temporary code ----------------------------
        }
    }

}
