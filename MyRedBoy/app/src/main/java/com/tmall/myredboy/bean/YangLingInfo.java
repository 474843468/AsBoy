package com.tmall.myredboy.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16 0016.
 */

public class YangLingInfo {

    /**
     * message : 查询帮助列表成功
     * status : 200
     * helpList : [{"name":"购物指南","URL":"gwzn.jsp"},{"name":"售后方式","URL":"shfw.jsp"},{"name":"配送方式","URL":"pszn.jsp"}]
     */

    public String message;
    public int                status;
    public List<HelpListBean> helpList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<HelpListBean> getHelpList() {
        return helpList;
    }

    public void setHelpList(List<HelpListBean> helpList) {
        this.helpList = helpList;
    }

    public static class HelpListBean {
        /**
         * name : 购物指南
         * URL : gwzn.jsp
         */

        public String name;
        public String URL;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
