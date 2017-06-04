package com.tmall.myredboy.bean.wyy;

import java.util.List;

/**
 * 新品上架数据
 */

public class NewProductsInfo {

    public String             message;
    public int                totalcount;
    public String             status;
    public List<ProductsBean> products;


    public static class ProductsBean {

        public float  commentCount;
        public String coverimg;
        public String createTime;
        public int    id;
        public int    marketprice;
        public String name;
        public float  sellprice;

    }
}
