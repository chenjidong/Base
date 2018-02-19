package com.kuky.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Environment
import android.support.multidex.MultiDex
import com.kuky.base.entity.C
import com.kuky.base.greendao.DaoMaster
import com.kuky.base.greendao.DaoSession
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import zlc.season.rxdownload3.core.DownloadConfig
import zlc.season.rxdownload3.extension.ApkInstallExtension
import zlc.season.rxdownload3.extension.ApkOpenExtension
import java.io.File

/**
 * @author Kuky
 */
@SuppressLint("StaticFieldLeak")
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

//        setUpLeakCanary()
        setUpDatabase(this)

        DownloadConfig.init(DownloadConfig.Builder
                .create(this)
                .setDefaultPath(File(Environment.getExternalStorageDirectory(), C.DOWNLOAD_FOLDER).absolutePath)
                .enableService(true)
                .enableNotification(true)
                .addExtension(ApkInstallExtension::class.java)
                .addExtension(ApkOpenExtension::class.java))
    }

    /**
     * init LeakCanary
     */
    private fun setUpLeakCanary(): RefWatcher {
        if (LeakCanary.isInAnalyzerProcess(mContext))
            return RefWatcher.DISABLED
        return LeakCanary.install(this)
    }

    /**
     * init GreenDao database
     */
    private fun setUpDatabase(context: Context) {
        val openHelper = DaoMaster.DevOpenHelper(context, C.DATABASE_NAME, null)
        val db = openHelper.writableDb
        val daoMaster = DaoMaster(db)
        mDaoSession = daoMaster.newSession()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private lateinit var mContext: Context
        private lateinit var mDaoSession: DaoSession

        fun getContext(): Context {
            return mContext
        }

        fun getSession(): DaoSession {
            return mDaoSession
        }
    }
}