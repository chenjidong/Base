package com.kuky.base.view.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.net.Uri
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuky.base.R
import com.kuky.base.databinding.ItemPicShowBinding

/**
 * @author Kuky
 */
class PicVpAdapter(private var mContext: Context, private var mList: MutableList<Uri>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (mList.isEmpty()) 0 else mList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val picBinding = DataBindingUtil.inflate<ItemPicShowBinding>(LayoutInflater.from(mContext), R.layout.item_pic_show, null, false)
        Glide.with(mContext)
                .load(mList[position])
                .apply(RequestOptions()
                        .placeholder(R.drawable.ic_launcher)
                        .centerCrop())
                .into(picBinding.picShow)
        container.addView(picBinding.root)
        return picBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun updateData(list: MutableList<Uri>) {
        this.mList = list
        notifyDataSetChanged()
    }
}