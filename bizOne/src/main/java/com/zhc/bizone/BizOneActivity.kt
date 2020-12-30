package com.zhc.bizone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhc.bizone.vm.BizOneActivityVm
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.di.component.DaggerRepoComponent
import com.zhc.common.di.component.RepoComponent
import com.zhc.common.di.module.ApiModule
import com.zhc.common.di.module.NetModule
import com.zhc.common.di.module.RepoModule
import com.zhc.common.net.Repo
import com.zhc.common.socket.ConnectBean
import com.zhc.common.socket.SocketManager
import kotlinx.android.synthetic.main.biz_one_activiy.*

private const val TAG = "zhc BizOneActivity"
@Route(path = Router.Pages.BIZ_ONE_BIZ_ONE_ACTIVITY)
class BizOneActivity: BaseActivity<BizOneActivityVm>() {

    override fun getLayoutId(): Int {
        return R.layout.biz_one_activiy
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        bizOneButton.setOnClickListener {
            SocketManager.getInstance().connect(
                Repo.getInstance().getOkHttpClient(), ConnectBean()
            ) {
                Log.d(TAG, "onCreate: connect error")
            }
        }
    }

    override fun generateViewModel(): BizOneActivityVm {
        return ViewModelProvider(this).get(BizOneActivityVm::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.getInstance().destroySocket()
    }
}