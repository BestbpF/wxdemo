package com.bpf.wxdemo.app.pay;

import com.bpf.wxdemo.app.socket.WebSocketService;
import com.bpf.wxdemo.config.WeChatConfig;
import com.bpf.wxdemo.constant.WxUrlConstant;
import com.bpf.wxdemo.utils.CommonUtils;
import com.bpf.wxdemo.utils.WXPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 微信支付服务
 * @author baipengfei
 */
@Service
public class PayService {

    @Autowired
    private WeChatConfig weChatConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebSocketService socketService;

    /**
     * 获取微信支付二维码地址
     * @return
     * @throws Exception
     */
    public String getCodeUrl() throws Exception {
        //生成签名
        //1.封装请求参数 （一些有关商品的数据是假的，实际情况可以从controller层穿购买的商品id进来，去查询商品数据）
        SortedMap<String, String> params = new TreeMap<>();
        params.put("appid", weChatConfig.getPayAppId());
        params.put("mch_id", weChatConfig.getMchId());
        params.put("nonce_str", CommonUtils.generateUUID());
        params.put("body", "测试商品");
        params.put("out_trade_no", CommonUtils.generateUUID());
        params.put("total_fee", "10");
        params.put("spbill_create_ip", "127.0.0.1");
        params.put("notify_url", weChatConfig.getPayNotifyUrl());
        params.put("trade_type", "NATIVE");
        params.put("product_id", CommonUtils.generateUUID());

        //2.根据参数生成sign
        String sign = WXPayUtils.createSign(params, weChatConfig.getKey());
        //3.完成最后封装
        params.put("sign", sign);
        //4.参数转为XML
        String payXml = WXPayUtils.mapToXml(params);

        //5.调用统一下单接口,得到响应结果
        HttpHeaders headers = new HttpHeaders();
        //这里注意！官方规定统一采用UTF-8字符编码
        headers.setContentType(MediaType.parseMediaType("text/xml; charset=UTF-8"));
        HttpEntity<String> entity = new HttpEntity<>(payXml, headers);
        String resultXmlStr = restTemplate.postForObject(new URI(WxUrlConstant.UNIFIED_ORDER_URL), entity, String.class);
        if (resultXmlStr == null) {
            return null;
        }
        resultXmlStr = new String(resultXmlStr.getBytes(StandardCharsets.ISO_8859_1), UTF_8);
        Map<String, String> resultMap = WXPayUtils.xmlToMap(resultXmlStr);
        if (resultMap != null) {
            //返回code_url
            return resultMap.get("code_url");
        }
        return null;
    }

    /**
     * 通知浏览器支付结果
     * @param paidState
     */
    public void notifyClientPaidState(int paidState) {
        socketService.sendMessage("/topic/pay", paidState);
    }

}
