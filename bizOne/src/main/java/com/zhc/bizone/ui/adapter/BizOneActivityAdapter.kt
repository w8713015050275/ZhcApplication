package com.zhc.bizone.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhc.bizone.R
import com.zhc.common.api.Data
import com.zhc.common.imageLoader.ImageLoader

class BizOneActivityAdapter(data: List<Data>): BaseQuickAdapter<Data, BaseViewHolder>(R.layout.biz_one_adapter_item_view) {
    override fun convert(holder: BaseViewHolder, item: Data) {
        val userName = holder.getView<TextView>(R.id.bizOneUserName)
        userName.text = item.first_name + " " + item.last_name

        val avator = holder.getView<ImageView>(R.id.bizOneAvator)
        ImageLoader.loadCircleImageWithBorder(item.avatar, avator)
    }
}