package browser.pig.cn.pig;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.liulishuo.filedownloader.FileDownloader;
import com.lzy.okgo.OkGo;
import com.tencent.smtt.sdk.QbSdk;
import com.yidian.newssdk.NewsFeedsSDK;
import com.yidian.newssdk.export.IReportInterface;

import cn.my.library.other.AppManager;

/**
 * created by dan
 */
public class MyAppliction extends Application implements QbSdk.PreInitCallback{

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
        MultiDex.install(this);
        registerLifecycle();
        FileDownloader.setup(this);
        QbSdk.initX5Environment(this,this);

        new NewsFeedsSDK.Builder()
                .setAppId("L_l6aLAsTkxsM7IhsUa8Swgb")
                .setAppKey("GyyILHhopCbnyRj8OpNry78hhxTFN4QE")
                .setContext(getApplicationContext())
                .setDebugEnabled(BuildConfig.DEBUG)
                .build();

        NewsFeedsSDK.getInstance().setReportInterface(new IReportInterface() {

            @Override
            public void onPageSelected(String channelPageName) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 注册生命周期管理
     */
    private void registerLifecycle(){
        //SDK版本大于等于14
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    AppManager.getInstance().addActivity(activity);

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    AppManager.getInstance().finishActivity(activity);

                }
            });
        }

    }

    @Override
    public void onCoreInitFinished() {

    }

    @Override
    public void onViewInitFinished(boolean b) {
        Log.e("X5",b+"");
    }
}
