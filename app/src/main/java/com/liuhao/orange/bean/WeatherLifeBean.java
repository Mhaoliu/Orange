package com.liuhao.orange.bean;

/**
 * Created by liuhao on 2016/10/24.
 */
public class WeatherLifeBean {
    private String zhiShi;
    private String jianYi;
    public WeatherLifeBean() {

    }

    public WeatherLifeBean(String zhiShi, String jianYi) {
        this.zhiShi = zhiShi;
        this.jianYi = jianYi;
    }

    public String getZhiShi() {
        return zhiShi;
    }

    public void setZhiShi(String zhiShi) {
        this.zhiShi = zhiShi;
    }

    public String getJianYi() {
        return jianYi;
    }

    public void setJianYi(String jianYi) {
        this.jianYi = jianYi;
    }
}
