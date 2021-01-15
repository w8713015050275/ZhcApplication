package com.zhc.common.imageLoader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import java.io.File

interface BaseImageLoaderStrategy {
    fun loadImage(
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    )

    fun loadImage(
        url: String?,
        requestOptions: RequestOptions?,
        imageView: ImageView
    )

    fun loadImage(
        url: String?,
        placeholderId: Int,
        weigth: Int,
        height: Int,
        imageView: ImageView
    )

    fun loadImage(
        context: Context?,
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    )

    //无占位图
    fun loadImage(url: String?, imageView: ImageView)
    fun loadFileImage(file: File?, imageView: ImageView)
    fun loadResImage(resId: Int, imageView: ImageView)
    fun loadCircleImage(
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    )

    fun loadCircleImage(url: String?, imageView: ImageView)
    fun loadCircleImageWithBorder(url: String?, imageView: ImageView)
    fun loadCircleImage(resId: Int, imageView: ImageView)
    fun loadRoundImage(
        url: String?,
        placeholderId: Int,
        radius: Int,
        imageView: ImageView
    )

    fun loadRoundImageWithoutAnim(
        url: String?,
        placeholderId: Int,
        radius: Int,
        imageView: ImageView
    )

    fun loadRoundImage(
        url: String?,
        radius: Int,
        imageView: ImageView
    )

    fun loadRoundImage(
        resId: Int,
        radius: Int,
        imageView: ImageView
    )

    fun loadGifImage(
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    )

    fun loadGifImage(url: String?, imageView: ImageView)
    fun loadGifImage(resId: Int, imageView: ImageView)
    fun loadGifImage(resId: Int, imageView: ImageView, count: Int)

    //清除硬盘缓存
    fun clearImageDiskCache(context: Context)

    //清除内存缓存
    fun clearImageMemoryCache(context: Context)

    //根据不同的内存状态，来响应不同的内存释放策略
    fun trimMemory(context: Context?, level: Int)
}