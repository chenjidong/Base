package com.kuky.base.view.adapter

import android.content.Context
import android.net.Uri
import android.support.v4.view.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuky.base.R
import com.kuky.base.databinding.ItemPicShowBinding
import com.kuky.baselib.baseAdapter.LooperBannerAdapter

/**
 * @author Kuky
 */
class PicSelectedResultAdapter(context: Context, list: MutableList<Uri>, viewPager: ViewPager) : LooperBannerAdapter<Uri, ItemPicShowBinding>(context, list, viewPager) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_pic_show
    }

    override fun itemInitial(position: Int) {
        Glide.with(mContext)
                .load(mList[position])
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_launcher)
                        .centerCrop())
                .into(mViewBinding.picShow)
    }
}