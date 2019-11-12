package com.xianzhi.map.gaode.bean;

import java.io.Serializable;

/**
 * @author LiMing
 * @Demo class GaoDeTrackBean
 * @Description TODO
 * @date 2019-11-06 11:36
 */
public class GaoDeTrackBean implements Serializable {

    /**
     * data : {"name":"测试","sid":83498}
     * errcode : 10000
     * errdetail : null
     * errmsg : OK
     */

    private DataBean data;
    private int errcode;
    private Object errdetail;
    private String errmsg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public Object getErrdetail() {
        return errdetail;
    }

    public void setErrdetail(Object errdetail) {
        this.errdetail = errdetail;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public static class DataBean {
        /**
         * name : 测试
         * sid : 83498
         */

        private String name;
        private int sid;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }
    }
}
