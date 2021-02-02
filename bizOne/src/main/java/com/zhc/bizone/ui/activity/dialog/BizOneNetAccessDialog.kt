package com.zhc.bizone.ui.activity.dialog

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.blankj.utilcode.util.ToastUtils
import com.zhc.bizone.R
import com.zhc.bizone.vm.BizOneActivityVm
import com.zhc.common.api.TestResponse
import com.zhc.common.imageLoader.ImageLoader
import com.zhc.common.ui.BaseDialogFragment
import com.zhc.common.vm.DataState
import kotlinx.android.synthetic.main.biz_one_dialog_layout.*

class BizOneNetAccessDialog: BaseDialogFragment<BizOneActivityVm, TestResponse>() {

    private var data: TestResponse? = null

    companion object {
        fun newInstance(): BizOneNetAccessDialog {
            return BizOneNetAccessDialog()
        }
    }
    
    override fun getLayoutId(): Int {
        return R.layout.biz_one_dialog_layout
    }

    override fun onViewCreatedFinish(view: View, savedInstanceState: Bundle?) {
        bizOneConfirm.setOnClickListener {
            invokeConfirmed(data!!)
        }

        bizOneCancel.setOnClickListener {
            invokeCanceled()
        }
        bizOneConfirm.isClickable = false

        viewModel.loadData2()

        viewModel.loadData1.observe(this, Observer {
            ToastUtils.showLong("${it.data?.total}")
            if (it.state == DataState.SUCCESS) {
                bizOneConfirm.isClickable = true
                data = it.data
                ImageLoader.loadImage(it.data!!.data[0].avatar, contentIV)
            } else {
                bizOneConfirm.isClickable = false
            }
        })
    }

    override fun generateViewModel(): BizOneActivityVm {
        return ViewModelProvider(this).get(BizOneActivityVm::class.java)
    }
}