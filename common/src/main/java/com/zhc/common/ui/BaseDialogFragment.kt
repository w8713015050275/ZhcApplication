package com.zhc.common.ui

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.zhc.common.R
import com.zhc.common.vm.BaseViewModel

abstract class BaseDialogFragment<VM : BaseViewModel, T> : DialogFragment() {

    protected abstract fun getLayoutId(): Int

    abstract fun generateViewModel(): VM

    abstract fun onViewCreatedFinish(view: View, savedInstanceState: Bundle?)

    protected lateinit var viewModel: VM

    var enableLoading = true

    private lateinit var listener: ListenerBuilder<T>

    private var confirmClicked = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#7C000000")))
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var isShow  = this.showsDialog
        showsDialog = false
        super.onViewCreated(view, savedInstanceState)
        showsDialog = isShow
        viewModel = generateViewModel()

        if (enableLoading) {
            viewModel.loading.observe(viewLifecycleOwner, Observer {
                showLoading(it)
            })
        }

        viewModel.successToast.observe(viewLifecycleOwner, Observer {
//            ToastUtils.showSuccess(it)
        })

        viewModel.errorToast.observe(viewLifecycleOwner, Observer {
//            ToastUtils.showError(it)
        })

        if (canCancel()) {
            dialog?.run {
                setCancelable(true)
                setCanceledOnTouchOutside(true)
            }
        } else {
            dialog?.run {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setOnKeyListener { _, _, _ -> true }
            }
        }

        onViewCreatedFinish(view, savedInstanceState)
    }

    open fun canCancel(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogFragmentStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (::listener.isInitialized) {
            listener.dismissAction?.invoke(confirmClicked)
        }
    }

    open fun registerListener(listenerBuilder: ListenerBuilder<T>.() -> Unit): BaseDialogFragment<VM, T> {
        listener = ListenerBuilder<T>().also(listenerBuilder)
        return this
    }

    fun showLoading(show: Boolean) {
        when (show) {
//            true -> (activity as BaseActivity<*>).showLoading()
//            false -> (activity as BaseActivity<*>).hideLoading()
        }
    }

    open fun showSuccessMessage(msg: String) {
//        (activity as BaseActivity<*>).showSuccessMessage(msg)
    }

    open fun showErrorMessage(msg: String) {
//        (activity as BaseActivity<*>).showErrorMessage(msg)
    }

    protected open fun invokeConfirmed(selected: T) {
        this.confirmClicked = true
        if (::listener.isInitialized) {
            listener.confirmedAction?.invoke(selected)
        }
    }

    protected fun invokeCanceled() {
        if (::listener.isInitialized) {
            listener.cancelAction?.invoke()
        }
    }

    protected fun invokeDismissed() {
        if (::listener.isInitialized) {
            listener.dismissAction?.invoke(confirmClicked)
        }
        confirmClicked = false
    }

    fun show(manager: FragmentManager) {
        show(manager, javaClass.canonicalName)
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            DialogFragment::class.java.getDeclaredField("mDismissed").apply {
                isAccessible = true
                setBoolean(this@BaseDialogFragment, false)
            }
            DialogFragment::class.java.getDeclaredField("mShownByMe").apply {
                isAccessible = true
                setBoolean(this@BaseDialogFragment, true)
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        try {
            manager.beginTransaction()
                    .add(this, tag)
                    .commitAllowingStateLoss()
        } catch (ignore: Exception) {
            // do nothing
        }
    }

    override fun showNow(manager: FragmentManager, tag: String?) {
        show(manager, tag)
    }

    class ListenerBuilder<T> {
        internal var confirmedAction: ((T) -> Unit)? = null
        internal var cancelAction: (() -> Unit)? = null
        internal var dismissAction: ((Boolean) -> Unit)? = null

        fun confirmed(action: (T) -> Unit) {
            confirmedAction = action
        }

        fun canceled(action: () -> Unit) {
            cancelAction = action
        }

        fun dismissed(action: (Boolean) -> Unit) {
            dismissAction = action
        }
    }
}


