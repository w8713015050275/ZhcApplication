package com.zhc.bizone.ui.activity

import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.gson.Gson
import com.zhc.bizone.R
import com.zhc.common.BaseActivity
import com.zhc.common.Router
import com.zhc.common.vm.BaseViewModel
import kotlinx.android.synthetic.main.biz_one_web_view_activiy.*


private const val TAG = "WebViewActivity zhc===="
@Route(path = Router.Pages.BizOneModule.BIZ_ONE_PDF_WEB_VIEW_ACTIVITY)
class PdfViewActivity: BaseActivity<BaseViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.biz_one_web_view_activiy
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initWebSettings()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val cookieManager: CookieManager = CookieManager.getInstance()
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        }
//        webView.loadUrl("file:///android_asset/mypdf.html?pdfpath="+"https://du-merchant.oss-cn-shanghai.aliyuncs.com/invoice/stark-invoice-e31cf5150182448890b6a1cf06e9403e?Expires=1616406575&OSSAccessKeyId=LTAI4Fg3D5WTm3s6JZcMEfmp&Signature=zE9Z2CoovHrTziSdCFBo%2BjDEF24%3D")
//        webView.loadUrl(/*"file:///android_asset/mypdf.html?pdfpath="+*/"https://du-merchant.oss-cn-shanghai.aliyuncs.com/invoice/stark-invoice-e31cf5150182448890b6a1cf06e9403e?Expires=1616406575&OSSAccessKeyId=LTAI4Fg3D5WTm3s6JZcMEfmp&Signature=zE9Z2CoovHrTziSdCFBo%2BjDEF24%3D")
        webView.loadUrl("file:///android_asset/mypdf.html?pdfpath="+"https://codecrux.com/codecrux-portfolio.pdf")

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

    private fun initWebSettings() {
        //去掉横向滚动条
        webView.setHorizontalScrollBarEnabled(false)
        //去掉纵向滚动条
        webView.setVerticalScrollBarEnabled(false)

        val webSettings = webView!!.settings ?: return
        //设置字体缩放倍数，默认100
        webSettings.textZoom = 100
        // 支持 Js 使用
        webSettings.javaScriptEnabled = true
        // 开启DOM缓存
        webSettings.domStorageEnabled = true
        // 开启数据库缓存
        webSettings.databaseEnabled = true
        // 支持启用缓存模式
        webSettings.setAppCacheEnabled(true)
        // 设置 AppCache 最大缓存值(现在官方已经不提倡使用，已废弃)
        webSettings.setAppCacheMaxSize((8 * 1024 * 1024).toLong())
        // Android 私有缓存存储，如果你不调用setAppCachePath方法，WebView将不会产生这个目录
        webSettings.setAppCachePath(cacheDir.absolutePath)
        // 关闭密码保存提醒功能
        webSettings.savePassword = false
        // 支持缩放
        webSettings.setSupportZoom(true)
        //设置内置的缩放控件
        webSettings.setBuiltInZoomControls(true)
        //不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);
        // 设置 UserAgent 属性
        webSettings.userAgentString = ""
        // 允许加载本地 html 文件/false
        webSettings.allowFileAccess = true
        webSettings.allowUniversalAccessFromFileURLs = true

        webView.setWebViewClient(object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                webView.loadUrl(/*"file:///android_asset/mypdf.html?pdfpath="+*/"https://du-merchant.oss-cn-shanghai.aliyuncs.com/invoice/stark-invoice-e31cf5150182448890b6a1cf06e9403e?Expires=1616406575&OSSAccessKeyId=LTAI4Fg3D5WTm3s6JZcMEfmp&Signature=zE9Z2CoovHrTziSdCFBo%2BjDEF24%3D")
//
//                return false
//            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
//                super.onReceivedSslError(view, handler, error)
                //handler.cancel(); //默认的处理方式，WebView变成空白页
                handler.proceed();//接受证书

//handleMessage(Message msg); 其他处理
            }


//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//
////handler.cancel(); 默认的处理方式，WebView变成空白页
//                handler.proceed();接受证书
//
////handleMessage(Message msg); 其他处理
//            }
//
//// 这行代码一定加上否则效果不会出现
//
//        }

        })
//        webView.getSettings().setJavaScriptEnabled(true);
    }
}