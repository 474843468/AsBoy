package com.tmall.myredboy.bean;

import java.util.List;

//{}---定义类
// []---集合，用ArrayList
//什么符号都没有----基本的数据类型
public class HomeViewPagerInfo {

    public String           message;
    public String           status;
    public List<HomeImages> home_images;

    public static class HomeImages {
        public int    id;
        public String path;
    }
}
