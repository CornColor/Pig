package browser.pig.cn.pig;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.base.Request;
import com.yanzhenjie.permission.Permission;
import com.yidian.newssdk.exportui.NewsPortalFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import browser.pig.cn.pig.adapter.HomeSelectAdapter;
import browser.pig.cn.pig.bean.AppAdressBean;
import browser.pig.cn.pig.bean.BanBenBean;
import browser.pig.cn.pig.bean.HomeSelect;
import browser.pig.cn.pig.login.LoginActivity;
import browser.pig.cn.pig.min.MinActivity;
import browser.pig.cn.pig.net.CommonCallback;
import browser.pig.cn.pig.view.SideGroupLayout;
import browser.pig.cn.pig.web.BrowserActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.eventbus.MessageEvent;
import cn.my.library.net.BaseBean;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.utils.util.AppUtils;
import cn.my.library.utils.util.DeviceUtils;
import cn.my.library.utils.util.FilePathUtil;
import cn.my.library.utils.util.SPUtils;
import cn.my.library.utils.util.StringUtils;
import freemarker.template.utility.StringUtil;

import static browser.pig.cn.pig.net.ApiSearvice.SEND_REGISTER_CODE;
import static browser.pig.cn.pig.net.ApiSearvice.UPDATA_ADDRESS;
import static browser.pig.cn.pig.net.ApiSearvice.Y_CODE;

public class MainActivity extends BaseActivity implements HomeSelectAdapter.OnHomeSelectClickListener,SideGroupLayout.OnGroupScrollListener {
    private int[] ds = {R.mipmap.icon_baidu, R.mipmap.icon_xinlangshipin, R.mipmap.icon_zhougongjiemeng,
            R.mipmap.icon_baozoumanhua, R.mipmap.icon_mianfeixiaoshuo, R.mipmap.icon_jinrifengyun, R.mipmap.icon_teixuejunshi,
            R.mipmap.icon_zhuyouyou};

    private String[] ns = {"百度", "视频电影", "娱乐八卦", "免费漫画", "免费小说", "今日风云", "创业情报", "实用工具"};
    private String[] urls = {"http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=baidu_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=sina_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=jm_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=mh_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=xs_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=fy_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=js_android",
            "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=zyy_android"

    };
    private HomeSelectAdapter homeSelectAdapter;
    private List<HomeSelect> homeSelects;
    private RecyclerView rv_entrance;
    private EditText et_search;
    private Fragment fragmentNavi;
    private SideGroupLayout sl;
    private String []search = {"兼职","挣钱","赚钱","猪悠悠","钱","赚","挣"};
    private String zyy2 = "http://izyy.hbyundao.com/zhuyouyou_app_clientmanager/jump/jump?site=zyy2_android";
    private Pattern httpPattern;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        homeSelects = new ArrayList<>();
        for (int i = 0; i < ds.length; i++) {
            HomeSelect homeSelect = new HomeSelect();
            homeSelect.setDrawableId(ds[i]);
            homeSelect.setName(ns[i]);
            homeSelect.setUrl(urls[i]);
            homeSelects.add(homeSelect);
        }
        homeSelectAdapter = new HomeSelectAdapter(this, homeSelects);
        homeSelectAdapter.setOnHomeSelectClickListener(this);

        sl = findViewById(R.id.sl);

        rv_entrance = findViewById(R.id.rv_entrance);
        rv_entrance.setLayoutManager(new GridLayoutManager(this, 4));
        rv_entrance.setAdapter(homeSelectAdapter);
        et_search = findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard(et_search);
                    for (int i = 0;i<search.length;i++){
                        if(et_search.getText().toString().equals(search[i])){
                            if(!SPUtils.getInstance().contains("token")){
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.putExtra("type",1);
                                startActivity(intent);
                            }else {
                                try {
                                    Intent intent = new Intent(MainActivity.this,BrowserActivity.class);
                                    intent.putExtra("url",buildUrl(zyy2));
                                    startActivity(intent);

                                }catch (Exception e){

                                }
                            }
                            return  true;


                        }
                    }
                    //点击搜索的时候隐藏软键盘

