package com.zhc.bizone.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.zhc.bizone.R
import com.zhc.bizone.ui.activity.dialog.BizOneDialog
import com.zhc.bizone.ui.activity.dialog.BizOneNetAccessDialog
import com.zhc.bizone.ui.adapter.BizOneActivityAdapter
import com.zhc.bizone.vm.BizOneActivityVm
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.aop.TraceDelay
import com.zhc.common.api.Data
import com.zhc.common.api.TestResponse
import com.zhc.common.launchActivity
import com.zhc.common.net.Repo
import com.zhc.common.socket.ConnectBean
import com.zhc.common.socket.SocketManager
import com.zhc.common.vm.DataState
import kotlinx.android.synthetic.main.biz_one_activiy.*

private const val TAG = "zhc BizOneActivity"
@Route(path = Router.Pages.BizOneModule.BIZ_ONE_BIZ_ONE_ACTIVITY)
class BizOneActivity: BaseActivity<BizOneActivityVm>() {


    private var bizOneAdapter: BizOneActivityAdapter? = null
    var data = mutableListOf<Data>()

    override fun getLayoutId(): Int {
        return R.layout.biz_one_activiy
    }

    @TraceDelay()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        bizOneButton.setOnClickListener {
//            SocketManager.getInstance().connect(
//                Repo.getInstance().getOkHttpClient(), ConnectBean()
//            ) {
//                Log.d(TAG, "onCreate: connect error")
//            }

//            viewModel.loadData1()
            viewModel.loadData2()
        }

        showNoNetAccessDialog.setOnClickListener {
            supportFragmentManager.let {
                BizOneDialog.newInstance().apply {
                    registerListener {
                        confirmed {
                            ToastUtils.showLong("confirm")
                            dismiss()
                        }
                        canceled {
                            ToastUtils.showLong("cancel")
                            dismiss()
                        }
                    }
                    show(it)
                }
            }
        }

        showNetAccessDialog.setOnClickListener {
            supportFragmentManager.let {
                BizOneNetAccessDialog.newInstance().apply {
                    registerListener {
                        confirmed {
                            ToastUtils.showLong("confirm")
                            dismiss()
                        }
                        canceled {
                            ToastUtils.showLong("cancel")
                            dismiss()
                        }
                    }
                    show(it)
                }
            }
        }

        showWebViewActivity.setOnClickListener {
            launchActivity(Router.Pages.BizOneModule.BIZ_ONE_WEB_VIEW_ACTIVITY)
        }

        initView()
        observeVm()
    }

    private fun initView() {
        //recyclerView
        bizOneAdapter = BizOneActivityAdapter(data)
        bizOneRecyclerView?.apply {
            adapter = bizOneAdapter
            layoutManager = LinearLayoutManager(this@BizOneActivity)
            setHasFixedSize(true)
        }

        //itemView的点击事件
        bizOneAdapter?.setOnItemClickListener { adapter, view, position ->
            Log.d(TAG, "initView: zhc=== ${view.javaClass.canonicalName}")
        }
        //注册子View点击事件
        bizOneAdapter?.addChildClickViewIds(R.id.bizOneAvator, R.id.bizOneUserName)
        bizOneAdapter?.setOnItemChildClickListener { adapter, view, position ->
            Log.d(TAG, "initView: zhc=== ${view.javaClass.canonicalName}")
        }
    }

    private fun observeVm() {
        viewModel.loadData1.observe(this, Observer { it ->
            Log.d(TAG, "observeVm: zhc===")
            when (it.state) {
                DataState.SUCCESS -> {
                    it.data?.let {
                        updateView(it)
                    }
                }
                DataState.ERROR -> {

                }
                else -> {
                    Toast.makeText(this, "loadData1 Failed", Toast.LENGTH_SHORT)
                }
            }
        })
    }

    private fun updateView(it: TestResponse) {
        //更新View
        bizOneAdapter?.setNewData(it.data)
        ToastUtils.showLong(it.total)

    }

    override fun generateViewModel(): BizOneActivityVm {
        return ViewModelProvider(this).get(BizOneActivityVm::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketManager.getInstance().destroySocket()
    }
}