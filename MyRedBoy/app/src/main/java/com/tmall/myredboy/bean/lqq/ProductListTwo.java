package com.tmall.myredboy.bean.lqq;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ProductListTwo {

    public String message;
    public String status;
    public ArrayList<Categories> categories;

    public class Categories {
        public String id;//8,
        public int isLeaf;//1,
        public String name;//孕妈装",
        public String parentId;//22
    }


}
