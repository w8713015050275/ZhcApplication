package com.zhc.bizone

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import kotlinx.android.synthetic.main.biz_one_activiy.*

private const val TAG = "zhc BizOneActivity"
@Route(path = Router.Pages.BIZ_ONE_BIZ_ONE_ACTIVITY)
class BizOneActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: BizOneActivity")
        setContentView(R.layout.biz_one_activiy)
        bizOneButton.setOnClickListener {
            Log.d(TAG, "onCreate: I AM $TAG")
        }
    }
}