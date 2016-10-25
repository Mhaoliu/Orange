package com.liuhao.orange.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by liuhao on 2016/10/25.
 */
@Entity
public class WetherSearchBean {
    @Id
    public Long id;
    public String userName;
    public Long searTime;
    public String cityName;
    public String getCityName() {
        return this.cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public Long getSearTime() {
        return this.searTime;
    }
    public void setSearTime(Long searTime) {
        this.searTime = searTime;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 1119895021)
    public WetherSearchBean(Long id, String userName, Long searTime, String cityName) {
        this.id = id;
        this.userName = userName;
        this.searTime = searTime;
        this.cityName = cityName;
    }
    @Generated(hash = 811789661)
    public WetherSearchBean() {
    }

    @Override
    public String toString() {
        return "WetherSearchBean{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", searTime=" + searTime +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}
