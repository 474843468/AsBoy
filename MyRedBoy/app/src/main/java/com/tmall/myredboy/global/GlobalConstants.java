package com.tmall.myredboy.global;

/**
 * 全局常量
 */

public interface GlobalConstants {
    //    http://103.214.140.26:8080/Shop/


//    public static final String URL_NET_PREFIX = "http://103.214.140.26:8080/Shop/";
//    public static final String URL_NET_IMAGE  = "http://103.214.140.26:8080/Shop/upload/";
//    public static final String URL_NET_PREFIX = "http://192.168.11.214:8080/Shop/";
//    public static final String URL_NET_IMAGE  = "http://192.168.11.214:8080/Shop/upload/";
    public static final String URL_NET_PREFIX = "http://192.168.191.1:8080/Shop/";
    public static final String URL_NET_IMAGE  = "http://192.168.191.1:8080/Shop/upload/";

    /**
     * 访问地址前缀103.214.140.26
     */
//    public static final String URL_PREFIX     = "http://192.168.11.214:8080/Shop/";
    public static final String URL_PREFIX     = "http://192.168.191.1:8080/Shop/";

    /**
     * 图片访问地址前缀
     */
//    public static final String URL_IMAGE = "http://192.168.11.214:8080/Shop/upload/";
    public static final String URL_IMAGE = "http://192.168.191.1:8080/Shop/upload/";

    /**
     * 登录用户id
     */
    public static final String PREF_USER_ID = "user_id";

    /**
     * 购物车的地址
     */
    public static final String SHOPPING_CAR = "product/cart_getCartList.html";


    /**
     * 商品分类的地址
     */
    public static final String PRODUCT_FIND_CATORIES = "product/product_findCatories.html";

    /**
     * 商品列表的地址
     */
    public static final String PRODUCT_LIST   = "product/product_getProductsByCategory.html";

    /**
     * 商品详情的地址
     */
    public static final String PRODUCT_DETAIL = "product/product_getProductById.html";

    /**
     * 加入购物车的地址
     */
    public static final String PRODUCT_ADD_TO_SHPPING_CAR = "product/cart_add.html";

    /**
     * 添加到收藏夹的地址
     */
    public static final String PRODUCT_ADD_TO_COLLECTION = "productCollection/productCollection_save.html";

    /**
     * 取消收藏的地址
     */
    public static final String PRODUCT_DELETE_COLLECTION = "productCollection/productCollection_delCollection.html";

    /**
     * 解析数据Json
     */
    public static final String DATA = "resoult";


    /**
     * 购物车删除
     * */
    public static final String DELETE = "product/cart_delete.html";


    /**
     * 下拉刷新日期
     */
    public static final String PREF_LAST_UPDATE_TIME = "last_update_time";

    /**
     * 清算中心提交数据
     * */

    public static final String URL_COMIT = "product/order_submitOrder.html";


}
