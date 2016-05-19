/**
 *
 */
package com.cn.coachs.model.myaccount;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author kuangtiecheng
 */
public class BeanFans implements Parcelable {
    public String headImgUrl;
    public String memberName;
    public String name;
    public String sex;
    public String age;
    public String contactid;
    public String imgUrl;
    public String nickname;
    public String height;
    public String weight;
    public int remainServeTime;
    public long paitentID;
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public long getPaitentID() {
        return paitentID;
    }

    public void setPaitentID(long paitentID) {
        this.paitentID = paitentID;
    }

    public int getRemainServeTime() {
        return remainServeTime;
    }

    public void setRemainServeTime(int remainServeTime) {
        this.remainServeTime = remainServeTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "BeanFans [headImgUrl=" + headImgUrl + ", memberName="
                + memberName + ", name=" + name + ", sex=" + sex + ", age="
                + age + ", contactid=" + contactid + ", imgUrl=" + imgUrl
                + ", nickname=" + nickname + ", height=" + height + ", weight="
                + weight + ", remainServeTime=" + remainServeTime
                + ", paitentID=" + paitentID + "]";
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(headImgUrl);
        dest.writeString(memberName);
        dest.writeString(sex);
        dest.writeString(contactid);
        dest.writeString(imgUrl);
        dest.writeString(nickname);
        dest.writeString(age);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeInt(remainServeTime);
        dest.writeLong(paitentID);
        dest.writeString(name);
    }

    public final static Parcelable.Creator<BeanFans> CREATOR = new Parcelable.Creator<BeanFans>() {

        @Override
        public BeanFans createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            BeanFans msa = new BeanFans();
            msa.headImgUrl = source.readString();
            msa.memberName = source.readString();
            msa.sex = source.readString();
            msa.contactid = source.readString();
            msa.nickname = source.readString();
            msa.imgUrl = source.readString();
            msa.age = source.readString();
            msa.height = source.readString();
            msa.weight = source.readString();
            msa.remainServeTime = source.readInt();
            msa.paitentID = source.readLong();
            msa.name = source.readString();
            return msa;
        }

        @Override
        public BeanFans[] newArray(int size) {
            // TODO Auto-generated method stub
            return new BeanFans[size];
        }
    };

}
