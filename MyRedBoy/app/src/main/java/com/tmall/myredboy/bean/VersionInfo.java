package com.tmall.myredboy.bean;

/**
 * Created by Administrator on 2016/11/17 0017.
 */

public class VersionInfo {
    /**
     * message : 版本检测成功
     * status : 200
     * version : {"downloadUrl":"http://103.214.140.26:8080/Shop/RedBoy.apk","versionCode":"2.05","versionMessage":"该版本修复了若干个Bug"}
     */

    public String message;
    public int         status;
    public VersionBean version;

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

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class VersionBean {
        /**
         * downloadUrl : http://103.214.140.26:8080/Shop/RedBoy.apk
         * versionCode : 2.05
         * versionMessage : 该版本修复了若干个Bug
         */

        public String downloadUrl;
        public String versionCode;
        public String versionMessage;

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionMessage() {
            return versionMessage;
        }

        public void setVersionMessage(String versionMessage) {
            this.versionMessage = versionMessage;
        }
    }
}
