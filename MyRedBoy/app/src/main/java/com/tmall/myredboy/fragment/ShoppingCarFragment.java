package com.tmall.myredboy.fragment;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.MainActivity;
import com.tmall.myredboy.activity.lqq.GoodsDetailActivity;
import com.tmall.myredboy.activity.zl.AccountCenterActivity;
import com.tmall.myredboy.bean.CartInfoUpdateEvent;
import com.tmall.myredboy.bean.ShoppingCarBean;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.utils.PrefUtils;
import com.tmall.myredboy.utils.Xutils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import more_activity.AccountActivity;

import static com.tmall.myredboy.global.GlobalConstants.PREF_USER_ID;

public class ShoppingCarFragment extends BaseFragment {

    private Button   btnCount;
    private Button   btnEdit;
    private TextView etYH;
    private ListView lvListView;
    private static final int     TYPE_NORMAL = 0;
    private static final int     TYPE_TITLE1 = 1;
    private static final int     TYPE_TITLE2 = 2;
    private              boolean isEdit      = false;
    private MyAdapter                      mAdapter;
    private String                         userID;
    private int                            totalcount;
    private ShoppingCarBean                bean;
    //判断是否有过期商品(两种布局)
    private boolean                        isDoubble;
    private List<ShoppingCarBean.CartBean> cart;


    private Handler mHandler = null;
    private List<ShoppingCarBean.CartBean> list2;
    private List<ShoppingCarBean.CartBean> list1;
    private FrameLayout                    kview;
    private FrameLayout                    yhView;
    private FrameLayout                    jzView;
    private Button                         btnEdit1;
    private Button                         btnLCount;
    private View                           loginedView;
    private String                         url;

