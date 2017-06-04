package com.tmall.myredboy.bean;

import java.util.List;

/**
 * 限时抢购页面数据
 */

public class LimitTimeInfo {
    public int               totalcount; //一共有多少条数据
    public String            message;
    public String            status;
    public List<ProductBean> products;


    public static class ProductBean {
        public int    commentCount;//评价数量
        public String coverimg; //封面图像
        public String empireTime; //限时抢购截止时间
        public int    id;            //商品id
        public double marketprice; //市场价
        public String name;   //商品名称
        public double sellprice;  //售价
    }
}
