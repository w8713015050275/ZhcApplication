package com.zhc.mainpage

import android.os.Bundle
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.aop.TraceDelay
import com.zhc.common.launchActivity
import com.zhc.common.vm.BaseViewModel
import kotlinx.android.synthetic.main.main_page_activity.*

@TraceDelay
class MainActivity: BaseActivity<BaseViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.main_page_activity
    }

    @TraceDelay
    override fun onViewCreated(savedInstanceState: Bundle?) {
        jumpToBizOne.setOnClickListener {
            launchActivity(Router.Pages.BizOneModule.BIZ_ONE_BIZ_ONE_ACTIVITY)
        }
    }
}