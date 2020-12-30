package com.zhc.common.socket

import java.io.Serializable

/**
 * socket.io与服务器获取连接。 全部参数必须输入
 */
data class ConnectBean(
    var name: String? = "",
    var mobile: String? = "",
    var gender: String? = "",
    var token: String? = "",
    var lessonId: String? = "",
    var lessonUid: String? = "",
    var lessonType: String? = "",
    var role: String? = "",
    var lastIndex: String? = "",
    var messageIndex: String? = "",
    var connUrl: String? = "",
    var clientVersion: String? = "",
    var channel: String? = "",
    var duration: Int? = 0,
    var muteType: String? = "",
    var umengChannel: String? = "",
    var canScreenShot: Boolean? = false,
    var deveiceInfo: String? = "",
    var watchMobile: String? = "",
    var userId: Long? = -1,
    var modeType: String? = "",
    var recordId: String? = "",
    var bu: String? = "",
    var abilityVersion: String? = "1.1",
    var subject: String? = "",
    var contentId:String?="",
    var linkId: Int? = null,
    var teacherId: Int? = null
) : Serializable

object ConnectBeanBuild {
    fun build(
        clientVersion: String?,
        url: String,
        screenSize: String,
        resolution: String,
        buType: String?
    ): ConnectBean {
        return ConnectBean(
            "genius-review",
            "student",
            "-1",
            "-1",
            url,
            clientVersion,
            "agora",
            "unmute",
            "zhangmen",
            "Android_kp:1.0" + "; screen:" + screenSize + "英寸;pixel:" + resolution,
            "",
            buType,
            "1.1"
        )
    }
}