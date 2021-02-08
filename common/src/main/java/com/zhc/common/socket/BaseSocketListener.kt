package com.zhc.common.socket

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.zhc.common.executors.runInMain
import com.zhc.common.extensions.gson
import com.zhc.common.extensions.isFalse
import com.zhc.common.extensions.isTrue
import com.zhc.common.extensions.toJsonObject
import com.zhc.common.utils.ReflectUtils
import io.socket.client.Ack
import io.socket.emitter.Emitter
import org.json.JSONObject
import java.lang.reflect.ParameterizedType

/**
 *  Created by yetao on 2019-10-18
 *  description
 **/

abstract class BaseSocketListener<RESPONSE, REQUEST>(val signal: String) : Emitter.Listener {
    companion object {
        const val TAG = "SocketListener"
        private val CLZ_MAP = mutableMapOf<Class<*>, Class<*>>()

        @JvmStatic
        fun getDataClass(clz: Class<*>): Class<*>? {
            return CLZ_MAP[clz] ?: ReflectUtils.getTypeClass(clz, Any::class.java)?.apply {
                CLZ_MAP[clz] = this
            }
        }
    }

    private val socketManager = SocketManager.getInstance()
    var listeners = mutableListOf<((RESPONSE?) -> Unit)?>()
    private var doBefore: Boolean = true
    private var doAfter: Boolean = true
    var ignoreSignalMap:HashMap<String,Boolean> = HashMap<String,Boolean>()
    private fun listenOn() {
        socketManager.on(signal, this)
    }

    /*初始化代码块*/
    init {
        ignoreSignalMap.put("zmg", true)
        ignoreSignalMap.put("play time record",true)
        ignoreSignalMap.put("whiteboard current data", true)
        ignoreSignalMap.put("get zmg",true)
    }
    fun isAiLesson() = true

    @JvmOverloads
    fun observe(doBefore: Boolean = true, doAfter: Boolean = true, listener: (RESPONSE?) -> Unit) {
        this.doBefore = doBefore
        this.doAfter = doAfter
        listeners.add(listener)
        listenOn()
    }

    fun sendData(req: REQUEST? = null, lessonUid: String? = SocketManager.getInstance().connectBean?.lessonUid, ack: ((Array<Any?>?) -> Unit)? = null) {
        SocketManager.getInstance().apply {
            if (lessonUid == connectBean?.lessonUid) {
                this.sendData(signal, if (req is JSONObject) req else req.toJsonObject(), Ack {
//                    "send <<$signal>> (${if (req is JSONObject) req else req.toJsonObject()}) to socket".interactClient2SocketLog()
                    ack?.invoke(it)
                    //影响流程的信令上报数据，方便定位问题
                   /* if (Constants.SocketSignal.STEP_END.equals(signal) || Constants.SocketSignal.SWITCH_PAGE.equals(signal)
                        ||Constants.SocketSignal.STEP_CHANGE.equals(signal)){
                        FullLinkHelper.onEventBringLessonUid(
                            FullLinkConst.AI_SOCKET_SEND,
                            hashMapOf("signal" to signal,"data" to "${req.toJsonObject()}")
                        )
                    }else if (!ignoreSignalMap.containsKey(signal)) {//过滤掉不需要的信令日志上报
                        FullLinkHelper.onEventBringLessonUid(
                            FullLinkConst.AI_SOCKET_SEND,
                            hashMapOf("signal" to signal)
                        )
                    }*/

                })
            } else {
//                Logger.e("sendData <<$signal>> to socket failed, lessonUid not equal $lessonUid")
                /*FullLinkHelper.onEventBringLessonUid(FullLinkConst.AI_SOCKET_SEND_ERROR,
                    hashMapOf("signal" to signal))*/
            }
        }
    }


    fun sendMultiData(vararg req: Any?, lessonUid: String? = SocketManager.getInstance().connectBean?.lessonUid, ack: ((Array<Any?>?) -> Unit)? = null) {
        SocketManager.getInstance().apply {
            if (lessonUid == connectBean?.lessonUid) {
                this.sendData(signal, req, Ack {
                    ack?.invoke(it)
                })
            }  else {
//                Logger.e("sendMultiData <<$signal>> to socket failed, lessonUid not equal $lessonUid")
            }
        }
    }

    override fun call(vararg args: Any?) {
        args.apply {
            try {
                runInMain {
                    var data = parseData(*args)
                    doBefore isTrue {

                        doBeforeAction(listeners,*args) isTrue {

                            doAfter isTrue {
                                doAfterAction(data)
                            }
                            for(listener in listeners){
                                listener?.invoke(data)
                            }
                        }
                    } isFalse {
//                        var data = parseData(*args)
                        doAfter isTrue {
                            doAfterAction(data)
                        }
                        for(listener in listeners){
                            listener?.invoke(data)
                        }
                    }
//                    "signal: <<$signal>> data=${data}".interactSocket2ClientLog()
         /*           if (Constants.SocketSignal.STEP_END.equals(signal) || Constants.AiClassSignal.PLAY_RESOURCE.equals(signal)
                        ||Constants.AiClassSignal.PLAY_START.equals(signal)){
                        FullLinkHelper.onEventBringLessonUid(
                            FullLinkConst.AI_SOCKET_RECEIVE,
                            hashMapOf("signal" to signal,"data" to "${data}")
                        )

                    }else if (!ignoreSignalMap.containsKey(signal)){//过滤不必要的信令日志上报
                        FullLinkHelper.onEventBringLessonUid(
                            FullLinkConst.AI_SOCKET_RECEIVE,
                            hashMapOf("signal" to signal)
                        )
                    }*/
                }
            } catch (e: Exception) {
//                "signal: <<$signal>>  error:${e.message}".interactSocket2ClientLog()
                /*FullLinkHelper.onEventBringLessonUid(FullLinkConst.AI_SOCKET_RECEIVE_ERROR,
                    hashMapOf("signal" to signal, "error" to "${e.message}"))*/
            }
            return
        }
    }

    private fun parseData(vararg args: Any?): RESPONSE? {

        if (args.isNotEmpty()) {
            val json = args[0].toString()
            val clz =
                getDataClass(javaClass)
            return when (clz) {
                String::class.java -> {
                    json
                }
                Int::class.java -> {
                    json.toIntOrNull() ?: 0
                }
                Throwable::class.java -> {
                    /**
                     * [ZmConnectError]
                     */
                    args[0]
                }
                else -> {
                    var data: RESPONSE? = null
                    try {
                        val dataType = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
                        data = gson.getAdapter(TypeToken.get(dataType)).fromJson(json) as? RESPONSE
                    } catch (e: Exception) {
//                        SocketLog.onLog(signal, "fromJson error: $clz $json")
                        e.printStackTrace()
                    }
                    data
                }
            } as? RESPONSE
        }
        return null
    }

    /**
     * 解析数据前
     */
    open fun doBeforeAction(listeners: MutableList<((RESPONSE?) -> Unit)?>, vararg args: Any?) = true

    /**
     * 解析数据后
     */
    open fun doAfterAction(resp: RESPONSE?){}

    fun onDestroy(){
        listeners.clear()
    }
}

open class SocketListener<RESPONSE>(signal: String) : BaseSocketListener<RESPONSE, Any>(signal)