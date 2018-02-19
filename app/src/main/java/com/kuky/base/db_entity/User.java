package com.kuky.base.db_entity;

import com.kuky.base.greendao.DaoSession;
import com.kuky.base.greendao.HobbyDao;
import com.kuky.base.greendao.UserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * @author Kuky
 *         <p>
 *         目前不支持 Kotlin 实体类的配置，写完实体类参数，Build -> Make Project/Make Model 'app' 自动生成方法
 */

@Entity
public class User {
    @Id(autoincrement = true) // 主键必须 Long/long 型, 可以设置是否自增
    private Long id;
    @NotNull // 数据库当前列不为空
    private String name;
    private int age;
    @Unique //数据不重复
    private String identificationCode;
    @Transient // 不会被持久化, 不写入数据库的列
    private String tempUsageCount;

    @ToMany(referencedJoinProperty = "userId") // 一对多关系
    @OrderBy("id ASC")
    private List<Hobby> hobbies;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    @Generated(hash = 1745908879)
    public User(Long id, @NotNull String name, int age, String identificationCode) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.identificationCode = identificationCode;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", identificationCode='" + identificationCode + '\'' +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdentificationCode() {
        return this.identificationCode;
    }

    public void setIdentificationCode(String identificationCode) {
        this.identificationCode = identificationCode;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 356296690)
    public List<Hobby> getHobbies() {
        if (hobbies == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HobbyDao targetDao = daoSession.getHobbyDao();
            List<Hobby> hobbiesNew = targetDao._queryUser_Hobbies(id);
            synchronized (this) {
                if (hobbies == null) {
                    hobbies = hobbiesNew;
                }
            }
        }
        return hobbies;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 994162544)
    public synchronized void resetHobbies() {
        hobbies = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
}
