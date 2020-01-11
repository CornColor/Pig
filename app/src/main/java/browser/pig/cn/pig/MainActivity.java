package browser.pig.cn.pig;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import browser.pig.cn.pig.adapter.HomeSelectAdapter;
import browser.pig.cn.pig.bean.HomeSelect;
import browser.pig.cn.pig.min.MinActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.my.library.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements HomeSelectAdapter.OnHomeSelectClickListener {
    private int[] ds = {R.drawable.icon_baidu, R.drawable.icon_xinlangshipin, R.drawable.icon_zhougongjiemeng,
            R.drawable.icon_baozoumanhua, R.drawable.icon_mianfeixiaoshuo, R.drawable.icon_jinrifengyun, R.drawable.icon_teixuejunshi,
            R.drawable.icon_zhuyouyou};

    private String[] ns = {"百度", "新浪视频", "周公解梦", "暴走漫画", "免费小说", "今日风云", "铁血军事", "猪悠悠"};
    private HomeSelectAdapter homeSelectAdapter;
    private List<HomeSelect> homeSelects;
    private RecyclerView rv_entrance;

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
            homeSelects.add(homeSelect);
        }
        homeSelectAdapter = new HomeSelectAdapter(this, homeSelects);
        homeSelectAdapter.setOnHomeSelectClickListener(this);

        rv_entrance = findViewById(R.id.rv_entrance);
        rv_entrance.setLayoutManager(new GridLayoutManager(this, 4));
        rv_entrance.setAdapter(homeSelectAdapter);

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
    public void onHomeSelect(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.iv_geren)
    public void onViewClicked() {
        Intent intent = new Intent(this, MinActivity.class);
        startActivity(intent);


    }
}
