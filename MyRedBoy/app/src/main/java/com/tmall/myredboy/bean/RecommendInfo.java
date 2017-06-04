package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 推荐品牌数据
 */

public class RecommendInfo {

    public String          message;
    public int             status;
    public List<BrandBean> brand;

    public class BrandBean {

        public String             title; //品牌分类标题名称
        public List<ChildrenBean> children;


        public class ChildrenBean {
            public String brandUrl; //品牌图片
            public int    id; //品牌id，可根据此id查询该品牌下所有的商品信息
            public String name;//品牌名称
            public int    parentId;  //品牌分类标题
        }
    }


}

