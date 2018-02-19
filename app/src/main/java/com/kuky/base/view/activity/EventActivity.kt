package com.kuky.base.view.activity

import android.os.Bundle
import android.view.View
import com.kuky.base.R
import com.kuky.base.databinding.ActivityEventBinding
import com.kuky.base.event.TransEvent
import com.kuky.baselib.baseClass.BaseActivity
import org.greenrobot.eventbus.EventBus

class EventActivity : BaseActivity<ActivityEventBinding>() {

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_event
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mViewBinding.event = this@EventActivity
    }

    override fun setListener() {

    }

    fun eventTrans(view: View) {
        EventBus.getDefault().post(TransEvent("Hello -- Greeting from EventActivity"))
        finish()
    }
}
