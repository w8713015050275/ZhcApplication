package com.zhc.bizone.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhc.bizone.R
import com.zhc.common.api.Data

class BizOneActivityAdapter(data: List<Data>): BaseQuickAdapter<Data, BaseViewHolder>(R.layout.biz_one_adapter_item_view) {
    override fun convert(holder: BaseViewHolder, item: Data) {
        val userName = holder.getView<TextView>(R.id.userName)
        userName.text = item.first_name + " " + item.last_name
    }
}