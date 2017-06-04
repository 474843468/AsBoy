package com.tmall.myredboy.bean.wyy;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class SpecialProductsInfo {
    public ArrayList<ProductData> product;

    public class ProductData {
        public String coverimg;
        public String name;
        public int    commentCount;
        public int    id;
        public double sellprice;
        public double marketprice;
    }
}
