package com.bpf.wxdemo.app.login;

import com.bpf.wxdemo.config.WeChatConfig;
import com.bpf.wxdemo.constant.WxUrlConstant;
import com.bpf.wxdemo.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.util.Objects;

/**
 * 微信登陆controller
 * @author baipengfei
 */
@RestController
@RequestMapping("login")
public class WeChatLoginController {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * 拼装获取二维码链接，打开该连接即可扫描二维码
     * @return
     * @throws UnsupportedEncodingException
     */
    @GetMapping("qrcode_url")
    public Object getQrcodeUrl(HttpSession session) throws UnsupportedEncodingException {
        //1、获取开发平台重定向地址，并使用urlEncode对链接进行处理
        String redirectUrl = URLEncoder.encode(weChatConfig.getRedirectUrl(), "GBK");
        //2、创建state，用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击）
        String state = CommonUtils.generateUUID();
        session.setAttribute("state", state);
        //3、使用urlEncode对链接进行处理
        return String.format(WxUrlConstant.OPEN_QRCODE_URL, weChatConfig.getOpenAppId(), redirectUrl, state);
    }

    /**
     * 扫描登陆后微信重定向地址，这里请求到用户数据后直接返回给浏览器
     * @param code code 用于请求accessToken
     * @param state 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击）
     * @param session session
     * @return
     */
    @GetMapping("callback")
    public Object loginCallBack(@RequestParam(value = "code") String code, @RequestParam(value = "state") String state, HttpSession session) {
        return valid(session, state) ? userInfoService.getUserInfo(code) : null;
    }

    /**
     * 严重state是否正确
     * @param session httpSession
     * @param state 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击）
     * @return true or false
     */
    private boolean valid(HttpSession session, String state) {
        return Objects.equals(session.getAttribute("state"), state);
    }


}
