package com.qi.market.module

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.qi.market.R
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        showError.setOnClickListener {
            dataContainerView.showErrorView()
        }
        showProgress.setOnClickListener {
            dataContainerView.showProgressBar()
        }
        showData.setOnClickListener {
            dataContainerView.showDataView()
        }
    }


}
