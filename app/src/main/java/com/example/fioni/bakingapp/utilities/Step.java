package com.example.fioni.bakingapp.utilities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fioni on 9/2/2017.
 */

public class Step implements Parcelable {
    private String r_id;
    private String id;
    private String short_desc;
    private String desc;
    private String video_url;
    private String thumb_url;

    public Step() {
    }

    public Step(String r_id, String id, String short_desc, String desc, String video_url, String thumb_url) {
        this.r_id = r_id;
        this.id = id;
        this.short_desc = short_desc;
        this.desc = desc;
        this.video_url = video_url;
        this.thumb_url = thumb_url;
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getR_id() {
        return r_id;
    }

    public void setR_id(String r_id) {
        this.r_id = r_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShort_desc() {
        return short_desc;
    }

    public void setShort_desc(String short_desc) {
        this.short_desc = short_desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(r_id);
        dest.writeString(id);
        dest.writeString(short_desc);
        dest.writeString(desc);
        dest.writeString(video_url);
        dest.writeString(thumb_url);

    }

    protected Step(Parcel in) {
        r_id = in.readString();
        id = in.readString();
        short_desc = in.readString();
        desc = in.readString();
        video_url = in.readString();
        thumb_url = in.readString();

    }
}
