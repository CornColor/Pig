package browser.pig.cn.pig.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import browser.pig.cn.pig.R;


/**
 * created by dan
 * 自定义弹出框
 */
public class CoustomDialog extends Dialog implements View.OnClickListener{
    OnMultiClickListener listener;

    public static CoustomDialog create(Context context,OnMultiClickListener listener){
        CoustomDialog dialog = new CoustomDialog(context);
        dialog.init(context,listener);
        return dialog;
    }
    public static CoustomDialog create(Context context,int mLayoutId,int mGravity){
        CoustomDialog dialog = new CoustomDialog(context);
        dialog.init(context,mLayoutId,mGravity);
        return dialog;
    }

    public CoustomDialog(@NonNull Context context) {
        this(context, R.style.CoustomDialog);
    }

    public CoustomDialog(@NonNull Context context, int themeResId) {
        super(context,R.style.CoustomDialog);
    }

    protected CoustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context,R.style.CoustomDialog);
    }


    private void init (Context context,OnMultiClickListener listener){
        this.listener = listener;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        ViewGroup view = (ViewGroup) View.inflate(context,R.layout.view_dlown,null);

        Button confirm = view.findViewById(R.id.btn_ok);
        if(confirm!=null){
            confirm.setOnClickListener(this);
        }
        Button cancel = view.findViewById(R.id.btn_no);
        if(cancel!=null){
            cancel.setOnClickListener(this);
        }
        setContentView(view);
    }
    private void init (Context context,int mLayoutId,int mGravity){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.gravity = mGravity;
        getWindow().setAttributes(lp);
        ViewGroup view = (ViewGroup) View.inflate(context,mLayoutId,null);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //确定按钮
            case R.id.btn_ok:
                if(listener != null){
                    listener.onConfirm(this,v);
                }
                break;
                //取消
            case R.id.btn_no:
                if(listener != null){
                    listener.onCancel(this,v);
                }
                break;
        }
    }

    /**
     * 多的点击事件监听
     */
    public interface OnMultiClickListener {
        void  onConfirm(Dialog dialog, View view);
        void onCancel(Dialog dialog, View view);
    }

}
