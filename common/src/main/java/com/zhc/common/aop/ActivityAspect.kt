package com.zhc.common.aop

import android.util.Log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect


@Aspect
class ActivityAspect {
    /**
     *
     * @param joinPoint
     * @throws Throwable
     */
    @After("within(@com.zhc.common.aop.TraceDelay *)")
    @Throws(Throwable::class)
    fun onUi(joinPoint: JoinPoint) {
        Log.d("helloAOP", "" + joinPoint.signature)
    }


    @After("execution(* android.app.Activity.on**(..))")
    @Throws(
        Throwable::class
    )
    fun onResumeMethod(joinPoint: JoinPoint) {
        Log.d("helloAOP", "aspect:::" + joinPoint.signature)
    }
}