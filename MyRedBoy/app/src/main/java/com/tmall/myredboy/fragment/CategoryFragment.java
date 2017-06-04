package com.tmall.myredboy.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ant.liao.GifView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tmall.myredboy.R;
import com.tmall.myredboy.activity.lqq.MyBitmapUtils;
import com.tmall.myredboy.activity.lqq.GoodsListActivity;
import com.tmall.myredboy.activity.lqq.SecondCategoryActivity;
import com.tmall.myredboy.bean.lqq.ProductList;
import com.tmall.myredboy.global.GlobalConstants;
import com.tmall.myredboy.global.Globals;
import com.tmall.myredboy.utils.ToastUtils;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;
import static com.tmall.myredboy.R.id.llLoading;

/**
 * LQQ 2016/11/14
 */
public class CategoryFragment extends BaseFragment{

    private View view;
    private ListView lvList;
    private ProductList ProductList;
    private ProductList.Categories categories;
    private ArrayList<ProductList.Categories> categoriesList;
    private MyListAdapter myListAdapter;


    @Override
    public View initView() {
        view = View.inflate(activity, R.layout.layout_category_fragment, null);
        lvList = (ListView) view.findViewById(R.id.lv_list);

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //判断是不是有字条目,如果有就跳入二级分类页面,如果没有就就跳到商品列表页面
                int flag = categoriesList.get(position).isLeaf;
                Intent intent = new Intent();
                if (flag == 1) {
                    //没有字条目,跳到商品列表页面
                    intent.putExtra("id", categoriesList.get(position).parentId);
                    intent.setClass(activity, GoodsListActivity.class);
                    startActivity(intent);
                } else {
                    //0不是叶子节点，有子条目,跳到二级分类页面
                    intent.putExtra("id", categoriesList.get(position).id);
                    intent.putExtra("name", categoriesList.get(position).name);
                    intent.setClass(activity, SecondCategoryActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
    }

    public void getDataFromServer() {
        String url = Globals.URL_PREFIX + GlobalConstants.PRODUCT_FIND_CATORIES;
        com.tmall.myredboy.utils.HttpUtils.getHttpDes(activity, url, ProductList.class, new com.tmall.myredboy.utils.HttpUtils.OnHttpSuccess() {
            @Override
            public void onSuccess(Object object) {


                //object是解析好的对象
                ProductList = (ProductList)object;
                //获取分类浏览页面的字条目
                categoriesList = ProductList.categories;
                //给ListView设置适配器
                myListAdapter = new MyListAdapter();
                lvList.setAdapter(myListAdapter);



            }
        });
    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categoriesList.size();
        }

        @Override
        public ProductList.Categories getItem(int position) {
            return categoriesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mHolder;
            if (convertView == null) {
                mHolder = new ViewHolder();
                convertView = View.inflate(activity, R.layout.list_item_category, null);
                mHolder.ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
                mHolder.tvTitle1 = (TextView) convertView.findViewById(R.id.tv_title1);
                mHolder.littleTitle = (TextView) convertView.findViewById(R.id.tv_little_title);
                mHolder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
                convertView.setTag(mHolder);
            } else {
                mHolder = (ViewHolder) convertView.getTag();
            }

            mHolder.ivImage.setVisibility(View.VISIBLE);
            mHolder.tvTitle1.setVisibility(View.VISIBLE);
            mHolder.littleTitle.setVisibility(View.VISIBLE);

            //给控件赋值
            //从网络获取图片,给控件设置图片
            String twoUrl = categoriesList.get(position).iconUrl;
            MyBitmapUtils getSelector = new MyBitmapUtils();


            getSelector.getSelectrorBitMap(twoUrl, mHolder.ivImage);
            mHolder.tvTitle1.setText(categoriesList.get(position).name);
            mHolder.littleTitle.setText(categoriesList.get(position).desc);
            mHolder.ivArrow.setImageResource(R.drawable.selector_item_arrow_right_red);
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivImage;
        public TextView tvTitle1;
        public TextView littleTitle;
        public ImageView ivArrow;
    }
}
