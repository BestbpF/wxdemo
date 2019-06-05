package com.bpf.wxdemo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 微信相关参数配置
 * @author baipengfei
 */
@Configuration
@Data
public class WeChatConfig {

    /**
     * 开放平台appid
     */
    @Value("${wxopen.appid}")
    private String openAppId;

    /**
     * 开放平台秘钥
     */
    @Value("${wxopen.appsecret}")
    private String openAppSecret;

    /**
     * 开放平台回调URL
     */
    @Value("${wxopen.redirect_uri}")
    private String redirectUrl;

    /**
     * 公众号appid
     */
    @Value("${wxpay.appid}")
    private String payAppId;

    /**
     * 公众号秘钥
     */
    @Value("${wxpay.appsecret}")
    private String payAppSecret;

    /**
     * 商户号
     */
    @Value("${wxpay.mch_id}")
    private String mchId;

    /**
     * 支付key
     */
    @Value("${wxpay.key}")
    private String key;

    /**
     * 支付回调url
     */
    @Value("${wxpay.notify_url}")
    private String payNotifyUrl;
}
