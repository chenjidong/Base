package com.kuky.baselib.baseAdapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * @author Kuky
 */
abstract class LooperBannerAdapter<T : Any, VB : ViewDataBinding>(protected var mContext: Context, protected var mList: MutableList<T>, protected var mViewPager: ViewPager)
    : PagerAdapter(), ViewPager.OnPageChangeListener {

    protected var mCurrentPosition = 0
    protected lateinit var mViewBinding: VB

    init {
        if (mList.size > 1) {
            mList.add(mList[0])
            mList.add(0, mList[mList.size - 2])
        }

        mViewPager.adapter = this
        mViewPager.addOnPageChangeListener(this)
        mViewPager.setCurrentItem(1, false)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return if (mList.isEmpty()) 0 else mList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        mViewBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getItemLayoutId(), null, false)
        itemInitial(position)
        container.addView(mViewBinding.root)
        return mViewBinding.root
    }

    abstract fun getItemLayoutId(): Int

    abstract fun itemInitial(position: Int)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun updateList(list: MutableList<T>) {
        this.mList = list
        notifyDataSetChanged()
    }

    fun clearData() {
        this.mList.clear()
        notifyDataSetChanged()
    }

    /**
     * ViewPager 刷新问题
     */
    override fun getItemPosition(`object`: Any): Int {
        return if (mList.isEmpty()) super.getItemPosition(`object`) else POSITION_NONE
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (state != ViewPager.SCROLL_STATE_IDLE) return
        if (mCurrentPosition == 0) mViewPager.setCurrentItem(mList.size - 2, false)
        else if (mCurrentPosition == mList.size - 1) mViewPager.setCurrentItem(1, false)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        mCurrentPosition = position
    }
}