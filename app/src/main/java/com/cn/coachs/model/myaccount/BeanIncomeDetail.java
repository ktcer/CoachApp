/**
 *
 */
package com.cn.coachs.model.myaccount;

/**
 * @author kuangtiecheng
 */
public class BeanIncomeDetail {
    public String item;
    public String detail;
    public float amount;
    public String createtime;
    public String inAccount;
    public String outAccount;
    public float type;//true 来款 ，白，false 支出，灰
    public int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getInAccount() {
        return inAccount;
    }

    public void setInAccount(String inAccount) {
        this.inAccount = inAccount;
    }

    public String getOutAccount() {
        return outAccount;
    }

    public void setOutAccount(String outAccount) {
        this.outAccount = outAccount;
    }


    public float getType() {
        return type;
    }

    public void setType(float type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BeanIncomeDetail [item=" + item + ", detail=" + detail
                + ", amount=" + amount + ", createtime=" + createtime + ",state=" + state
                + ", inAccount=" + inAccount + ", outAccount=" + outAccount
                + ", type=" + type + "]";
    }

}
