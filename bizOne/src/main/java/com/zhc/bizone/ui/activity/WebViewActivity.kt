package com.zhc.bizone.ui.activity

import android.os.Bundle
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.zhc.bizone.R
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.vm.BaseViewModel
import kotlinx.android.synthetic.main.biz_one_web_view_activiy.*

private const val TAG = "WebViewActivity zhc===="
@Route(path = Router.Pages.BizOneModule.BIZ_ONE_WEB_VIEW_ACTIVITY)
class WebViewActivity: BaseActivity<BaseViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.biz_one_web_view_activiy
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        webView.loadUrl("file:///android_asset/demo.html")

        //注册监听h5消息的handler
        webView.registerHandler("submitFromWeb") { data, function ->
            Log.d(TAG, "handler = submitFromWeb, data from web = $data")
            function.onCallBack("submitFromWeb exe, response data 中文 from Java")
        }

        val user = User()
        val location: Location = Location()
        location.address = "SDU"
        user.location = location
        user.name = "大头鬼"

        callH5Function.setOnClickListener {
            //调用h5方法,并监听返回
            webView.callHandler("functionInJs", Gson().toJson(user)) {
                Log.d(TAG, "onViewCreated: java call h5 function return msg: $it ")
            }
        }
    }

    internal class User {
        var name: String? = null
        var location: Location? = null
        var testStr: String? = null
    }

    internal class Location {
        var address: String? = null
    }
}