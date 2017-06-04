package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 商品详情信息
 */
public class OrderItemInfo {

    /**
     * message : 订单详情查询成功
     * orderDetail : {"address":"zp\n12222\n陕西省西安市雁塔区","createTime":"2016-06-20 01:01:20","orderId":"201609220226140815110","orderState":2,"paymentWay":"支付宝","productList":[{"amount":1,"coverimg":"product3897/n1/10277559099/57247c8cN82021977.jpg","discountMsg":null,"extras":"选择颜色：:藏青色-选择尺码：:XXL","id":3897,"marketprice":null,"name":"简向左孕妇裤夏大码薄款托腹裤韩版纯棉春夏孕妇装长裤子孕妇打底裤 深卡其 XL","sellprice":96},{"amount":1,"coverimg":"product3897/n1/10277559099/57247c8cN82021977.jpg","discountMsg":null,"extras":"选择颜色：:荧光色-选择尺码：:XL","id":3897,"marketprice":null,"name":"简向左孕妇裤夏大码薄款托腹裤韩版纯棉春夏孕妇装长裤子孕妇打底裤 深卡其 XL","sellprice":96},{"amount":1,"coverimg":"product3895/n1/10194980024/56ebf083Nddf1b4d0.jpg","discountMsg":null,"extras":"选择颜色：:黑色-选择尺码：:3XL","id":3895,"marketprice":null,"name":"锦溪妈咪（送孕妇裤）孕妇夏新款孕妇蕾丝休闲孕妇套装t 恤+托腹裤子 粉红色 L","sellprice":129},{"amount":1,"coverimg":"product3898/n1/1118640380/53db5053N65358f84.jpg","discountMsg":null,"extras":null,"id":3898,"marketprice":null,"name":"十月名裳 孕妇装夏装 休闲孕妇运动套装孕妇t恤短袖上衣+孕妇七分裤孕妇裤打底裤托腹裤 白色-豹子头图案-热卖款 M-亲肤柔软店长推荐","sellprice":95},{"amount":1,"coverimg":"product3895/n1/10194980024/56ebf083Nddf1b4d0.jpg","discountMsg":null,"extras":"选择颜色：:白色-选择尺码：:XXL","id":3895,"marketprice":null,"name":"锦溪妈咪（送孕妇裤）孕妇夏新款孕妇蕾丝休闲孕妇套装t 恤+托腹裤子 粉红色 L","sellprice":129}],"sendDetail":"","sendTime":"只双休日、假日送货(工作日不送货)","sendWay":"邮政","ticketContent":"单位:资料","totalPrice":545,"userId":1395}
     * status : 200
     */

    public String message;
    public OrderDetailBean orderDetail;
    public String          status;

    public static class OrderDetailBean {
        /**
         * address : zp
         12222
         陕西省西安市雁塔区
         * createTime : 2016-06-20 01:01:20
         * orderId : 201609220226140815110
         * orderState : 2
         * paymentWay : 支付宝
         * productList : [{"amount":1,"coverimg":"product3897/n1/10277559099/57247c8cN82021977.jpg","discountMsg":null,"extras":"选择颜色：:藏青色-选择尺码：:XXL","id":3897,"marketprice":null,"name":"简向左孕妇裤夏大码薄款托腹裤韩版纯棉春夏孕妇装长裤子孕妇打底裤 深卡其 XL","sellprice":96},{"amount":1,"coverimg":"product3897/n1/10277559099/57247c8cN82021977.jpg","discountMsg":null,"extras":"选择颜色：:荧光色-选择尺码：:XL","id":3897,"marketprice":null,"name":"简向左孕妇裤夏大码薄款托腹裤韩版纯棉春夏孕妇装长裤子孕妇打底裤 深卡其 XL","sellprice":96},{"amount":1,"coverimg":"product3895/n1/10194980024/56ebf083Nddf1b4d0.jpg","discountMsg":null,"extras":"选择颜色：:黑色-选择尺码：:3XL","id":3895,"marketprice":null,"name":"锦溪妈咪（送孕妇裤）孕妇夏新款孕妇蕾丝休闲孕妇套装t 恤+托腹裤子 粉红色 L","sellprice":129},{"amount":1,"coverimg":"product3898/n1/1118640380/53db5053N65358f84.jpg","discountMsg":null,"extras":null,"id":3898,"marketprice":null,"name":"十月名裳 孕妇装夏装 休闲孕妇运动套装孕妇t恤短袖上衣+孕妇七分裤孕妇裤打底裤托腹裤 白色-豹子头图案-热卖款 M-亲肤柔软店长推荐","sellprice":95},{"amount":1,"coverimg":"product3895/n1/10194980024/56ebf083Nddf1b4d0.jpg","discountMsg":null,"extras":"选择颜色：:白色-选择尺码：:XXL","id":3895,"marketprice":null,"name":"锦溪妈咪（送孕妇裤）孕妇夏新款孕妇蕾丝休闲孕妇套装t 恤+托腹裤子 粉红色 L","sellprice":129}]
         * sendDetail :
         * sendTime : 只双休日、假日送货(工作日不送货)
         * sendWay : 邮政
         * ticketContent : 单位:资料
         * totalPrice : 545.0
         * userId : 1395
         */

        public String address;
        public String                createTime;
        public String                orderId;
        public int                   orderState;
        public String                paymentWay;
        public String                sendDetail;
        public String                sendTime;
        public String                sendWay;
        public String                ticketContent;
        public double                totalPrice;
        public int                   userId;
        public List<ProductListBean> productList;

        public static class ProductListBean {
            /**
             * amount : 1
             * coverimg : product3897/n1/10277559099/57247c8cN82021977.jpg
             * discountMsg : null
             * extras : 选择颜色：:藏青色-选择尺码：:XXL
             * id : 3897
             * marketprice : null
             * name : 简向左孕妇裤夏大码薄款托腹裤韩版纯棉春夏孕妇装长裤子孕妇打底裤 深卡其 XL
             * sellprice : 96.0
             */

            public int amount;
            public String coverimg;
            public Object discountMsg;
            public String extras;
            public int    id;
            public Object marketprice;
            public String name;
            public double sellprice;
        }
    }
}
