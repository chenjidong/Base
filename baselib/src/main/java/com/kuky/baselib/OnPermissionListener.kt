package com.kuky.baselib

/**
 * @author Kuky
 */
interface OnPermissionListener {
    fun onGranted()

    fun onDenied(deniedPermissions: MutableList<String>)
}