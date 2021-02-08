package com.zhc.common.webView

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView
import android.webkit.WebViewClient
import com.github.lzyzsd.jsbridge.*


class CustomWebView : WebView, WebViewJavascriptBridge, IWebView {
    private var bridgeHelper: BridgeHelper? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        this.setVerticalScrollBarEnabled(false)
        this.setHorizontalScrollBarEnabled(false)
        this.getSettings().setJavaScriptEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        bridgeHelper = BridgeHelper(this)
        this.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(webView: WebView?, s: String?) {
                bridgeHelper!!.onPageFinished()
            }

            override fun shouldOverrideUrlLoading(webView: WebView?, s: String?): Boolean {
                return bridgeHelper!!.shouldOverrideUrlLoading(s)
            }
        })
    }

    /**
     * @param handler default handler,handle messages send by js without assigned handler name,
     * if js message has handler name, it will be handled by named handlers registered by native
     */
    fun setDefaultHandler(handler: BridgeHandler?) {
        bridgeHelper!!.setDefaultHandler(handler)
    }

    override fun send(data: String?) {
        send(data, null)

    }

    override fun send(data: String?, responseCallback: CallBackFunction?) {
        bridgeHelper!!.send(data, responseCallback)
    }

    /**
     * register handler,so that javascript can call it
     * 注册处理程序,以便javascript调用它
     *
     * @param handlerName handlerName
     * @param handler     BridgeHandler
     */
    fun registerHandler(handlerName: String?, handler: BridgeHandler?) {
        bridgeHelper!!.registerHandler(handlerName, handler)
    }

    /**
     * unregister handler
     *
     * @param handlerName
     */
    fun unregisterHandler(handlerName: String?) {
        bridgeHelper!!.unregisterHandler(handlerName)
    }

    /**
     * call javascript registered handler
     * 调用javascript处理程序注册
     *
     * @param handlerName handlerName
     * @param data        data
     * @param callBack    CallBackFunction
     */
    fun callHandler(
        handlerName: String?,
        data: String?,
        callBack: CallBackFunction?
    ) {
        bridgeHelper!!.callHandler(handlerName, data, callBack)
    }
}