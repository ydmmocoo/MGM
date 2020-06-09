package com.library.repository.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author    by hanlz
 * Date      on 2019/10/23.
 * Description：
 */
@Entity
public class TestModel {
    @Id
    Long id;

    private int a;
    private String b;
    @Generated(hash = 1137861633)
    public TestModel(Long id, int a, String b) {
        this.id = id;
        this.a = a;
        this.b = b;
    }
    @Generated(hash = 1568142977)
    public TestModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getA() {
        return this.a;
    }
    public void setA(int a) {
        this.a = a;
    }
    public String getB() {
        return this.b;
    }
    public void setB(String b) {
        this.b = b;
    }
}
