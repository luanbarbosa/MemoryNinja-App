package com.luanbarbosagomes.hmr.feature.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.luanbarbosagomes.hmr.R
import com.luanbarbosagomes.hmr.feature.add.NewExpressionStatus.FAILED
import com.luanbarbosagomes.hmr.feature.add.NewExpressionStatus.SAVED
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_new_expression.*

class ActivityNewExpression : AppCompatActivity() {

    private val model by viewModels<NewExpressionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_expression)

        model
            .status
            .observeForever { status ->
                when (status) {
                    SAVED -> {
                        Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show()
                        clearFields()
                    }
                    FAILED -> {
                        Toast.makeText(this, "Something went wrong :(", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                    }
                }
            }

        saveBtn.setOnClickListener {
            model.saveExpression(
                expressionEt.text.toString(),
                translationEt.text.toString()
            )
        }
    }

    private fun clearFields() {
        expressionEt.text.clear()
        translationEt.text.clear()
    }
}
