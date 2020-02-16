package browser.pig.cn.pig;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.ui.base.BaseActivity;

public class ShowActivity extends BaseActivity {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.view2)
    View view2;
    @Bind(R.id.web)
    WebView web;

    private String t1 = "关于我们";
    private String t2 = "服务协议";
    private String t3 ="隐私政策";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        int type = getIntent().getIntExtra("type", 1);
        if (type == 1) {
            title.setText(t1);
            web.loadUrl("file:///android_asset/guanyu.html");

        } else if (type == 2) {
            title.setText(t2);
            web.loadUrl("file:///android_asset/xieyi.html");
        }else if(type == 3){
            title.setText(t3);
            web.loadUrl("file:///android_asset/yinsi.html");
        }
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

    @OnClick(R.id.imageView2)
    public void onViewClicked() {
        finish();
    }
}
