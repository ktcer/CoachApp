package com.cn.coachs.model.healthpost;


public class BeanHealthPostOrder {
    //	private static final long serialVersionUID = -7060210544600464481L;
    private long travelID;
    private String title;
    private String cover;
    private float finalPrice;
    private String tag;
    private String activeTime;
    private byte state;
    private String ddh;
    private float money;
    private String detail;


    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public long getTravelID() {
        return travelID;
    }

    public void setTravelID(long travelID) {
        this.travelID = travelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
//	public static long getSerialversionuid() {
//		return serialVersionUID;
//	}


}
