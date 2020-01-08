package browser.pig.cn.pig.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import browser.pig.cn.pig.R;
import browser.pig.cn.pig.bean.HomeSelect;
import cn.my.library.ui.adapter.recycleview.CommonAdapter;
import cn.my.library.ui.adapter.recycleview.base.ViewHolder;


/**
 * created by dan
 */
public class HomeSelectAdapter extends CommonAdapter<HomeSelect> {
    OnHomeSelectClickListener onHomeSelectClickListener;
    public HomeSelectAdapter(Context context,List<HomeSelect> datas) {
        super(context, R.layout.item_home_entrance, datas);
    }

    public void setOnHomeSelectClickListener(OnHomeSelectClickListener onHomeSelectClickListener) {
        this.onHomeSelectClickListener = onHomeSelectClickListener;
    }

    @Override
    protected void convert(ViewHolder holder, HomeSelect homeSelect, final int position) {
        ImageView iv_img = holder.getView(R.id.iv_img);
        iv_img.setImageResource(homeSelect.getDrawableId());

        TextView tv_name = holder.getView(R.id.tv_name);
        tv_name.setText(homeSelect.getName());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHomeSelectClickListener.onHomeSelect(position);
            }
        });

    }
    public interface OnHomeSelectClickListener{
        void onHomeSelect(int position);
    }
}
