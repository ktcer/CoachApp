package com.cn.coachs.model.personinfo;


public class BeanPersonInfo {
    private int result;
    private String detail;
    private long id;
    private String txlj;
    private String name;
    private byte ifModify;

    public byte getIfModify() {
        return ifModify;
    }

    public void setIfModify(byte ifModify) {
        this.ifModify = ifModify;
    }

    public String getTxlj() {
        return txlj;
    }

    public void setTxlj(String txlj) {
        this.txlj = txlj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        detail = detail;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

