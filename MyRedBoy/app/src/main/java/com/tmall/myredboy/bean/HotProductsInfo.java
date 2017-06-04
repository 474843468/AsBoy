package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 热门单品数据
 */

public class HotProductsInfo {

    public String             message;
    public int                totalcount;
    public String             status;
    public List<ProductsBean> product;

    public static class ProductsBean {

        public int    commentCount;
        public String coverimg;
        public int    id;
        public float  marketprice;
        public String name;
        public float  sellprice;
    }
}
