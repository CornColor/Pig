package browser.pig.cn.pig.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import browser.pig.cn.pig.MainActivity;
import browser.pig.cn.pig.R;
import browser.pig.cn.pig.ShowActivity;
import browser.pig.cn.pig.login.LoginActivity;
import cn.my.library.other.WeakReferenceHandle;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.utils.util.SPUtils;

public class WelcomeActivity extends BaseActivity implements Handler.Callback{
    public final int MSG_FINISH_LAUNCHERACTIVITY = 500;
    public WeakReferenceHandle mHandler = new WeakReferenceHandle(this);
    private RelativeLayout rl_xieyi;
    private Button btn_ok,btn_no;
    private TextView tv_xieyi,tv_zhengce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        super.onCreate(savedInstanceState);
        // 不显示系统的标题栏，保证windowBackground和界面activity_main的大小一样，显示在屏幕不会有错位（去掉这一行试试就知道效果了）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 注意：添加3秒睡眠，以确保黑屏一会儿的效果明显，在项目应用要去掉这3秒睡眠
        setContentView(R.layout.activity_welcome);
        // 停留3秒后发送消息，跳转到MainActivity
       // mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 3000);

        rl_xieyi = findViewById(R.id.rl_xieyi);
        if(!SPUtils.getInstance().contains("xy")){
            rl_xieyi.setVisibility(View.VISIBLE);
        }else {
            rl_xieyi.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 2000);
        }
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_xieyi.setVisibility(View.GONE);
                mHandler.sendEmptyMessageDelayed(MSG_FINISH_LAUNCHERACTIVITY, 2000);
                SPUtils.getInstance().put("xy","1");
            }
        });
        tv_xieyi = findViewById(R.id.tv_xieyi);
        tv_xieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ShowActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        tv_zhengce = findViewById(R.id.tv_zhengce);
        tv_zhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, ShowActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });

        btn_no = findViewById(R.id.btn_no);
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_FINISH_LAUNCHERACTIVITY:
                toMainActivity();
                break;

            default:
                break;
        }
        return false;
    }

    private void toMainActivity(){
        //初始化用户
//        User user = new User(SPUtils.getInstance().getString("token"),SPUtils.getInstance().getString("nickname"),
//                SPUtils.getInstance().getString("head"),SPUtils.getInstance().getInt("type"));
//        ZlchApplication.setTOKEN(user.getToken());
//        ZlchApplication.setUser(user);


        //跳转到MainActivity，并结束当前的LauncherActivity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void toLoginActivity(){
//        //跳转到LoginActivity，并结束当前的LauncherActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
