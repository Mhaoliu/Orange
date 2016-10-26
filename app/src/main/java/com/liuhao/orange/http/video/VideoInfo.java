package com.liuhao.orange.http.video;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhao on 2016/10/26.
 */
public class VideoInfo implements Parcelable {



    private int page;
    private int count;
    private int total;
    private List<VideosBean> videos;

    @Override
    public String toString() {
        return "VideoInfo{" +
                "page=" + page +
                ", count=" + count +
                ", total=" + total +
                ", videos=" + videos +
                '}';
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<VideosBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosBean> videos) {
        this.videos = videos;
    }

    public static class VideosBean implements Parcelable {
        private int view_count;
        private int up_count;
        private int down_count;
        private String id;
        private String title;
        private String thumbnail;
        private UserBean user;
        private String published;
        private String category;
        private String link;
        private String quality;
        private String seconds;

        @Override
        public String toString() {
            return "VideosBean{" +
                    "view_count=" + view_count +
                    ", up_count=" + up_count +
                    ", down_count=" + down_count +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", thumbnail='" + thumbnail + '\'' +
                    ", user=" + user +
                    ", published='" + published + '\'' +
                    ", category='" + category + '\'' +
                    ", link='" + link + '\'' +
                    ", quality='" + quality + '\'' +
                    ", seconds='" + seconds + '\'' +
                    '}';
        }

        public int getView_count() {
            return view_count;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }

        public int getUp_count() {
            return up_count;
        }

        public void setUp_count(int up_count) {
            this.up_count = up_count;
        }

        public int getDown_count() {
            return down_count;
        }

        public void setDown_count(int down_count) {
            this.down_count = down_count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getSeconds() {
            return seconds;
        }

        public void setSeconds(String seconds) {
            this.seconds = seconds;
        }

        public static class UserBean implements Parcelable {
            private int user_id;
            private String user_name;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.user_id);
                dest.writeString(this.user_name);
            }

            public UserBean() {
            }

            protected UserBean(Parcel in) {
                this.user_id = in.readInt();
                this.user_name = in.readString();
            }

            public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
                @Override
                public UserBean createFromParcel(Parcel source) {
                    return new UserBean(source);
                }

                @Override
                public UserBean[] newArray(int size) {
                    return new UserBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.view_count);
            dest.writeInt(this.up_count);
            dest.writeInt(this.down_count);
            dest.writeString(this.id);
            dest.writeString(this.title);
            dest.writeString(this.thumbnail);
            dest.writeParcelable(this.user, flags);
            dest.writeString(this.published);
            dest.writeString(this.category);
            dest.writeString(this.link);
            dest.writeString(this.quality);
            dest.writeString(this.seconds);
        }

        public VideosBean() {
        }

        protected VideosBean(Parcel in) {
            this.view_count = in.readInt();
            this.up_count = in.readInt();
            this.down_count = in.readInt();
            this.id = in.readString();
            this.title = in.readString();
            this.thumbnail = in.readString();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
            this.published = in.readString();
            this.category = in.readString();
            this.link = in.readString();
            this.quality = in.readString();
            this.seconds = in.readString();
        }

        public static final Creator<VideosBean> CREATOR = new Creator<VideosBean>() {
            @Override
            public VideosBean createFromParcel(Parcel source) {
                return new VideosBean(source);
            }

            @Override
            public VideosBean[] newArray(int size) {
                return new VideosBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.page);
        dest.writeInt(this.count);
        dest.writeInt(this.total);
        dest.writeList(this.videos);
    }

    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        this.page = in.readInt();
        this.count = in.readInt();
        this.total = in.readInt();
        this.videos = new ArrayList<VideosBean>();
        in.readList(this.videos, VideosBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
