package com.zhc.mainpage

import android.os.Bundle
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.launchActivity
import kotlinx.android.synthetic.main.main_page_activity.*

class MainActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page_activity)


        jumpToBizOne.setOnClickListener {
            launchActivity(Router.Pages.BIZ_ONE_BIZ_ONE_ACTIVITY)
        }
    }
}