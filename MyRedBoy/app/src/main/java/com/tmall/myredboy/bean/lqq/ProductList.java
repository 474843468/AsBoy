package com.tmall.myredboy.bean.lqq;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ProductList {

    public String message;
    public String status;
    public int totalcount;
    public ArrayList<Product> product;
    public ArrayList<Categories> categories;

    public class Product {
        public String commentCount; // 18,
        public String coverimg; // public String n1/10140926443/56d0110cN3276ff92.jpgpublic String ,
        public String id;
        public String marketprice; // 402.0, // 4703,
        public String name; // public String 曲美防辐射服孕妇装银纤维防辐射肚兜吊带四季内穿 6026全银离子纤维吊带 Lpublic String ,
        public String sellprice; // 358.0
    }
    public class Categories {
        public String desc;
        public String iconUrl;
        public String id;
        public int isLeaf;
        public String name;
        public String parentId;
    }
}
