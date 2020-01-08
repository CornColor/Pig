package browser.pig.cn.pig;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import browser.pig.cn.pig.adapter.HomeSelectAdapter;
import browser.pig.cn.pig.bean.HomeSelect;
import cn.my.library.ui.base.BaseActivity;

public class MainActivity extends BaseActivity implements HomeSelectAdapter.OnHomeSelectClickListener {
//    private int[]ds = {R.drawable.xiangmuxinxi,R.drawable.qiyexinxi,R.drawable.gongzuozu,R.drawable.gerenxinxi,R.drawable.wendangxiazai,R.drawable.xiehuigonggao};
//
//    private String[]ns = {"项目信息","企业信息","工作组","个人信息","文档下载","协会公告"};
    private HomeSelectAdapter homeSelectAdapter;
    private List<HomeSelect> homeSelects;
    private RecyclerView rv_entrance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeSelects = new ArrayList<>();
//        for (int i = 0;i<ds.length;i++){
//            HomeSelect homeSelect = new HomeSelect();
//            homeSelect.setDrawableId(ds[i]);
//            homeSelect.setName(ns[i]);
//            homeSelects.add(homeSelect);
//        }
        homeSelectAdapter = new HomeSelectAdapter(this,homeSelects);
        homeSelectAdapter.setOnHomeSelectClickListener(this);

        rv_entrance = findViewById(R.id.rv_entrance);
        rv_entrance.setLayoutManager(new GridLayoutManager(this,4));
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
}
