package browser.pig.cn.pig.min;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import browser.pig.cn.pig.R;
import browser.pig.cn.pig.ShowActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.ui.base.BaseActivity;

public class MinActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {

    }


    @OnClick({R.id.imageView3, R.id.relativeLayout3, R.id.relativeLayout4, R.id.relativeLayout6, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView3:
                break;
            case R.id.relativeLayout3:
                break;
            case R.id.relativeLayout4:
                break;
            case R.id.relativeLayout6:
            {
                Intent intent = new Intent(this, ShowActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
                break;
            case R.id.btn_exit:
                break;
        }
    }
}
