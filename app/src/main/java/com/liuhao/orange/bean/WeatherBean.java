package com.liuhao.orange.bean;

import java.util.List;

/**
 * Created by liuhao on 2016/10/22.
 */
public class WeatherBean {
    @Override
    public String toString() {
        return "WeatherBean{" +
                "data=" + data +
                '}';
    }

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        @Override
        public String toString() {
            return "DataBean{" +
                    "realtime=" + realtime +
                    ", life=" + life +
                    ", f3h=" + f3h +
                    ", pm25=" + pm25 +
                    ", jingqu='" + jingqu + '\'' +
                    ", jingqutq='" + jingqutq + '\'' +
                    ", date='" + date + '\'' +
                    ", isForeign='" + isForeign + '\'' +
                    ", weather=" + weather +
                    '}';
        }

        private RealtimeBean realtime;


        private LifeBean life;
        private FhBean f3h;


        private PmBean pm25;
        private String jingqu;
        private String jingqutq;
        private String date;
        private String isForeign;


        private List<WeatherInfo> weather;

        public RealtimeBean getRealtime() {
            return realtime;
        }

        public void setRealtime(RealtimeBean realtime) {
            this.realtime = realtime;
        }

        public LifeBean getLife() {
            return life;
        }

        public void setLife(LifeBean life) {
            this.life = life;
        }

        public FhBean getF3h() {
            return f3h;
        }

        public void setF3h(FhBean f3h) {
            this.f3h = f3h;
        }

        public PmBean getPm25() {
            return pm25;
        }

        public void setPm25(PmBean pm25) {
            this.pm25 = pm25;
        }

        public String getJingqu() {
            return jingqu;
        }

        public void setJingqu(String jingqu) {
            this.jingqu = jingqu;
        }

        public String getJingqutq() {
            return jingqutq;
        }

