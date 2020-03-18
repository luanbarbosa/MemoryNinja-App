package com.luanbarbosagomes.hmr.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.luanbarbosagomes.hmr.R
import kotlinx.android.synthetic.main.fragment_error.view.*

class FragError: BaseMainFragment() {

    private lateinit var rootView: View

    private val args by navArgs<FragErrorArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_error, container, false).also { rootView = it }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView.errorDescription.text = args.errorMsg ?: ""
    }
}