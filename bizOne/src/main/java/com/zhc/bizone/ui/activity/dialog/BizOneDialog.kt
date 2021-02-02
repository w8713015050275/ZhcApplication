package com.zhc.bizone.ui.activity.dialog

import android.os.Bundle
import android.view.View
import com.zhc.bizone.R
import com.zhc.common.ui.NoNetAccessFragmentDialog
import kotlinx.android.synthetic.main.biz_one_dialog_layout.*

class BizOneDialog: NoNetAccessFragmentDialog() {

    companion object {
        fun newInstance(): BizOneDialog {
            return BizOneDialog()
        }
    }
    
    override fun getLayoutId(): Int {
        return R.layout.biz_one_dialog_layout
    }

    override fun onViewCreatedFinish(view: View, savedInstanceState: Bundle?) {
        bizOneConfirm.setOnClickListener {
            invokeConfirmed()
        }

        bizOneCancel.setOnClickListener {
            invokeCanceled()
        }
    }
}