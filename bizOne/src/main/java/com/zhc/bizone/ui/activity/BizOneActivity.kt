package com.zhc.bizone.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhc.bizone.R
import com.zhc.bizone.vm.BizOneActivityVm
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.net.Repo
import com.zhc.common.socket.ConnectBean
import com.zhc.common.socket.SocketManager
import com.zhc.common.vm.DataState
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
        observeVm()
    }

    private fun observeVm() {
        viewModel.loadData1.observe(this, Observer {
            when (it.state) {
                DataState.SUCCESS -> {
                    it.data?.let {

                    }
                }
                else -> {

                }
            }
        })
    }

    override fun generateViewModel(): BizOneActivityVm {
        return ViewModelProvider(this).get(BizOneActivityVm::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.getInstance().destroySocket()
    }
}