package more_activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.tmall.myredboy.R;
import com.tmall.myredboy.fragment.BaseFragment;

public class MoreFragment extends BaseFragment implements View.OnClickListener {


    @Override
    public View initView() {
        View view = View.inflate(activity, R.layout.activity_more, null);
           view.setOnClickListener(this);

        return view;
    }

    @Override
    public void initData() {




    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.payCenter:

            break;
            case R.id.lookLog:

            break;
            case R.id.helpCenter:

            break;
            case    R.id.userFeedBack :

            break;
            case R.id.about:
                      startActivity(new Intent(activity, AboutActivity.class));
            break;
            case  R.id. btn:
                String number = "40088888888";
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel://"+number));
                startActivity(intent);
                break;
        }
    }
}
