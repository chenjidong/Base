package com.kuky.base.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.kuky.base.R
import com.kuky.base.component.DaggerDaoActivityComponent
import com.kuky.base.databinding.ActivityDaoBinding
import com.kuky.base.db_entity.Hobby
import com.kuky.base.db_entity.Order
import com.kuky.base.db_entity.User
import com.kuky.base.entity.C
import com.kuky.base.greendao.HobbyDao
import com.kuky.base.greendao.OrderDao
import com.kuky.base.greendao.UserDao
import com.kuky.base.module.DaoActivityModule
import com.kuky.baselib.baseClass.BaseActivity
import com.kuky.baselib.baseUtils.LogUtils
import com.kuky.baselib.baseUtils.SharePreferencesUtils
import com.kuky.baselib.baseUtils.ToastUtils
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class DaoActivity : BaseActivity<ActivityDaoBinding>() {

    @Inject lateinit var mUserDao: UserDao
    @Inject lateinit var mOrderDao: OrderDao
    @Inject lateinit var mHobbyDao: HobbyDao
    private var isFirst = true
    private var oddUp = false
    private var odd = false
    private val querySql = """
        select u._id, u.NAME, u.AGE, u.IDENTIFICATION_CODE from USER as u
        where u.IDENTIFICATION_CODE="${C.IDENTIFICATION_CODE}" order by u._id
    """.trimIndent()

    override fun openTransparentStatus(): Boolean {
        return false
    }

    override fun enabledEventBus(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dao
    }

    override fun initActivity(savedInstanceState: Bundle?) {
        mViewBinding.dao = this@DaoActivity

        DaggerDaoActivityComponent.builder()
                .daoActivityModule(DaoActivityModule())
                .build().inject(this@DaoActivity)
    }

    override fun setListener() {

    }

    fun insertData(view: View) {
        isFirst = false
        SharePreferencesUtils.saveBoolean(this@DaoActivity, C.FIRST_INSTALL, isFirst)

        val sb = StringBuffer()
        val user = User(null, C.NAME, 20, C.IDENTIFICATION_CODE)
        val user2 = User(null, C.NAME_ED, 20, C.IDENTIFICATION_CODE_ED)

        if (mUserDao.queryBuilder()
                .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE))
                .build().unique() != null) {
            sb.append("User ${user.name} exits")
        } else {
            mUserDao.insert(user)
            sb.append("Insert ${user.name} succeed")
        }

        if (mUserDao.queryBuilder()
                .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE_ED))
                .build().unique() != null) {
            sb.append("\n").append("User ${user2.name} exits")
        } else {
            mUserDao.insert(user2)
            sb.append("\n").append("Insert ${user2.name} succeed")
        }

        mViewBinding.operateResult.text = sb.toString().trim()

        val order = Order(null, user.id, "Order 01", 100.99, C.ORDER)
        val order2 = Order(null, user.id, "Order 02", 198.99, C.ORDER_ED)

        if (mOrderDao.queryBuilder()
                .where(OrderDao.Properties.UniqueId.eq(C.ORDER))
                .build().unique() != null)
            mOrderDao.insert(order)

        if (mOrderDao.queryBuilder()
                .where(OrderDao.Properties.UniqueId.eq(C.ORDER_ED))
                .build().unique() != null)
            mOrderDao.insert(order2)

        val hobby = Hobby(null, user.id, "Basketball")
        val hobby2 = Hobby(null, user.id, "Football")
        mHobbyDao.insert(hobby)
        mHobbyDao.insert(hobby2)
    }

    fun updateData(view: View) {
        if (!SharePreferencesUtils.getBoolean(this@DaoActivity, C.FIRST_INSTALL, true)) {
            oddUp = !oddUp
            val sb = StringBuffer()
            val user = if (oddUp) mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE))
                    .build().unique()
            else mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE_ED))
                    .build().unique()// 查找单个数据

            if (user == null) {
                sb.append("User whose identification code is " +
                        "${if (oddUp) C.IDENTIFICATION_CODE else C.IDENTIFICATION_CODE_ED} not exits")
            } else {
                user.name = if (oddUp) "Kuky" else "Cucy"
                mUserDao.update(user)
                sb.append("Update data succeed and user name after updated is ${user.name}")
            }

            val userList = mUserDao.queryBuilder()
                    .orderAsc(UserDao.Properties.Id)
                    .build().list() // 查找多个数据

            if (userList.isNotEmpty()) {
                sb.append("\n")
                userList.forEach { sb.append(it.toString()).append("\n") }
            }

            mViewBinding.operateResult.text = sb.toString().trim()

            val user1 = mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE))
                    .build().unique()

            val user2 = mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE_ED))
                    .build().unique()

            val id1 = if (user1 != null) user1.id else -1L

            val id2 = if (user2 != null) user2.id else -1L

            if (id1 != -1L && id2 != -1L) {
                val orderList = mOrderDao.queryBuilder()
                        .where(OrderDao.Properties.UserId.eq(id1))
                        .build().list()

                if (orderList.isNotEmpty())
                    orderList.forEach {
                        it.userId = id2
                        mOrderDao.update(it)
                    }
            }

        } else {
            ToastUtils.showToast(this@DaoActivity, "Insert data first please")
        }
    }

    fun queryData(view: View) {
        if (!SharePreferencesUtils.getBoolean(this@DaoActivity, C.FIRST_INSTALL, true)) {
            val sb = StringBuffer()
            val userList = mUserDao.queryBuilder().orderAsc(UserDao.Properties.Id).build().list()
            if (userList.isNotEmpty())
                userList.forEach { sb.append(it.toString()).append("\n") }
            else
                sb.append("There is no user exits")

            mViewBinding.operateResult.text = sb.toString().trim()

            val orderList = mOrderDao.queryBuilder().orderAsc(OrderDao.Properties.Id).build().list()
            if (orderList.isNotEmpty())
                orderList.forEach {
                    LogUtils.e("orderId= ${it.id}, orderName= ${it.orderName}, " +
                            "userId= ${it.userId}, userName= ${it.user.name}")
                }

            // 使用原生 Sql 查询
            val cursor = mUserDao.database.rawQuery(querySql, null)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(0)
                val name = cursor.getString(1)
                val age = cursor.getInt(2)
                val identificationCode = cursor.getString(3)
                LogUtils.e("User[id= $id, name= $name, age= $age, identificationCode= $identificationCode]")
            }

            val user = mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE))
                    .build().unique()
            val hobbyList = if (user != null) user.hobbies else mutableListOf()

            if (user != null && hobbyList.isNotEmpty())
                hobbyList.forEach { LogUtils.e("hobbyId= ${it.id}, hobbyName= ${it.name}") }
        } else {
            ToastUtils.showToast(this@DaoActivity, "Insert data first please")
        }
    }

    fun deleteData(view: View) {
        if (!SharePreferencesUtils.getBoolean(this@DaoActivity, C.FIRST_INSTALL, true)) {
            odd = !odd
            val sb = StringBuffer()
            val user = if (odd) mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE))
                    .build().unique()
            else mUserDao.queryBuilder()
                    .where(UserDao.Properties.IdentificationCode.eq(C.IDENTIFICATION_CODE_ED))
                    .build().unique()

            if (user != null) {
                sb.append("Delete ${user.name} succeed")
                mUserDao.delete(user)
            } else {
                sb.append("There is no user exits")
            }

            mViewBinding.operateResult.text = sb.toString().trim()

            val order = if (odd) mOrderDao.queryBuilder()
                    .where(OrderDao.Properties.UniqueId.eq(C.ORDER))
                    .build().unique()
            else mOrderDao.queryBuilder()
                    .where(OrderDao.Properties.UniqueId.eq(C.ORDER_ED))
                    .build().unique()

            if (order != null)
                mOrderDao.delete(order)
        } else {
            ToastUtils.showToast(this@DaoActivity, "Insert data first please")
        }
    }
}
