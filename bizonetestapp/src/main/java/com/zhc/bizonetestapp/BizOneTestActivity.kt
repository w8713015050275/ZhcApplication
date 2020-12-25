package com.zhc.bizonetestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhc.common.Router
import com.zhc.common.launchActivity
import kotlinx.android.synthetic.main.activity_biz_one_test.*

class BizOneTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biz_one_test)
        helloBizOneTestActivity.setOnClickListener {
            launchActivity(Router.Pages.BIZ_ONE_BIZ_ONE_ACTIVITY)
        }
    }
}