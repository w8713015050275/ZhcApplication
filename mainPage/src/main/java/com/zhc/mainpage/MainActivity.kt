package com.zhc.mainpage

import android.os.Bundle
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.launchActivity
import com.zhc.common.vm.BaseViewModel
import kotlinx.android.synthetic.main.main_page_activity.*

class MainActivity: BaseActivity<BaseViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.main_page_activity
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        jumpToBizOne.setOnClickListener {
            launchActivity(Router.Pages.BIZ_ONE_BIZ_ONE_ACTIVITY)
        }
    }
}