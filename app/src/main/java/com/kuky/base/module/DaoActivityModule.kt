package com.kuky.base.module

import com.kuky.base.BaseApplication
import com.kuky.base.greendao.HobbyDao
import com.kuky.base.greendao.OrderDao
import com.kuky.base.greendao.UserDao
import dagger.Module
import dagger.Provides

/**
 * @author Kuky
 */
@Module
class DaoActivityModule {

    @Provides
    fun provideUserDao(): UserDao {
        return BaseApplication.getSession().userDao
    }

    @Provides
    fun provideOrderDao(): OrderDao {
        return BaseApplication.getSession().orderDao
    }

    @Provides
    fun provideHobbyDao(): HobbyDao {
        return BaseApplication.getSession().hobbyDao
    }
}