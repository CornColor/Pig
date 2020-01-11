package browser.pig.cn.pig;

import android.os.Bundle;
import android.view.View;
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
    @Bind(R.id.tv_content)
    TextView tvContent;
    private String t1 = "关于我们";
    private String t2 = "注册服务协议";
    private String c1 = "\"\t猪悠悠浏览器是一个是专门为上网人群打造的手机浏览器，节省流量，并收集手机各类常用网址，热点新闻，小说等，致力于为用户提供一站式超快上网体验。\n" +
            "\n" +
            "\t猪悠悠为你而生，让您的上网不在犹豫不决。\"";
    private String c2 = "石家庄云道科技有限公司(以下简称“公司”或“本公司”)通过其合法运营的APP平台（APP平台名称：猪悠悠浏览器，简称“猪悠悠”）、微信端及其它已经开发及将来可能开发的实现部分或全部功能的移动终端软件（以下统称“平台”），依据本协议为平台注册用户（以下简称“平台用户”、“用户”或“您”）提供服务。在本协议中，根据上下文义。在成为平台用户前，您务必仔细阅读以下条款，充分理解各条款内容后再选择是否接受本协议。一旦您在平台提交用户注册申请，即意味着您已阅读本协议所有条款，并对本协议条款的含义及相应的法律后果已全部通晓并充分理解，同意受本协议约束。\n" +
            " 一、本协议的签订和修订\n" +
            " 1\n" +
            " 本平台注册人应当同时具备如下条件：\n" +
            " 1.1\n" +
            " 个人客户条件：\n" +
            " 1.1.1\n" +
            " 持有中华人民共和国法律认可有效身份证明的18周岁以上的自然人；\n" +
            " 1.1.2\n" +
            " 具有中华人民共和国法律规定的完全民事行为能力的自然人；\n" +
            " 1.2\n" +
            " 企业法人或其他机构条件：\n" +
            " 1.2.1\n" +
            " 依照中华人民共和国法律合法成立的境内法人企业；\n" +
            " 1.2.2\n" +
            " 取得营业执照并正常合法经营。\n" +
            " 请您仔细确认是否符合上述条件，如不符合上述条件的，请勿注册，如违反上述条件注册平台账户及及资金存管账户，我公司有权随时中止或终止您的用户资格。\n" +
            " 2\n" +
            " 注册资料提供";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);
        int type = getIntent().getIntExtra("type",1);
        if(type == 1){
            title.setText(t1);
            tvContent.setText(c1);

        }else if(type == 2){
            title.setText(t2);
            tvContent.setText(c2);
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
