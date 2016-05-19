package com.cn.coachs.model.nurse;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @parameter
 * @return
 */
public class BeanEditDiaryNode implements Parcelable {
    public String fansId;//即为后台的patientID
    public Long id;
    public String content;
    public int day;
    public int week;
    public String time;

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BeanEditDiaryNode [fansId=" + fansId + ", id=" + id
                + ", content=" + content + ", day=" + day + ", week=" + week
                + ", time=" + time + "]";
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // TODO Auto-generated method stub
        parcel.writeString(fansId);
        parcel.writeLong(id);
        parcel.writeString(content);
        parcel.writeInt(day);
        parcel.writeInt(week);
        parcel.writeString(time);
    }

    public final static Parcelable.Creator<BeanEditDiaryNode> CREATOR = new Parcelable.Creator<BeanEditDiaryNode>() {

        @Override
        public BeanEditDiaryNode createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            BeanEditDiaryNode msa = new BeanEditDiaryNode();
            msa.fansId = source.readString();
            msa.id = source.readLong();
            msa.content = source.readString();
            msa.day = source.readInt();
            msa.week = source.readInt();
            msa.time = source.readString();
            return msa;
        }

        @Override
        public BeanEditDiaryNode[] newArray(int size) {
            // TODO Auto-generated method stub
            return new BeanEditDiaryNode[size];
        }
    };
}
