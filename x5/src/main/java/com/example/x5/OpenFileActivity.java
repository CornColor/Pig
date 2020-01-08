package com.example.x5;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;


public class OpenFileActivity extends Activity implements TbsReaderView.ReaderCallback {
    TbsReaderView mTbsReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file);
        mTbsReaderView = new TbsReaderView(this, this);
        RelativeLayout rootRl = (RelativeLayout) findViewById(R.id.rl_root);
        rootRl.addView(mTbsReaderView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        String filePath = getIntent().getStringExtra("filePath");
        findViewById(R.id.vt_tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        displayFile(filePath);
    }
    public static void start(Context context, String filePath) {
        Intent intent = new Intent();
        intent.putExtra("filePath", filePath);
        intent.setClass(context, OpenFileActivity.class);
        context.startActivity(intent);
    }




    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();

    }
    private void displayFile(String filePath) {

        Bundle bundle = new Bundle();
        bundle.putString("filePath",filePath);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        boolean result = mTbsReaderView.preOpen(parseFormat(filePath), false);
        if (result) {
            mTbsReaderView.openFile(bundle);
        }
    }
    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
