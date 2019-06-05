package com.bpf.wxdemo.app.login;

import com.bpf.wxdemo.config.WeChatConfig;
import com.bpf.wxdemo.constant.WxUrlConstant;
import com.bpf.wxdemo.utils.FastJsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 微信用户service
 * @author baipengfei
 */
@Service
public class UserInfoService {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private RestTemplate restTemplate;

    public User getUserInfo(String code) {
        //1、获取accessToken和openId
        String accessTokenUrl = String.format(WxUrlConstant.OPEN_ACCESS_TOKEN_URL,
                weChatConfig.getOpenAppId(), weChatConfig.getOpenAppSecret(), code);
        String resStr = restTemplate.getForObject(accessTokenUrl, String.class);
        Map result = FastJsonUtils.toMap(resStr);
        if (result == null || result.isEmpty()) {
            return null;
        }
        String accessToken = (String) result.get("access_token");
        String openId = (String) result.get("openid");

        //2、获取用户基本信息
        String userInfoUrl = String.format(WxUrlConstant.OPEN_USER_INFO, accessToken, openId);
        String userStr = restTemplate.getForObject(userInfoUrl, String.class);
        Map userInfoMap = FastJsonUtils.toMap(userStr);
        return buildUser(userInfoMap, openId);
    }

    private User buildUser(Map userInfoMap, String openId) {
        if (userInfoMap == null || userInfoMap.isEmpty()) {
            return null;
        }
        String name = (String) userInfoMap.get("nickname");
        //解决中文乱码
        name = new String(name.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Integer sex = (Integer) userInfoMap.get("sex");
        String province = (String) userInfoMap.get("province");
        String city = (String) userInfoMap.get("city");
        String country = (String) userInfoMap.get("country");
        String headimgurl = (String) userInfoMap.get("headimgurl");
        //中国||山西||太原
        String address = country + "||" + province + "||" + city;
        //解决中文乱码
        address = new String(address.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        //封装用户信息并存储
        return new User(openId, name, headimgurl, sex, address);
    }
}
