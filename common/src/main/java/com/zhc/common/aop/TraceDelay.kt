package com.zhc.common.aop

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy


/**
 * class description here
 *
 * @author xiaoming1109@gmail.com
 * @version 1.0.0
 * @since 2018-08-21
 */
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS
)
@Retention(RetentionPolicy.RUNTIME)
annotation class TraceDelay {

}
