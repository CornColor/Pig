package browser.pig.cn.pig.login;

import cn.my.library.net.BaseBean;

/**
 * created by dan
 */
public class LoginBean extends BaseBean {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        private String token;
        private String id;
        private String phone;
        private String invitation_code;

        public String getInvitation_code() {
            return invitation_code;
        }

        public void setInvitation_code(String invitation_code) {
            this.invitation_code = invitation_code;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }
}
