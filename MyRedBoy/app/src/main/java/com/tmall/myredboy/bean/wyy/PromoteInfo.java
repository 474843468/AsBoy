package com.tmall.myredboy.bean.wyy;

import java.util.List;

/**
 * 促销快报数据
 */

public class PromoteInfo {

    public String            message;
    public String            status;
    public List<PromoteBean> promote;

    public class PromoteBean {

        public String promoteCoverImg; //专题图片
        public String promoteId;
        public String promoteName; //专题名称
        public String time;    //专题时间
    }
}
