package com.kuky.base.db_entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * @author Kuky
 */
@Entity
public class Hobby {
    @Id
    private Long id;
    private Long userId;
    @NotNull
    private String name;

    @Generated(hash = 264780604)
    public Hobby(Long id, Long userId, @NotNull String name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    @Generated(hash = 23756816)
    public Hobby() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
