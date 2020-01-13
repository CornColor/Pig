package browser.pig.cn.pig.min;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;

import browser.pig.cn.pig.R;
import browser.pig.cn.pig.login.LoginActivity;
import browser.pig.cn.pig.net.CommonCallback;
import browser.pig.cn.pig.register.FindPasswordActivity;
import browser.pig.cn.pig.register.RegisterActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.net.BaseBean;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.utils.util.SPUtils;

import static browser.pig.cn.pig.net.ApiSearvice.MODIFY_PASSWORD;
import static browser.pig.cn.pig.net.ApiSearvice.REGISTER;

public class ModifyPasswordActivity extends BaseActivity {

    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.et_q_password)
    EditText etQPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
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
    private void modify(String phone, String password, String pre_password) {
        OkGo.<BaseBean>post(MODIFY_PASSWORD)
                .params("phone", phone)
                .params("password", password)
                .params("pre_password", pre_password)
                .execute(new CommonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onStart(Request<BaseBean, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("修改...");
                    }

                    @Override
                    public void onFailure(String code, String s) {
                        showToast(s);

                    }

                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        showToast("修改成功");
                        SPUtils.getInstance().remove("token");
                        SPUtils.getInstance().remove("phone");
                        SPUtils.getInstance().remove("id");
                        Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        cancelProgressDialog();
                    }
                });

    }
    @OnClick({R.id.close, R.id.tv_wangji_password, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close:
                finish();
                break;
            case R.id.tv_wangji_password:
            {
                Intent intent = new Intent(this, FindPasswordActivity.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_login:
            {
                String phone = SPUtils.getInstance().getString("phone");
                String old = etOldPassword.getText().toString();
                if (old == null || old.length() < 0) {
                    showToast("请输入旧密码");
                    return;
                }
                String password = etNewPassword.getText().toString();
                if (password == null || password.length() < 0) {
                    showToast("请输入新密码");
                    return;
                }
                String qpassword = etQPassword.getText().toString();
                if (qpassword == null || qpassword.length() <= 0) {
                    showToast("请输入确认密码");
                    return;
                }
                if (!password.equals(qpassword)) {
                    showToast("两次输入的密码不一致");
                    return;
                }

                modify(phone,password,old);

            }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
