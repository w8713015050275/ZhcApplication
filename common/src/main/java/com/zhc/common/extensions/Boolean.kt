package com.zhc.common.extensions

/**
 *  Created by yetao on 2019-10-18
 *  description
 **/

inline infix fun Boolean.isTrue(action: () -> Unit): Boolean = when (this) {
    true -> this.also { action.invoke() }
    false -> this
}

inline infix fun Boolean.isFalse(action: () -> Unit) = when (this) {
    true -> this
    false -> this.also { action.invoke() }
}