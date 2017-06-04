package com.tmall.myredboy.bean.lqq;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/17.
 */

public class ProductListOne {

    public String message;  //分类查询成功
    public String status;
    public ArrayList<Categories> categories;

    public class Categories {
        public String desc;
        public String iconUrl;
        public String id;
        public int isLeaf;
        public String name;
        public String parentId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
