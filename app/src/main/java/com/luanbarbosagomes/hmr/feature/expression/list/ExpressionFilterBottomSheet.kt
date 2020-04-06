package com.luanbarbosagomes.hmr.feature.expression.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.luanbarbosagomes.hmr.R

class ExpressionFilterBottomSheet : BottomSheetDialogFragment() {

    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_expression_filter, container, false).also {
        rootView = it
    }
}