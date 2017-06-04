package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 收藏夹数据信息
 */

public class FavoritesBean {

    /**
     * collections : 用户收藏的产品列表[{"id":11,"product":{"commentCount":335,"coverimg":"product1/product_cover.jpg","id":1,"marketprice":366,"name":"十月妈咪防辐射服 背心款金属丝孕妇防辐射衣服 粉红 L","sellprice":299},"userId":2},{"id":12,"product":{"commentCount":121,"coverimg":"product2/product_cover.jpg","id":2,"marketprice":400,"name":"十月妈咪防辐射服孕妇装 金属丝孕妇防辐射衣服防辐射马甲秋春 灰色 L","sellprice":279},"userId":2}]
     * message : 收藏查询成功
     * status : 200
     * totalcount : 收藏总数
     */

    public String                message;
    public int                   status;
    public int                   totalcount;
    public List<CollectionsBean> collections;

    public class CollectionsBean {
        /**
         * id : 11
         * product : {"commentCount":335,"coverimg":"product1/product_cover.jpg","id":1,"marketprice":366,"name":"十月妈咪防辐射服 背心款金属丝孕妇防辐射衣服 粉红 L","sellprice":299}
         * userId : 2
         */

        public int         id;
        public ProductBean product;
        public int         userId;

    }

    public class ProductBean {
        /**
         * commentCount : 评价数量
         * coverimg :封面图像
         * id : 商品id
         * marketprice :市场价
         * name :商品名称
         * sellprice : 售价
         */

        public int    commentCount;
        public String coverimg;
        public int    id;
        public int    marketprice;
        public String name;
        public String des;
        public float  sellprice;
    }
}
