package com.tmall.myredboy.bean.wyy;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class BrandProductsInfo {
    public String                message;
    public String                status;
    public ArrayList<ProdutData> product;

    public class ProdutData {
        public String coverimg;
        public String name;
        public int    commentCount;
        public int    id;
        public double marketprice;
        public double sellprice;
    }

}
