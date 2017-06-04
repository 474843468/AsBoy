package com.tmall.myredboy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 地址信息
 */

public class AddressInfo {

    /**
     * address : [{"address":"北京市朝阳区","area":"建国路soho大厦","id":13,"isDefault":0,"name":"testuser","telphone":"18938939113","userId":2}]
     * message : 查询地址列表成功
     * status : 200
     */
    public String            message;
    public String            status;
    public List<AddressBean> address;

    public static class AddressBean implements Serializable {
        /**
         * address：省+市+区;
         * area：街道地址;
         * id：地址id;
         * isDefault：是否是默认地址（0为不是；1为是）;
         * name：收货人;
         * telephone：收货人电话;
         * userId：地址所属的用户id;
         */
        public String address;
        public String area;
        public int    id;
        public int    isDefault;
        public String name;
        public String telphone;
        public int    userId;
    }
}
