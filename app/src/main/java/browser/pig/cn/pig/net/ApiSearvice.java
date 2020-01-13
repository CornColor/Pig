package browser.pig.cn.pig.net;



/**
 * created by dan
 */
public interface ApiSearvice {
    String HOST = "http://zhuyouyou.wanchao.org/api/";
    /**
     * 登录
     */
    String LOGIN = HOST+"login";

    /**
     * 注册
     */
    String REGISTER = HOST+"register";

    /**
     * 发送短信验证码
     */
    String SEND_REGISTER_CODE = HOST+"register/sendcode";

    /**
     * 发送找回密码验证码
     */
    String SEND_FIND_PASSWORD_CODE = HOST+"login/sendcode";

    /**
     * 找回密码
     */
    String FIND_PASSWORD= HOST+"login/findpassword";

    /**
     * 修改密码
     */
    String MODIFY_PASSWORD = HOST+"emp/updatepassword";
    /**
     * 判断版本
     */
    String Y_CODE = HOST+"system/androidversion";
    /**
     * 更新地址
     */
    String UPDATA_ADDRESS = HOST+"system/androidaddress";




}
