package com.zhc.loginmodule.ui

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.vm.BaseViewModel
import com.zhc.loginmodule.R

@Route(path = Router.Pages.LoginModule.ACTIVITY_LOGIN_NEW)
class LoginActivity : BaseActivity<BaseViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.login_activity_layout
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {

    }

}