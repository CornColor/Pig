package browser.pig.cn.pig.bean;

import cn.my.library.net.BaseBean;

/**
 * 作者：liuyutao
 * 创建时间：2020/1/13 下午10:23
 * 类描述：
 * 修改人：
 * 修改内容:
 * 修改时间：
 */
public class BanBenBean extends BaseBean {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
