package com.kuky.base.module

import com.kuky.base.BaseApplication
import com.kuky.base.greendao.HobbyDao
import com.kuky.base.greendao.OrderDao
import com.kuky.base.greendao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * @author Kuky
 */
@Module
class DaoActivityModule {

    @Singleton
    @Provides
    fun provideUserDao(): UserDao {
        return BaseApplication.getSession().userDao
    }

    @Singleton
    @Provides
    fun provideOrderDao(): OrderDao {
        return BaseApplication.getSession().orderDao
    }

    @Singleton
    @Provides
    fun provideHobbyDao(): HobbyDao {
        return BaseApplication.getSession().hobbyDao
    }
}