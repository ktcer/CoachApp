package com.cn.coachs.model.nurse;

public class BeanServeTime {
    /**
     * 时长
     */
    private String time;
    /**
     * 价格
     */
    private String price;
    /**
     * 服务id
     */
    private long servetimeid;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getServetimeid() {
        return servetimeid;
    }

    public void setServetimeid(long servetimeid) {
        this.servetimeid = servetimeid;
    }


}
