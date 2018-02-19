package com.kuky.baselib

import android.app.Activity

/**
 * @author Kuky
 */
object ActivityManager {
    private val activityList = mutableListOf<Activity>()

    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (activity in activityList) {
            activityList.remove(activity)
            activity.finish()
        } else {
            return
        }
    }

    fun getTopActivity(): Activity? {
        return if (!activityList.isEmpty())
            activityList[0]
        else
            null
    }

    fun finishAllActivity() {
        activityList
                .filterNot { it.isFinishing }
                .forEach { it.finish() }
    }
}