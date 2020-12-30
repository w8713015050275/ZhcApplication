package com.zhc.common.socket

import android.text.TextUtils
import android.util.Log
//import com.zhangmen.aiclassroom.manager.AiClassManager
//import com.zhangmen.aiclassroom.utils.Logger
//import com.zhangmen.media.ZMMediaEngine
import io.socket.client.Socket
import io.socket.emitter.Emitter
import okhttp3.OkHttpClient
import java.util.concurrent.ConcurrentHashMap

/**
 *  Created by yetao on 2019-10-18
 *  description
 **/


private const val TAG = "zhc SocketManager"
class SocketManager private constructor() {

    companion object {
        @Volatile
        private var instance: SocketManager? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: SocketManager().apply {
                instance = this
            }
        }
    }

    var webViewInfo: String? = null
    @Volatile
    private var allListeners: MutableMap<String, Emitter.Listener> = ConcurrentHashMap()
    var socket: Socket? = null
    var connectBean: ConnectBean? = null
    private val mLock = Any()

    fun init() {

    }

    fun connect(okHttpClient: OkHttpClient, connectBean: ConnectBean, errorAction: (String?) -> Unit) {
        this.connectBean = connectBean
        this.connectBean!!.connUrl = "ws://121.40.165.18:8800"
        if (TextUtils.isEmpty(connectBean.connUrl)) {
            errorAction.invoke("url is null")
            return
        }

        if (socket != null) {
            socket?.disconnect()
        } else {
            socket = SocketConnect.connect(okHttpClient, connectBean, errorAction)
        }


        if (socket == null) {
            errorAction.invoke("socket is null")
            return
        }

        synchronized(mLock){
            for ((key, fn) in allListeners) {
                on(key, fn)
            }
        }

        socket?.on(Socket.EVENT_CONNECT) { Log.d(TAG, "call: zhc socket EVENT_CONNECT") }
        socket?.on(Socket.EVENT_CONNECT_ERROR) { Log.d(TAG, "call: zhc socket EVENT_CONNECT_ERROR") }
        socket?.on(Socket.EVENT_CONNECT_TIMEOUT) { Log.d(TAG, "call: zhc socket EVENT_CONNECT_TIMEOUT") }

        socket?.connect()
    }

    fun sendData(event: String, vararg args: Any?) {
        socket?.emit(event, *args)
    }

    fun on(event: String, fn: Emitter.Listener): SocketManager {
        synchronized(mLock) {
            if (socket == null) {
                allListeners[event] = fn
            } else if (!socket!!.listeners(event).contains(fn)) {
                allListeners[event] = fn
                socket?.on(event, fn)
            }
        }
        return this
    }

    fun destroySocket() {
        for ((_, value) in allListeners) {
            if (value is BaseSocketListener<*, *>)
                value.onDestroy()
        }

        allListeners.clear()
        closeSocket()
    }

    fun closeSocket() {
//        Logger.d("base111 socketManager=$this")
        socket?.apply {
            this.off()
            this.disconnect()
//            ZMMediaEngine.getInstance().socketOfDestroy()      //销毁信令记录
            this.close()
            socket = null
        }
    }
}