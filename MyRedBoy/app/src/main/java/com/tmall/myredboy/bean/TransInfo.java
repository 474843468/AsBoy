package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 物流信息
 */

public class TransInfo {

    /**
     * content : {"code":"SF00101010992","code2":"102039938484","company":"顺丰快递","orderDetail":["2016-4-20 9：39：40 到达西安分流中心","2016-4-20 9：39：40 到达西安雁塔区分流中心","2016-4-20 9：39：40 到达丈八西社区"],"type":"快递"}
     * message : 物流信息查询成功
     * status : 200
     */

    public ContentBean content;
    public String      message;
    public int         status;

    public static class ContentBean {
        /**
         * code : 物流编码
         * code2 : 运单号码
         * company : 顺丰快递
         * orderDetail : ["2016-4-20 9：39：40 到达西安分流中心","2016-4-20 9：39：40 到达西安雁塔区分流中心","2016-4-20 9：39：40 到达丈八西社区"]
         * type : 快递
         */

        public String       code;
        public String       code2;
        public String       company;
        public String       type;
        public List<String> orderDetail;
    }
}
