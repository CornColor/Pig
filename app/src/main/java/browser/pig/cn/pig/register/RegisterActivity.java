package browser.pig.cn.pig.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;

import browser.pig.cn.pig.R;
import browser.pig.cn.pig.ShowActivity;
import browser.pig.cn.pig.login.LoginActivity;
import browser.pig.cn.pig.net.CommonCallback;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.net.BaseBean;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.ui.base.WeakReferenceHandle;

import static browser.pig.cn.pig.net.ApiSearvice.REGISTER;
import static browser.pig.cn.pig.net.ApiSearvice.SEND_REGISTER_CODE;

public class RegisterActivity extends BaseActivity implements Handler.Callback {
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(R.id.rl_code)
    RelativeLayout rlCode;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_y_code)
    EditText etYCode;
    private boolean offs = true;
    private WeakReferenceHandle handler = new WeakReferenceHandle(this);
    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            int a = 60;
            while (-1 < a && offs) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.arg1 = a;
                    handler.sendMessage(message);
                    a--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

    @Override
    public boolean handleMessage(Message msg) {
        if (tvGetCode != null) {
            tvGetCode.setText(msg.arg1 + "s");
            if (msg.arg1 == 0) {
                tvGetCode.setText("获取验证码");
                tvGetCode.setClickable(true);
            }
            if (!offs) {
                tvGetCode.setText("获取验证码");
                tvGetCode.setClickable(true);
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_get_code, R.id.et_password, R.id.btn_login, R.id.tv_xieyi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.et_password:
                break;
            case R.id.btn_login: {
                String phone = etPhone.getText().toString();
                if (phone == null || phone.length() < 11) {
                    showToast("请输入正确格式的手机号码");
                    return;
                }
                String password = etPassword.getText().toString();
                if (password == null || password.length() < 0) {
                    showToast("请输入密码");
                    return;
                }
                String code = etCode.getText().toString();
                if (code == null || code.length() < 0) {
                    showToast("请输入短信验证码");
                    return;
                }

                register(phone, password, code);


            }
            break;
            case R.id.tv_xieyi: {
                Intent intent = new Intent(this, ShowActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
            break;
        }
    }

    //获取验证码
    private void getCode() {
        String phone = etPhone.getText().toString();
        if (phone == null || phone.length() < 11) {
            showToast("请输入正确格式的手机号码");
            return;
        }
        OkGo.<BaseBean>post(SEND_REGISTER_CODE)
                .params("phone", phone)
                .execute(new CommonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onStart(Request<BaseBean, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("发送短信验证码...");
                    }

                    @Override
                    public void onFailure(String code, String s) {
                        showToast(s);

                    }

                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        tvGetCode.setClickable(false);
                        new Thread(sendable).start();

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        cancelProgressDialog();
                    }
                });


    }

    private void register(String phone, String password, String code) {
        OkGo.<BaseBean>post(REGISTER)
                .params("phone", phone)
                .params("password", password)
                .params("code", code)
                .execute(new CommonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onStart(Request<BaseBean, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("注册...");
                    }

                    @Override
                    public void onFailure(String code, String s) {
                        showToast(s);

                    }

                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        cancelProgressDialog();
                    }
                });

    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
