package browser.pig.cn.pig.min;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import browser.pig.cn.pig.MainActivity;
import browser.pig.cn.pig.R;
import browser.pig.cn.pig.ShowActivity;
import browser.pig.cn.pig.login.LoginActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.utils.util.SPUtils;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MinActivity extends BaseActivity {

    @Bind(R.id.phone)
    TextView mPhone;
    @Bind(R.id.tv_cache)
    TextView tvCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min);
        ButterKnife.bind(this);
        String phone = SPUtils.getInstance().getString("phone");
        mPhone.setText(getStarMobile(phone));
        if (!SPUtils.getInstance().contains("cache")) {
            Random random = new Random();
            float num = random.nextFloat() * 10;
            SPUtils.getInstance().put("cache", num);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
            String strPrice = decimalFormat.format(num);//返回字符串
            tvCache.setText(strPrice+"M");
        }
    }

    @Override
    public void initData() {

    }

    public static String getStarMobile(String mobile) {

        if (!TextUtils.isEmpty(mobile)) {

            if (mobile.length() >= 11)

                return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());

        } else {

            return "";

        }
        return mobile;

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
            case R.id.imageView3://退出
                finish();
                break;
            case R.id.relativeLayout3://修改密码
            {
                Intent intent = new Intent(MinActivity.this,ModifyPasswordActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.relativeLayout4://清理缓存
                showConfirmDialog("你确定要清除数据吗?", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SPUtils.getInstance().remove("cache");
                        tvCache.setText("0M");
                        showToast("清理完成");
                        sweetAlertDialog.dismiss();
                    }
                });

                break;
            case R.id.relativeLayout6: {
                Intent intent = new Intent(this, ShowActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
            break;
            case R.id.btn_exit:
            {
                SPUtils.getInstance().remove("token");
                SPUtils.getInstance().remove("phone");
                SPUtils.getInstance().remove("id");
                SPUtils.getInstance().remove("invitation_code");
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
                break;
        }
    }
}