        public void setJingqutq(String jingqutq) {
            this.jingqutq = jingqutq;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getIsForeign() {
            return isForeign;
        }

        public void setIsForeign(String isForeign) {
            this.isForeign = isForeign;
        }

        public List<WeatherInfo> getWeather() {
            return weather;
        }

        public void setWeather(List<WeatherInfo> weather) {
            this.weather = weather;
        }

        public static class RealtimeBean {
            private String city_code;
            private String city_name;
            private String date;
            private String time;
            private int week;
            private String moon;
            private int dataUptime;


            private WeatherInfo weather;


            private WindBean wind;

            @Override
            public String toString() {
                return "RealtimeBean{" +
                        "city_code='" + city_code + '\'' +
                        ", city_name='" + city_name + '\'' +
                        ", date='" + date + '\'' +
                        ", time='" + time + '\'' +
                        ", week=" + week +
                        ", moon='" + moon + '\'' +
                        ", dataUptime=" + dataUptime +
                        ", weather=" + weather +
                        ", wind=" + wind +
                        '}';
            }

            public String getCity_code() {
                return city_code;
            }

            public void setCity_code(String city_code) {
                this.city_code = city_code;
            }

            public String getCity_name() {
                return city_name;
            }

            public void setCity_name(String city_name) {
                this.city_name = city_name;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getWeek() {
                return week;
            }

            public void setWeek(int week) {
                this.week = week;
            }

            public String getMoon() {
                return moon;
            }

            public void setMoon(String moon) {
                this.moon = moon;
            }

            public int getDataUptime() {
                return dataUptime;
            }

            public void setDataUptime(int dataUptime) {
                this.dataUptime = dataUptime;
            }

            public WeatherInfo getWeather() {
                return weather;
            }

            public void setWeather(WeatherInfo weather) {
                this.weather = weather;
            }

            public WindBean getWind() {
                return wind;
            }

            public void setWind(WindBean wind) {
                this.wind = wind;
            }

            public static class WeatherInfo {
                private String temperature;
                private String humidity;
                private String info;
                private String img;

                @Override
                public String toString() {
                    return "WeatherInfo{" +
                            "temperature='" + temperature + '\'' +
                            ", humidity='" + humidity + '\'' +
                            ", info='" + info + '\'' +
                            ", img='" + img + '\'' +
                            '}';
                }

                public String getTemperature() {
                    return temperature;
                }

                public void setTemperature(String temperature) {
                    this.temperature = temperature;
                }

                public String getHumidity() {
                    return humidity;
                }

                public void setHumidity(String humidity) {
                    this.humidity = humidity;
                }

                public String getInfo() {
                    return info;
                }

                public void setInfo(String info) {
                    this.info = info;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }
            }

            public static class WindBean {
                private String direct;
                private String power;
                private Object offset;
                private Object windspeed;

                @Override
                public String toString() {
                    return "WindBean{" +
                            "direct='" + direct + '\'' +
                            ", power='" + power + '\'' +
                            ", offset=" + offset +
                            ", windspeed=" + windspeed +
                            '}';
                }

                public String getDirect() {
                    return direct;
                }

                public void setDirect(String direct) {
                    this.direct = direct;
                }

                public String getPower() {
                    return power;
                }

                public void setPower(String power) {
                    this.power = power;
                }

                public Object getOffset() {
                    return offset;
                }

                public void setOffset(Object offset) {
                    this.offset = offset;
                }

                public Object getWindspeed() {
                    return windspeed;
                }

                public void setWindspeed(Object windspeed) {
                    this.windspeed = windspeed;
                }
            }
        }

        public static class LifeBean {
            private String date;
            private InfoBean info;

            @Override
            public String toString() {
                return "LifeBean{" +
                        "date='" + date + '\'' +
                        ", info=" + info +
                        '}';
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public static class InfoBean {
                private List<String> chuanyi;
                private List<String> ganmao;
                private List<String> kongtiao;
                private List<String> xiche;
                private List<String> yundong;
                private List<String> ziwaixian;

                @Override
                public String toString() {
                    return "InfoBean{" +
                            "chuanyi=" + chuanyi +
                            ", ganmao=" + ganmao +
                            ", kongtiao=" + kongtiao +
                            ", xiche=" + xiche +
                            ", yundong=" + yundong +
                            ", ziwaixian=" + ziwaixian +
                            '}';
                }

                public List<String> getChuanyi() {
                    return chuanyi;
                }

                public void setChuanyi(List<String> chuanyi) {
                    this.chuanyi = chuanyi;
                }

                public List<String> getGanmao() {
                    return ganmao;
                }

                public void setGanmao(List<String> ganmao) {
                    this.ganmao = ganmao;
                }

                public List<String> getKongtiao() {
                    return kongtiao;
                }

                public void setKongtiao(List<String> kongtiao) {
                    this.kongtiao = kongtiao;
                }

                public List<String> getXiche() {
                    return xiche;
                }

                public void setXiche(List<String> xiche) {
                    this.xiche = xiche;
                }

                public List<String> getYundong() {
                    return yundong;
                }

                public void setYundong(List<String> yundong) {
                    this.yundong = yundong;
                }

                public List<String> getZiwaixian() {
                    return ziwaixian;
                }

                public void setZiwaixian(List<String> ziwaixian) {
                    this.ziwaixian = ziwaixian;
                }
            }
        }

        public static class FhBean {

            @Override
            public String toString() {
                return "FhBean{" +
                        "temperature=" + temperature +
                        ", precipitation=" + precipitation +
                        '}';
            }

            private List<TemperatureBean> temperature;


            private List<PrecipitationBean> precipitation;

            public List<TemperatureBean> getTemperature() {
                return temperature;
            }

            public void setTemperature(List<TemperatureBean> temperature) {
                this.temperature = temperature;
            }

            public List<PrecipitationBean> getPrecipitation() {
                return precipitation;
            }

            public void setPrecipitation(List<PrecipitationBean> precipitation) {
                this.precipitation = precipitation;
            }

            public static class TemperatureBean {
                private String jg;
                private String jb;

                @Override
                public String toString() {
                    return "TemperatureBean{" +
                            "jg='" + jg + '\'' +
                            ", jb='" + jb + '\'' +
                            '}';
                }

                public String getJg() {
                    return jg;
                }

                public void setJg(String jg) {
                    this.jg = jg;
                }

                public String getJb() {
                    return jb;
                }

                public void setJb(String jb) {
                    this.jb = jb;
                }
            }

            public static class PrecipitationBean {
                private String jg;
                private String jf;

                @Override
                public String toString() {
                    return "PrecipitationBean{" +
                            "jg='" + jg + '\'' +
                            ", jf='" + jf + '\'' +
                            '}';
                }

                public String getJg() {
                    return jg;
                }

                public void setJg(String jg) {
                    this.jg = jg;
                }

                public String getJf() {
                    return jf;
                }

                public void setJf(String jf) {
                    this.jf = jf;
                }
            }
        }

        public static class PmBean {
            private String key;
            private int show_desc;


            private Pm25Bean pm25;
            private String dateTime;
            private String cityName;

            @Override
            public String toString() {
                return "PmBean{" +
                        "key='" + key + '\'' +
                        ", show_desc=" + show_desc +
                        ", pm25=" + pm25 +
                        ", dateTime='" + dateTime + '\'' +
                        ", cityName='" + cityName + '\'' +
                        '}';
            }

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public int getShow_desc() {
                return show_desc;
            }

            public void setShow_desc(int show_desc) {
                this.show_desc = show_desc;
            }

            public Pm25Bean getPm25() {
                return pm25;
            }

            public void setPm25(Pm25Bean pm25) {
                this.pm25 = pm25;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public static class Pm25Bean {
                private String curPm;
                private String pm25;
                private String pm10;
                private int level;
                private String quality;
                private String des;

                @Override
                public String toString() {
                    return "Pm25Bean{" +
                            "curPm='" + curPm + '\'' +
                            ", pm25='" + pm25 + '\'' +
                            ", pm10='" + pm10 + '\'' +
                            ", level=" + level +
                            ", quality='" + quality + '\'' +
                            ", des='" + des + '\'' +
                            '}';
                }

                public String getCurPm() {
                    return curPm;
                }

                public void setCurPm(String curPm) {
                    this.curPm = curPm;
                }

                public String getPm25() {
                    return pm25;
                }

                public void setPm25(String pm25) {
                    this.pm25 = pm25;
                }

                public String getPm10() {
                    return pm10;
                }

                public void setPm10(String pm10) {
                    this.pm10 = pm10;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public String getQuality() {
                    return quality;
                }

                public void setQuality(String quality) {
                    this.quality = quality;
                }

                public String getDes() {
                    return des;
                }

                public void setDes(String des) {
                    this.des = des;
                }
            }
        }

        public static class WeatherInfo {
            private String date;
            private InfoBean info;
            private String week;
            private String nongli;

            @Override
            public String toString() {
                return "WeatherInfo{" +
                        "date='" + date + '\'' +
                        ", info=" + info +
                        ", week='" + week + '\'' +
                        ", nongli='" + nongli + '\'' +
                        '}';
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public InfoBean getInfo() {
                return info;
            }

            public void setInfo(InfoBean info) {
                this.info = info;
            }

            public String getWeek() {
                return week;
            }

            public void setWeek(String week) {
                this.week = week;
            }

            public String getNongli() {
                return nongli;
            }

            public void setNongli(String nongli) {
                this.nongli = nongli;
            }

            public static class InfoBean {
                private List<String> day;
                private List<String> night;

                @Override
                public String toString() {
                    return "InfoBean{" +
                            "day=" + day +
                            ", night=" + night +
                            '}';
                }

                public List<String> getDay() {
                    return day;
                }

                public void setDay(List<String> day) {
                    this.day = day;
                }

                public List<String> getNight() {
                    return night;
                }

                public void setNight(List<String> night) {
                    this.night = night;
                }
            }
        }
    }
}

