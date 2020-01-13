package browser.pig.cn.pig.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import browser.pig.cn.pig.MainActivity;
import browser.pig.cn.pig.R;
import browser.pig.cn.pig.net.CommonCallback;
import browser.pig.cn.pig.register.FindPasswordActivity;
import browser.pig.cn.pig.register.RegisterActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.utils.util.SPUtils;
import cn.my.library.utils.util.StringUtils;

import static browser.pig.cn.pig.net.ApiSearvice.LOGIN;

public class LoginActivity extends BaseActivity {

    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    private void login(String phone, String password) {
        OkGo.<LoginBean>post(LOGIN)
                .params("phone", phone)
                .params("password", password)
                .execute(new CommonCallback<LoginBean>(LoginBean.class) {
                    @Override
                    public void onStart(Request<LoginBean, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("登录...");
                    }

                    @Override
                    public void onFailure(String code, String s) {
                        showToast(s);

                    }

                    @Override
                    public void onSuccess(LoginBean loginBean) {

                        SPUtils.getInstance().put("token", loginBean.getData().getToken());
                        SPUtils.getInstance().put("id", loginBean.getData().getId());
                        SPUtils.getInstance().put("phone", loginBean.getData().getPhone());

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Response<LoginBean> response) {
                        super.onError(response);
                        showToast("登录失败");
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        cancelProgressDialog();
                    }
                });
    }

    @OnClick({R.id.tv_register, R.id.tv_wangji_password, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
            {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.tv_wangji_password:{
                Intent intent = new Intent(this, FindPasswordActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_login:
                String phone = etPhone.getText().toString();
                if(StringUtils.isEmpty(phone)||phone.length()<11){
                    showToast("请输入正确的手机号码");
                    return;
                }
                String password = etPassword.getText().toString();
                if(StringUtils.isEmpty(password)){
                    showToast("请输入密码");
                    return;
                }
                login(phone,password);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
