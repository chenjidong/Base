package com.kuky.baselib

import android.view.View

/**
 * @author Kuky
 */
interface OnGroupListener {
    fun getGroupName(position: Int): String?

    fun getGroupView(position: Int): View?
}