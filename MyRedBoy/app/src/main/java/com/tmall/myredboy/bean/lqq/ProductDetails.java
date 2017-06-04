package com.tmall.myredboy.bean.lqq;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.id;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ProductDetails {
    public String message;
    public String status;

    public Product product;

    public class Product {
        public ArrayList<String> bigImgs;
        public String clickCount;
        public int commentCount;
        public String count;
        public String coverimg;
        public String createTime;
        public String desc;
        public String discountMsg;
        public boolean hasCollected;
        public String id;
        public String marketprice;
        public String name;
        public String score;
        public String sellCount;
        public String sellprice;
        public ArrayList<String> smallImgs;
        public ArrayList<Extras> extras;

    }

    public class Extras {
        public String name;
        public ArrayList<String> value;
    }

}
