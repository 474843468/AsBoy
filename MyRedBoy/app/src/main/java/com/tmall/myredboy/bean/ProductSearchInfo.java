package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 商品搜索结果数据
 */
public class ProductSearchInfo {
    public int               totalcount;   //结果总数
    public String            message;
    public String            status;
    public List<ProductBean> product;

    public static class ProductBean {

        public int    commentCount;//评价数量
        public String coverimg;   //封面图像
        public int    id;    //商品id
        public float  marketprice; //市场价
        public String name;   //商品名称
        public float  sellprice; //商城售价

    }
}
