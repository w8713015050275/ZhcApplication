package com.zhc.common.executors

import android.annotation.SuppressLint
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.zhc.common.executors.Executor.dispatchers
//import com.zhangmen.braintrain.base.executors.Executor.dispatchers
//import com.zhangmen.braintrain.base.utils.Logger
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import kotlinx.coroutines.rx2.asCoroutineDispatcher
import okhttp3.internal.Util
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 使用rx的scheduler统一线程池
 */
data class AppRxSchedulers(
        val db: Scheduler,
        val bg: Scheduler,
        val io: Scheduler,
        val main: Scheduler)

data class AppCoroutineDispatchers(
        val db: CoroutineDispatcher,
        val bg: CoroutineDispatcher,
        val io: CoroutineDispatcher,
        val main: CoroutineDispatcher
)

object Executor {
    //统一io线程池
    val ioExecutor = ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
            SynchronousQueue(), Util.threadFactory("ZM IO Dispatcher", false))

    val schedulers = AppRxSchedulers(
            db = Schedulers.single(),
            bg = Schedulers.computation(),
            io = Schedulers.from(ioExecutor),
            main = AndroidSchedulers.mainThread()
    )

    val dispatchers = AppCoroutineDispatchers(
            db = schedulers.db.asCoroutineDispatcher(),
            bg = schedulers.bg.asCoroutineDispatcher(),
            io = schedulers.io.asCoroutineDispatcher(),
            main = schedulers.main.asCoroutineDispatcher()
    )

    @SuppressLint("CheckResult")
    fun <T> runInBg(block: () -> T,
                    scheduler: Scheduler = schedulers.bg,
                    success: (T) -> Unit = {},
                    error: () -> Unit = {}) {
        Single.fromCallable { block() }
                .subscribeOn(scheduler)
                .observeOn(schedulers.main)
                .subscribe({ resp ->
                    success(resp)
                }, {
                    error()
                })
    }

    fun <T> runInBg(block: () -> T) {
        runInBg(block, schedulers.bg)
    }
}

private var loadjob = SupervisorJob()

private val uiScope = CoroutineScope(
        dispatchers.main
                + loadjob
                + CoroutineExceptionHandler { _, exception ->
//            Logger.e("caught original $exception")
        })

fun <T> runInDispatcher(task: suspend () -> T,
                        dispatcher: CoroutineDispatcher = dispatchers.bg,
                        success: (T) -> Unit = {},
                        error: () -> Unit = {}): Job {
    return uiScope.launch {
        try {
            success(withContext(dispatcher) {
                task()
            })
        } catch (e: Exception) {
            error()
        } finally {
            //do nothing for now
        }

    }
}

/**
 * 默认rx的computation线程池运行
 * 耗时初始化可以runInBg一下
 */
fun <T> runInBg(task: suspend () -> T): Job {
    return runInDispatcher(task, dispatchers.bg, {}, {})
}

/**
 * android主线程执行
 */
fun <T> runInMain(task: suspend () -> T): Job {
    return runInDispatcher(task, dispatchers.main, {}, {})
}

fun delay(time: Long, task: () -> Unit): Job {
    val isMainThread = Looper.getMainLooper().thread == Thread.currentThread()
    return runInBg {
        delay(time)
        if (isMainThread) {
            runInMain { task() }
        } else {
            task()
        }
    }
}

fun Job.bindLifeCircle(owner: LifecycleOwner) {
    owner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            this@bindLifeCircle.cancel()
        }
    })
}