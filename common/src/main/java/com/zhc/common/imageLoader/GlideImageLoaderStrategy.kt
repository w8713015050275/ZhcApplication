package com.zhc.common.imageLoader

import android.content.Context
import android.content.res.Resources
import android.os.Looper
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.zhc.common.executors.Executor
import jp.wasabeef.glide.transformations.CropCircleTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import java.io.File

object GlideImageLoaderStrategy : BaseImageLoaderStrategy {

    override fun loadImage(
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadImage(
        context: Context?,
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    ) {
        Glide.with(context!!)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView!!)
    }

    override fun loadImage(
        url: String?,
        requestOptions: RequestOptions?,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(requestOptions!!)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadImage(
        url: String?,
        placeholderId: Int,
        width: Int,
        height: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .override(width, height)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadImage(url: String?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions() //                        .placeholder(imageView.getDrawable())
                    //                        .error(imageView.getDrawable())
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadFileImage(file: File?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(file)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadResImage(resId: Int, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(resId)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadCircleImage(
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    /*.transforms(
                        CenterCrop()
                        , GlideCircleTransform()
                    )*/.transform(CropCircleTransformation())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadCircleImage(url: String?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable)
                    /*.transforms(
                        CenterCrop()
                        , GlideCircleTransform()
                    )*/
                    .transform(CropCircleTransformation())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
    override fun loadCircleImageWithBorder(url: String?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable)
                    /*.transforms(
                        CenterCrop()
                        , GlideCircleTransform()
                    )*/
                    .transform(CropCircleWithBorderTransformation())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadCircleImage(resId: Int, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(resId)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable)
                   /* .transforms(
                        CenterCrop()
                        , GlideCircleTransform()
                    )*/
                    .transform(CropCircleTransformation())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadRoundImage(resId: Int, radius: Int, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(resId)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable) //                        .transforms(new CenterCrop()
                    //                                , new GlideRoundTransform(radius))
                    .transforms(
                        CenterCrop(),
                        RoundedCorners(
                            (Resources.getSystem()
                                .displayMetrics.density * radius).toInt()
                        )
                    )
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadRoundImage(
        url: String?,
        radius: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(imageView.drawable)
                    .error(imageView.drawable) //                        .transforms(new CenterCrop()
                    //                                , new GlideRoundTransform(radius))
                    .transforms(
                        CenterCrop(),
                        RoundedCorners(
                            (Resources.getSystem()
                                .displayMetrics.density * radius).toInt()
                        )
                    )
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadRoundImage(
        url: String?,
        placeholderId: Int,
        radius: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId) //                        .transforms(new CenterCrop()
                    //                                , new GlideRoundTransform(radius))
                    .transforms(
                        CenterCrop(),
                        RoundedCorners(
                            (Resources.getSystem()
                                .displayMetrics.density * radius).toInt()
                        )
                    )
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadRoundImageWithoutAnim(
        url: String?,
        placeholderId: Int,
        radius: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .transforms(
                        CenterCrop(),
                        RoundedCorners(
                            (Resources.getSystem()
                                .displayMetrics.density * radius).toInt()
                        )
                    )
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)
    }

    override fun loadGifImage(
        url: String?,
        placeholderId: Int,
        imageView: ImageView
    ) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions()
                    .placeholder(placeholderId)
                    .error(placeholderId)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)
    }

    override fun loadGifImage(url: String?, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .apply(
                RequestOptions() //                        .placeholder(imageView.getDrawable())
                    //                        .error(imageView.getDrawable())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)
    }

    override fun loadGifImage(resId: Int, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(resId)
            .apply(
                RequestOptions() //                        .placeholder(imageView.getDrawable())
                    //                        .error(imageView.getDrawable())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(imageView)
    }

    override fun loadGifImage(resId: Int, imageView: ImageView, count: Int) {
        Glide.with(imageView.context)
            .asGif()
            .load(resId)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<GifDrawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    gifDrawable: GifDrawable,
                    model: Any,
                    target: Target<GifDrawable>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    //设置循环播放次数为1次
                    gifDrawable.setLoopCount(count)
                    return false
                }
            })
            .into(imageView)
    }

    override fun clearImageDiskCache(context: Context) {
        Executor.runInBg {
            Glide.get(context.applicationContext).clearDiskCache()
            null
        }
    }

    override fun clearImageMemoryCache(context: Context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context.applicationContext).clearMemory()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun trimMemory(context: Context?, level: Int) {
        Glide.get(context!!).trimMemory(level)
    }
}