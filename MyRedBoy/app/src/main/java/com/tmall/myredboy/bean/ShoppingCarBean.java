package com.tmall.myredboy.bean;


import java.util.List;

public class ShoppingCarBean {


    /**
	* cart : [{"amount":1,"coverImg":"product4/product_cover.jpg","extra":"fbbb","productId":4,"productMarketprice":130,"productName":"十月妈咪防辐射束腹带 剖腹顺产两用收腹带 产后塑身","productPrice":99,"remindCount":283},{"amount":3,"coverImg":"product3/product_cover.jpg","extra":"fbbb","productId":3,"productMarketprice":601,"productName":"十月妈咪 OCTmami 孕妇装防辐射服孕妇装春夏银纤维吊带孕妇防辐射服内穿 褶裥款-中灰色 M","productPrice":450,"remindCount":283},{"amount":1,"coverImg":"product4/product_cover.jpg","extra":"bbb","productId":4,"productMarketprice":130,"productName":"十月妈咪防辐射束腹带 剖腹顺产两用收腹带 产后塑身","productPrice":99,"remindCount":283}]
	* discountMsg : 满300减100
	* discountPrice : 0
	* getScore : 1548
	* message : 购物车信息获取成功
	* status : 200
	* totalCount : 5
	* totalPrice : 1548.0
	*/

    public String discountMsg;
    public int            discountPrice;
    public int            getScore;
    public String         message;
    public String         status;
    public int            totalCount;
    public double         totalPrice;
    public List<CartBean> cart;

    public static class CartBean {
	   /**
	    * amount : 1
	    * coverImg : product4/product_cover.jpg
	    * extra : fbbb
	    * productId : 4
	    * productMarketprice : 130.0
	    * productName : 十月妈咪防辐射束腹带 剖腹顺产两用收腹带 产后塑身
	    * productPrice : 99.0
	    * remindCount : 283
	    */

	   public int amount;
	   public String coverImg;
	   public String extra;
	   public int    productId;
	   public double productMarketprice;
	   public String productName;
	   public double productPrice;
	   public int    remindCount;
    }
}
