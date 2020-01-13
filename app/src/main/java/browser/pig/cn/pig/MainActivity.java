package browser.pig.cn.pig;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import browser.pig.cn.pig.adapter.HomeSelectAdapter;
import browser.pig.cn.pig.bean.HomeSelect;
import browser.pig.cn.pig.login.LoginActivity;
import browser.pig.cn.pig.min.MinActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.ui.base.BaseActivity;
import cn.my.library.utils.util.FilePathUtil;
import cn.my.library.utils.util.SPUtils;
import cn.my.library.utils.util.StringUtils;
import freemarker.template.utility.StringUtil;

public class MainActivity extends BaseActivity implements HomeSelectAdapter.OnHomeSelectClickListener {
    private int[] ds = {R.drawable.icon_baidu, R.drawable.icon_xinlangshipin, R.drawable.icon_zhougongjiemeng,
            R.drawable.icon_baozoumanhua, R.drawable.icon_mianfeixiaoshuo, R.drawable.icon_jinrifengyun, R.drawable.icon_teixuejunshi,
            R.drawable.icon_zhuyouyou};

    private String[] ns = {"百度", "新浪视频", "周公解梦", "暴走漫画", "免费小说", "今日风云", "铁血军事", "猪悠悠"};
    private String[] urls = {"https://www.baidu.com", "https://video.sina.cn/", "https://m.xzw.com/jiemeng/",
            "http://baozoumanhua.com/", "http://www.quanshuwang.com", "http://top.baidu.com", "https://m.tiexue.net",
            "http://izyy.hbyundao.com/zhuyouyouclient/index.html"};
    private HomeSelectAdapter homeSelectAdapter;
    private List<HomeSelect> homeSelects;
    private RecyclerView rv_entrance;
    private EditText et_search;

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

        rv_entrance = findViewById(R.id.rv_entrance);
        rv_entrance.setLayoutManager(new GridLayoutManager(this, 4));
        rv_entrance.setAdapter(homeSelectAdapter);
        et_search = findViewById(R.id.et_search);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    hideKeyboard(et_search);
                    // 在这里写搜索的操作,一般都是网络请求数据
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("https://www.baidu.com/s?wd="+et_search.getText().toString());//此处填链接
                        intent.setData(content_url);
                        startActivity(intent);

                    }catch (Exception e){

                    }
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public void initData() {
        requestPermission(Permission.Group.STORAGE,Permission.Group.CAMERA);
    }

    private void fileDownLoad(String path) {
        if(StringUtils.isEmpty(path)){

            showToast("下载安装包失败");
            finish();
            return;
        }
        FileDownloader.getImpl().create(path)
                .setPath(FilePathUtil.getFilePath(this, "apk") + File.separator + "zhixun" + System.currentTimeMillis() + ".apk")
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {

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

    }

    @Override
    public void onHomeSelect(int position) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(homeSelects.get(position).getUrl());//此处填链接
            intent.setData(content_url);
            startActivity(intent);

        }catch (Exception e){

        }


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
}
