package com.zhc.common.imageLoader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.request.RequestOptions
import java.io.File

//
object ImageLoader {

    private var mStrategy: BaseImageLoaderStrategy

    init {
        mStrategy = GlideImageLoaderStrategy
    }

    fun loadImage(
        url: String?,
        placeholder: Int,
        imageView: ImageView
    ) {
        mStrategy.loadImage(url, placeholder, imageView)
    }

    fun loadImage(
        url: String?,
        requestOptions: RequestOptions?,
        imageView: ImageView
    ) {
        mStrategy.loadImage(url, requestOptions, imageView)
    }

    fun loadImage(
        context: Context?,
        url: String?,
        placeholder: Int,
        imageView: ImageView
    ) {
        mStrategy.loadImage(context, url, placeholder, imageView)
    }

    fun loadImage(
        url: String?,
        placeholder: Int,
        width: Int,
        height: Int,
        imageView: ImageView
    ) {
        mStrategy.loadImage(url, placeholder, width, height, imageView)
    }

    fun loadImage(url: String?, imageView: ImageView) {
        mStrategy.loadImage(url, imageView)
    }

    fun loadFileImage(file: File?, imageView: ImageView) {
        mStrategy.loadFileImage(file, imageView)
    }

    fun loadResImage(resId: Int, imageView: ImageView) {
        mStrategy.loadResImage(resId, imageView)
    }

    fun loadGifImage(
        url: String?,
        placeholder: Int,
        imageView: ImageView
    ) {
        mStrategy.loadGifImage(url, placeholder, imageView)
    }

    fun loadGifImage(resId: Int, imageView: ImageView) {
        mStrategy.loadGifImage(resId, imageView)
    }

    fun loadGifImage(
        resId: Int,
        imageView: ImageView,
        count: Int
    ) {
        mStrategy.loadGifImage(resId, imageView, count)
    }

    fun loadGifImage(url: String?, imageView: ImageView) {
        mStrategy.loadGifImage(url, imageView)
    }

    fun loadCircleImage(
        url: String?,
        placeholder: Int,
        imageView: ImageView
    ) {
        mStrategy.loadCircleImage(url, placeholder, imageView)
    }

    fun loadCircleImage(
        url: String?,
        imageView: ImageView
    ) {
        imageView.background = null
        imageView.setImageDrawable(null)
        imageView.setImageBitmap(null)
        mStrategy.loadCircleImage(url, imageView)
    }
    fun loadCircleImageWithBorder(
        url: String?,
        imageView: ImageView
    ) {
        imageView.background = null
        imageView.setImageDrawable(null)
        imageView.setImageBitmap(null)
        mStrategy.loadCircleImageWithBorder(url, imageView)
    }

    fun loadCircleImage(resId: Int, imageView: ImageView) {
        imageView.background = null
        imageView.setImageDrawable(null)
        imageView.setImageBitmap(null)
        mStrategy.loadCircleImage(resId, imageView)
    }

    fun loadRoundImageWithoutAnim(
        url: String?,
        placeholder: Int,
        radius: Int,
        imageView: ImageView
    ) {
        mStrategy.loadRoundImageWithoutAnim(url, placeholder, radius, imageView)
    }

    fun loadRoundImage(
        url: String?,
        placeholder: Int,
        radius: Int,
        imageView: ImageView
    ) {
        mStrategy.loadRoundImage(url, placeholder, radius, imageView)
    }

    fun loadRoundImage(
        url: String?,
        radius: Int,
        imageView: ImageView
    ) {
        imageView.background = null
        imageView.setImageDrawable(null)
        imageView.setImageBitmap(null)
        mStrategy.loadRoundImage(url, radius, imageView)
    }

    fun loadRoundImage(
        resId: Int,
        radius: Int,
        imageView: ImageView
    ) {
        imageView.background = null //这里清除之前的缓存
        imageView.setImageDrawable(null)
        imageView.setImageBitmap(null)
        mStrategy.loadRoundImage(resId, radius, imageView)
    }

    /**
     * 策略模式的注入操作
     *
     * @param strategy
     */
    fun setLoadImgStrategy(strategy: BaseImageLoaderStrategy) {
        mStrategy = strategy
    }

    /**
     * 清除图片磁盘缓存
     */
    fun clearImageDiskCache(context: Context) {
        mStrategy.clearImageDiskCache(context)
    }

    /**
     * 清除图片内存缓存
     */
    fun clearImageMemoryCache(context: Context) {
        mStrategy.clearImageMemoryCache(context)
    }

    /**
     * 根据不同的内存状态，来响应不同的内存释放策略
     *
     * @param context
     * @param level
     */
    fun trimMemory(context: Context?, level: Int) {
        mStrategy.trimMemory(context, level)
    }

    /**
     * 清除图片所有缓存
     */
    fun clearImageAllCache(context: Context) {
        clearImageDiskCache(context.applicationContext)
        clearImageMemoryCache(context.applicationContext)
    }

}