                    try {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(homeSelects.get(position).getUrl());//此处填链接
//                intent.setData(content_url);
//                startActivity(intent);

                        Intent intent = new Intent(MainActivity.this,BrowserActivity.class);
                        String uu = et_search.getText().toString();
                        String uu1 = et_search.getText().toString();
                        if(!uu.startsWith("http://")&&!uu.startsWith("https://")){
                            uu="http://"+uu;
                        }
                        if(isHttpUrl(uu)){

                            intent.putExtra("url",uu);
                        }else {
                            intent.putExtra("url","https://www.sogou.com/web?query="+et_search.getText().toString());
                        }

                        startActivity(intent);

                    }catch (Exception e){

                    }
                    return true;
                }

                return false;
            }
        });

        int action = getIntent().getIntExtra("action",1);
        if(action == 100){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

         fragmentNavi = new NewsPortalFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.portal_container, fragmentNavi)
                .commitNowAllowingStateLoss();

    }
    public  boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }


    @Override
    public void onBusEvent(MessageEvent messageEvent) {
        if(messageEvent.getCode() == 90){
            try {
                Intent intent = new Intent(MainActivity.this,BrowserActivity.class);
                intent.putExtra("url",buildUrl(zyy2));
                startActivity(intent);

            }catch (Exception e){

            }
        }
    }

    public String buildUrl(String url){

          StringBuilder stringBuilder = new StringBuilder(url);
          if(SPUtils.getInstance().contains("token")){
              if(SPUtils.getInstance().contains("phone")){
                  stringBuilder.append("&zyy_tel="+SPUtils.getInstance().getString("phone"));

              }
              if(SPUtils.getInstance().contains("invitation_code")){
                  stringBuilder.append("&zyy_invitationcode="+SPUtils.getInstance().getString("invitation_code"));

              }
          }

        stringBuilder.append("&zyy_onlycode="+DeviceUtils.getAndroidID());
        return stringBuilder.toString();
    }


    @Override
    public void initData() {
        requestPermission(Permission.Group.STORAGE,Permission.Group.CAMERA);
        requestPermission(new String[]{"android.permission.WRITE_SETTINGS"});
    }

    private void fileDownLoad(String path) {
        if(StringUtils.isEmpty(path)){
            return;
        }
        FileDownloader.getImpl().create(path)
                .setPath(FilePathUtil.getFilePath(this, "apk") + File.separator + "pig" + System.currentTimeMillis() + ".apk")
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        try{
                            if(!StringUtils.isEmpty(task.getPath())){
                                AppUtils.installApp(task.getPath());
                            }
                        }catch (Exception e){

                        }


                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {

                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {

                    }
                }).start();

    }
    /**
     * 隐藏软键盘
     * @param  :上下文
     * @param view    :一般为EditText
     */
    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initPresenter() {
        banben();
    }

    @Override
    public void onHomeSelect(int position) {

            try {
//                Intent intent = new Intent();
//                intent.setAction("android.intent.action.VIEW");
//                Uri content_url = Uri.parse(homeSelects.get(position).getUrl());//此处填链接
//                intent.setData(content_url);
//                startActivity(intent);
                String home = buildUrl(homeSelects.get(position).getUrl());
                Intent intent = new Intent(this,BrowserActivity.class);
                intent.putExtra("url",home);
                startActivity(intent);

            }catch (Exception e){

            }




    }

    private void banben(){
        if(!SPUtils.getInstance().contains("token")){
            return;
        }
        OkGo.<BanBenBean>post(Y_CODE)
                .headers("authkey",SPUtils.getInstance().getString("token"))
                .execute(new CommonCallback<BanBenBean>(BanBenBean.class) {

                    @Override
                    public void onFailure(String code, String s) {
                      showToast(s);

                    }

                    @Override
                    public void onSuccess(BanBenBean banBenBean) {
                        String banben = banBenBean.getData().getData();
                        String appVersionName = AppUtils.getAppVersionName();
                        if(!appVersionName.equals(banben)){
                           updata();
                        }

                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        banben();
    }

    private void updata(){
        OkGo.<AppAdressBean>post(UPDATA_ADDRESS)
                .headers("authkey",SPUtils.getInstance().getString("token"))
                .execute(new CommonCallback<AppAdressBean>(AppAdressBean.class) {

                    @Override
                    public void onFailure(String code, String s) {

                    }

                    @Override
                    public void onSuccess(AppAdressBean banBenBean) {
                        String banben = banBenBean.getData().getData();
                        fileDownLoad(banben);

                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_geren)
    public void onViewClicked() {
        if(SPUtils.getInstance().contains("token")){
            Intent intent = new Intent(this, MinActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public boolean isChildScroll() {
        return false;
    }

    @Override
    public boolean isGroupScroll() {
        return false;
    }

    @Override
    public void onScrollChanged(int left, int top) {

    }
}
