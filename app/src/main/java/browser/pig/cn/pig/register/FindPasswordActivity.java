package browser.pig.cn.pig.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;

import java.util.ArrayList;
import java.util.List;

import browser.pig.cn.pig.MainActivity;
import browser.pig.cn.pig.R;
import browser.pig.cn.pig.login.LoginActivity;
import browser.pig.cn.pig.net.CommonCallback;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.net.BaseBean;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.ui.base.WeakReferenceHandle;
import cn.my.library.utils.util.SPUtils;

import static browser.pig.cn.pig.net.ApiSearvice.FIND_PASSWORD;
import static browser.pig.cn.pig.net.ApiSearvice.SEND_FIND_PASSWORD_CODE;

public class FindPasswordActivity extends BaseActivity implements Handler.Callback {

    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @Bind(R.id.tv_1)
    TextView tv1;
    @Bind(R.id.rl_001)
    RelativeLayout rl001;
    @Bind(R.id.tv_2)
    TextView tv2;
    @Bind(R.id.rl_002)
    RelativeLayout rl002;
    @Bind(R.id.tv_3)
    TextView tv3;
    @Bind(R.id.rl_003)
    RelativeLayout rl003;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.view)
    View view;
    @Bind(R.id.rl_01)
    RelativeLayout rl01;
    @Bind(R.id.rl_02)
    RelativeLayout rl02;
    @Bind(R.id.rl_03)
    RelativeLayout rl03;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @Bind(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.tv_get_code)
    TextView tvGetCode;
    @Bind(R.id.rl_code)
    RelativeLayout rlCode;
    @Bind(R.id.ll_1)
    LinearLayout ll1;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.et_q_password)
    EditText etQPassword;
    @Bind(R.id.ll_2)
    LinearLayout ll2;
    @Bind(R.id.ll_3)
    LinearLayout ll3;
    @Bind(R.id.linearLayout3)
    RelativeLayout linearLayout3;
    @Bind(R.id.btn_next)
    Button btnNext;


    private List<TextView> t_s;
    private List<View> v_s;
    private List<View> v_ss;

    private int postion = 0;
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
        setContentView(R.layout.activity_find_password);
        ButterKnife.bind(this);
        t_s = new ArrayList<>();
        t_s.add(tv1);
        t_s.add(tv2);
        t_s.add(tv3);

        v_s = new ArrayList<>();
        v_s.add(rl01);
        v_s.add(rl02);
        v_s.add(rl03);

        v_ss = new ArrayList<>();
        v_ss.add(ll1);
        v_ss.add(ll2);
        v_ss.add(ll3);


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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_get_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code:
                getCode();
                break;
            case R.id.btn_next:
                switch (postion) {
                    case 0:
                        String phone = etPhone.getText().toString();
                        if (phone == null || phone.length() < 11) {
                            showToast("请输入正确格式的手机号码");
                            return;
                        }
                        String code = etCode.getText().toString();
                        if (code == null || code.length() <= 0) {
                            showToast("请输入正确格式的手机号码");
                            return;
                        }
                        setPostion(1);
                        break;
                    case 1: {
                        String password = etPassword.getText().toString();
                        if (password == null || password.length() <= 0) {
                            showToast("请输入密码");
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
                        phone = etPhone.getText().toString();
                        if (phone == null || phone.length() < 11) {
                            showToast("请输入正确格式的手机号码");
                            return;
                        }

                        code = etCode.getText().toString();
                        if (code == null || code.length() < 0) {
                            showToast("请输入短信验证码");
                            return;
                        }

                        findPassword(phone, password, code);
                    }
                    break;
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
        OkGo.<BaseBean>post(SEND_FIND_PASSWORD_CODE)
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

    private void findPassword(String phone, String password, String code) {
        OkGo.<BaseBean>post(FIND_PASSWORD)
                .params("phone", phone)
                .params("password", password)
                .params("code", code)
                .execute(new CommonCallback<BaseBean>(BaseBean.class) {
                    @Override
                    public void onStart(Request<BaseBean, ? extends Request> request) {
                        super.onStart(request);
                        showProgressDialog("");
                    }

                    @Override
                    public void onFailure(String code, String s) {
                        showToast(s);

                    }

                    @Override
                    public void onSuccess(BaseBean baseBean) {
                        setPostion(2);
                        showToast("找回密码成功");
                        SPUtils.getInstance().remove("token");
                        SPUtils.getInstance().remove("phone");
                        SPUtils.getInstance().remove("id");
                        Intent intent = new Intent(FindPasswordActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("action",100);
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

    private void setPostion(int position) {
        for (int i = 0; i < v_s.size(); i++) {
            TextView textView = t_s.get(i);
            View view = v_s.get(i);
            View view1 = v_ss.get(i);
            if (position == i) {
                textView.setTextColor(Color.parseColor("#FE9C2D"));
                view.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
            } else {
                textView.setTextColor(Color.parseColor("#ffcccccc"));
                view.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
            }
        }
        this.postion = position;


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

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        finish();
    }
}
