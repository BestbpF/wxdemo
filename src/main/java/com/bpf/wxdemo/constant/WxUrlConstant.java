package com.bpf.wxdemo.constant;

/**
 * 微信平台相关接口
 * @author baipengfei
 */
public class WxUrlConstant {
    /**
     * 开放平台登录二维码链接
     */
    public final static String OPEN_QRCODE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login&state=%s#wechat_redirect";

    /**
     * 开放平台获取accessToken地址
     */
    public final static String OPEN_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 开放平台获取用户信息
     */
    public final static String OPEN_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * 微信支付统一下单地址
     */
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
