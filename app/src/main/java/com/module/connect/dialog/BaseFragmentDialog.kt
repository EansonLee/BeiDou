package com.module.connect.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.viewbinding.ViewBinding
import com.module.connect.R

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/11/16
 * @desc:
 */
abstract class BaseFragmentDialog<VB : ViewBinding> : AppCompatDialogFragment() {

    protected lateinit var binding: VB

    var onItemConfirmClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getBinding(inflater, container)
        initWindow()
        notCanceledOnTouchOutsize()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initData()
    }

    abstract fun getBinding(inflate: LayoutInflater, container: ViewGroup?): VB
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()

    protected open fun getGravity() = Gravity.CENTER

    protected open fun getPadding(): Int = resources.getDimensionPixelOffset(R.dimen.base_dp_30)

    protected open fun isCanceledOnTouchOutsize(): Boolean = true

    protected open fun getWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT

    protected open fun getHeight(): Int = WindowManager.LayoutParams.WRAP_CONTENT

    protected open fun initWindow() {
        val window = dialog?.window
        window?.let {
            it.setBackgroundDrawableResource(android.R.color.transparent)
            it.decorView.setPadding(getPadding(), 0, getPadding(), 0)
            val wlp = it.attributes
            wlp.gravity = getGravity()
            wlp.width = getWidth()
            wlp.height = getHeight()
            it.attributes = wlp
        }
    }

    open fun notCanceledOnTouchOutsize() {
        dialog?.setCancelable(isCanceledOnTouchOutsize())
        dialog?.setCanceledOnTouchOutside(isCanceledOnTouchOutsize())
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true
                }
                return false
            }
        })
    }
}