    /**
     * 未解决问题 :
     * 1.条目点击
     * 2.删除
     * 3.尺码/颜色
     * 4.购物车登录之后返回的问题
     * 5.图片
     */
    @Override
    public View initView() {


        list1 = null;
        list2 = null;
        //totalcount = 0;
        //获取so中的用户id, 判断用户是否登录
        userID = PrefUtils.getString(activity, PREF_USER_ID, "");

        if ("".equals(userID)) {
            //用户还没有登录
            View view = View.inflate(getContext(), R.layout.shopping_car3, null);
            Button login = (Button) view.findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳进登录界面
                    startActivity(new Intent(activity, AccountActivity.class));
                }
            });
            return view;
        } else {
            //方位服务获取购物车信息

            loginedView = View.inflate(activity, R.layout.shopping_car2, null);
            //SystemClock.sleep(1500);

            kview = (FrameLayout) loginedView.findViewById(R.id.flK);
            //有货
            yhView = (FrameLayout) loginedView.findViewById(R.id.flYH);
            jzView = (FrameLayout) loginedView.findViewById(R.id.JZ);
            btnEdit1 = (Button) loginedView.findViewById(R.id.btnEdit);
            btnLCount = (Button) loginedView.findViewById(R.id.btnLCount);
            loginedView.findViewById(R.id.goShop).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进入商品页面
                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.putExtra("data",0);
                    startActivity(intent);
                }
            });
            jzView.setVisibility(View.VISIBLE);
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.configHttpCacheSize(0);
            url = GlobalConstants.URL_PREFIX + GlobalConstants.SHOPPING_CAR + "?uId=1395";
            httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String json = responseInfo.result;
                    Gson gson = new Gson();
                    bean = gson.fromJson(json, ShoppingCarBean.class);
                    totalcount = bean.totalCount;
                    System.out.print(totalcount + "");

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (totalcount == 0) {
                                jzView.setVisibility(View.GONE);

                                //YHView.setVisibility(View.GONE);
                                kview.setVisibility(View.VISIBLE);

                            } else {
                                get();
                                btnEdit.setVisibility(View.VISIBLE);
                                btnLCount.setVisibility(View.VISIBLE);
                                kview.setVisibility(View.GONE);
                                jzView.setVisibility(View.GONE);
                                yhView.setVisibility(View.VISIBLE);
                                //JZView.setVisibility(View.GONE);

                            }

                        }
                    });

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    System.out.print("网络访问失败" + e);
                    e.printStackTrace();
                }
            });

            initeViewCar(loginedView);
            return loginedView;
        }
    }

    public void initeViewCar(View view) {


        //初始化控件
        btnCount = (Button) view.findViewById(R.id.btnLCount);
        btnEdit = (Button) view.findViewById(R.id.btnEdit);
        etYH = (TextView) view.findViewById(R.id.etYH);
        lvListView = (ListView) view.findViewById(R.id.lvList);

        btnCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入结算中心页面
                startActivity(new Intent(getContext(), AccountCenterActivity.class));
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = !isEdit;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void get() {
        if ("".equals(userID)) {
            return;
        }
        if (totalcount == 0) {
            return;
        }
        //判断是否有优惠数据有就进行数据填充

        list2 = new ArrayList<ShoppingCarBean.CartBean>();
        list1 = new ArrayList<ShoppingCarBean.CartBean>();
        //2.对数据是否过期进行判断
        cart = bean.cart;
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).remindCount == 0) {
                isDoubble = true;

                list2.add(cart.get(i));
            } else {

                list1.add(cart.get(i));   //有货

            }
        }


        //加脚
        View view = View.inflate(activity, R.layout.shopping_car_foot, null);
        lvListView.addFooterView(view, null, false);
        //3.给listview设置adapter
        mAdapter = new MyAdapter();
        lvListView.setAdapter(mAdapter);

        //listview的条目点击事件
        lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入商品详情界面
                if (isDoubble) {
                    if (position == 0 || position == list1.size() + 1) {
                        return;
                    } else {
                        int productId = cart.get(position).productId;
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("id", productId + "");
                        //ToastUtils.show(activity, productId+);
                        startActivity(intent);
                    }
                } else {
                    if (position == 0) {
                        return;
                    } else {
                        int productId = cart.get(position - 1).productId;
                        Intent intent = new Intent(activity, GoodsDetailActivity.class);
                        intent.putExtra("id", productId + "");
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public void initData() {

    }

    //数据适配器
    //使用复杂listview
    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            if (isDoubble) {
                return cart.size() + 2;
            } else {
                return cart.size() + 1;
            }
        }

        @Override
        public ShoppingCarBean.CartBean getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_TITLE1:
                    //第一种头布局

                    TitleHolder1 holder = null;
                    if (convertView == null) {
                        holder = new TitleHolder1();
                        convertView = View.inflate(getContext(), R.layout.shoppingcar_title1, null);
                        holder.count = (TextView) convertView.findViewById(R.id.carTitleCount);
                        holder.price = (TextView) convertView.findViewById(R.id.TitlePrice);
                        holder.tvScore = (TextView) convertView.findViewById(R.id.score);
                        convertView.setTag(holder);
                    } else {
                        holder = (TitleHolder1) convertView.getTag();
                    }
                    holder.tvScore.setText(bean.getScore + "分");
                    holder.price.setText("￥：" + bean.totalPrice + "元");
                    holder.count.setText(bean.totalCount + "件");

                    break;

                case TYPE_TITLE2:
                    //第二种头布局
                    TitleHolder2 holder2 = null;
                    if (convertView == null) {
                        holder2 = new TitleHolder2();
                        convertView = View.inflate(getContext(), R.layout.shoppingcar_title2, null);
                        holder2.tvTitle = (TextView) convertView.findViewById(R.id.Title2);
                        convertView.setTag(holder2);
                    } else {
                        holder2 = (TitleHolder2) convertView.getTag();
                    }
                    holder2.tvTitle.setText("已过期商品");
                    break;
                case TYPE_NORMAL:
                    //普通布局
                    ViewHolder holder1 = null;
                    if (convertView == null) {
                        holder1 = new ViewHolder();
                        convertView = View.inflate(getContext(), R.layout.shoppingcar_listitem, null);
                        holder1.color = (TextView) convertView.findViewById(R.id.tvColor);
                        holder1.count = (TextView) convertView.findViewById(R.id.tvAll);
                        holder1.name = (TextView) convertView.findViewById(R.id.tvName);
                        holder1.num = (TextView) convertView.findViewById(R.id.tvNum);
                        holder1.size = (TextView) convertView.findViewById(R.id.tvSize);
                        holder1.picture = (ImageView) convertView.findViewById(R.id.ivSPPitcure);
                        holder1.price = (TextView) convertView.findViewById(R.id.tvPrice);
                        holder1.rl = (RelativeLayout) convertView.findViewById(R.id.rlDelete);
                        //根据编辑按钮的选定状态判断设置那个图片
                        holder1.delete = (ImageView) convertView.findViewById(R.id.ivDelete);
                        convertView.setTag(holder1);
                    } else {
                        holder1 = (ViewHolder) convertView.getTag();
                    }
                    //获取对象
                    final ShoppingCarBean.CartBean cartBean = list1.get(position - 1);
                    holder1.name.setText(cartBean.productName);
                    holder1.num.setText("数量: " + cartBean.amount);
                    //接口文档中商品尺码未找到
                    //颜色未找到
                    //holder1.color.setText(cartBean.);
                    holder1.price.setText("价格: " + cartBean.productPrice);
                    holder1.count.setText("小计: " + cartBean.productPrice * cartBean.amount);


                    //使用工具类设置图片
                    Xutils.setIv(activity, holder1.picture, GlobalConstants.URL_IMAGE + cartBean.coverImg);


                    //
                    holder1.delete.setImageResource(isEdit ? R.drawable.ico_delete : R.drawable.arrow_right_grey);
                    holder1.rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //将集合放空
                            HttpUtils utils = new HttpUtils();
                            RequestParams params = new RequestParams();
                            params.addQueryStringParameter("uId", userID);
                            params.addQueryStringParameter("productId", String.valueOf(cartBean.productId));
                            if (cartBean.extra != null && !"".equals(cartBean.extra)) {
                                params.addQueryStringParameter("extraMsg", cartBean.extra);
                            }


                            utils.send(HttpRequest.HttpMethod.POST, GlobalConstants.URL_PREFIX + GlobalConstants.DELETE, params, new RequestCallBack<String>() {


                                        @Override
                                        public void onSuccess(ResponseInfo<String> responseInfo) {
                                            judge(position);
                                            mAdapter.notifyDataSetChanged();
                                            initView2();
                                            EventBus.getDefault().post(new CartInfoUpdateEvent(true));
                                        }

                                        @Override
                                        public void onFailure(HttpException e, String s) {
                                            //System.out.print("失败");
                                            e.printStackTrace();
                                        }
                                    }
                            );
                        }
                    });


                    break;

            }


            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            if (isDoubble) {
                return 3;
            } else {
                return 2;
            }
        }

        public void judge(int position) {
            //进入商品详情界面
            if (isDoubble) {
                if (position == 0 || position == list1.size() + 1) {
                    return;
                } else {
                    cart.remove(position - 2);

                }
            } else {
                System.out.println(position);
                if (position == 0) {
                    return;
                } else {
                    cart.remove(position - 1);
                    list1.clear();
                    list1.addAll(cart);
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isDoubble) {
                //有过期商品
                if (position == 0) {
                    return TYPE_TITLE1;
                } else if (position == list1.size() + 1) {
                    return TYPE_TITLE2;
                } else {
                    return TYPE_NORMAL;
                }
            } else {
                if (position == 0) {
                    return TYPE_TITLE1;
                } else {
                    return TYPE_NORMAL;
                }
            }
            //根据position分别返回三种布局(在过期商品中将颜色改变)

        }
    }

    private void initView2() {
        if (cart.size() == 0) {
            jzView.setVisibility(View.GONE);

            //YHView.setVisibility(View.GONE);
            kview.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
            btnLCount.setVisibility(View.GONE);
            return;
        } else {
            //		  get();
            btnEdit.setVisibility(View.VISIBLE);
            btnLCount.setVisibility(View.VISIBLE);
            kview.setVisibility(View.GONE);
            jzView.setVisibility(View.GONE);
            yhView.setVisibility(View.VISIBLE);
            //JZView.setVisibility(View.GONE);
        }

        jzView.setVisibility(View.VISIBLE);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                Gson gson = new Gson();
                bean = gson.fromJson(json, ShoppingCarBean.class);
                totalcount = bean.totalCount;

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (totalcount == 0) {
                            jzView.setVisibility(View.GONE);
                        } else {
                            //get();
                            btnEdit.setVisibility(View.VISIBLE);
                            btnLCount.setVisibility(View.VISIBLE);
                            kview.setVisibility(View.GONE);
                            jzView.setVisibility(View.GONE);
                            yhView.setVisibility(View.VISIBLE);
                            //JZView.setVisibility(View.GONE);

                        }
                    }
                });

            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.print("网络访问失败" + e);
                e.printStackTrace();
            }
        });

        //	   initeViewCar(loginedView);
    }

    static class ViewHolder {
        ImageView      picture;
        TextView       num;//数量
        TextView       color;    //颜色
        TextView       size;
        TextView       count;
        TextView       name;
        ImageView      delete;
        TextView       price;
        RelativeLayout rl;

    }

    static class TitleHolder1 {
        TextView tvScore;
        TextView count;
        TextView price;
    }

    static class TitleHolder2 {
        public TextView tvTitle;
    }


}
