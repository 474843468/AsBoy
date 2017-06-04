package com.tmall.myredboy.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单信息
 */

public class OrderInfo implements Serializable {

    /**
     * message : 订单列表查询成功
     * orderList : [{"createTime":"2016-05-02 18:10:09","orderId":"201605021810090315334","orderState":2,"productList":[{"amount":2,"coverimg":"product3/product_cover.jpg","extras":"选择颜色:中灰 选择尺码:M ","id":3,"name":"十月妈咪 OCTmami 孕妇装防辐射服孕妇装春夏银纤维吊带孕妇防辐射服内穿 褶裥款-中灰色 M","sellprice":450}],"totalPrice":900,"userId":2},{"address":"123","createTime":"2016-05-02 18:30:55","orderId":"201605021831120751585","orderState":2,"paymentWay":"zhifubao","productList":[{"amount":2,"coverimg":"product3/product_cover.jpg","extras":"选择颜色:褶裥款 选择尺码:M ","id":3,"name":"十月妈咪 OCTmami 孕妇装防辐射服孕妇装春夏银纤维吊带孕妇防辐射服内穿 褶裥款-中灰色 M","sellprice":450}],"sendTime":"sendTime","sendWay":"kuaidi","ticketContent":"invo","totalPrice":900,"userId":2},{"address":"123","createTime":"2016-05-02 18:32:47","orderId":"201605021832500035484","orderState":2,"paymentWay":"zhifubao","productList":[{"amount":2,"coverimg":"product3/product_cover.jpg","extras":"选择颜色:中灰,褶裥款,中灰色-选择尺码:M,L,XL","id":3,"name":"十月妈咪 OCTmami 孕妇装防辐射服孕妇装春夏银纤维吊带孕妇防辐射服内穿 褶裥款-中灰色 M","sellprice":450}],"sendTime":"sendTime","sendWay":"kuaidi","ticketContent":"invo","totalPrice":900,"userId":2}]
     * status : 200
     */

    public String              message;
    public String              status;
    public List<OrderListBean> orderList;

    public class OrderListBean implements Serializable {
        /**
         * createTime : 订单创建时间
         * orderId : 订单id
         * orderState : 订单状态取消：-1,途中：2；
         * productList : 订单商品信息 [{"amount":2,"coverimg":"product3/product_cover.jpg","extras":"选择颜色:中灰 选择尺码:M ","id":3,"name":"十月妈咪 OCTmami 孕妇装防辐射服孕妇装春夏银纤维吊带孕妇防辐射服内穿 褶裥款-中灰色 M","sellprice":450}]
         * totalPrice : 总价
         * userId : 2
         * address : 地址
         * paymentWay :支付方式
         * sendTime : sendTime
         * sendWay : kuaidi
         * ticketContent : invo
         */

        public String                createTime;
        public String                orderId;
        public int                   orderState;
        public double                totalPrice;
        public int                   userId;
        public String                address;
        public String                paymentWay;
        public String                sendTime;
        public String                sendWay;
        public String                ticketContent;
        public List<ProductListBean> productList;

        public class ProductListBean implements Serializable {
            /**
             * amount : 商品数量
             * coverimg :商品封面图片
             * extras : 商品附加信息
             * id : 商品id
             * name : 商品名称
             * sellprice : 商品价格
             */

            public int    amount;
            public String coverimg;
            public String extras;
            public int    id;
            public String name;
            public double sellprice;
        }
    }
}
