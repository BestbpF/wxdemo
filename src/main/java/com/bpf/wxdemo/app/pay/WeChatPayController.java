package com.bpf.wxdemo.app.pay;

import com.bpf.wxdemo.config.WeChatConfig;
import com.bpf.wxdemo.constant.PayConstant;
import com.bpf.wxdemo.utils.WXPayUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * 微信支付controller
 * @author baipengfei
 */
@RestController
@RequestMapping("pay")
public class WeChatPayController {

    @Autowired
    private PayService payService;
    @Autowired
    private WeChatConfig weChatConfig;

    /**
     * 根据code_url生成二维码图片，然后输出
     * @param response
     * @throws Exception
     */
    @GetMapping("code_url")
    public void toCodeUrl(HttpServletResponse response) throws Exception {
        //获取code_url
        String codeUrl = payService.getCodeUrl();
        if(codeUrl == null) {
            throw new NullPointerException("code_url is null");
        }

        try {
            //生成二维码配置
            Map<EncodeHintType, Object> hints = new HashMap<>(16);
            //设置纠错等级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            //设置编码类型
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, 400, 400, hints);
            OutputStream out = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 接收微信方支付支付结果通知
     * @param request
     * @param response
     */
    @PostMapping("callback")
    public void payCallBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        InputStream inputStream = request.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null){
            sb.append(line);
        }
        in.close();
        inputStream.close();

        final Map<String, String> payCallBackMap = WXPayUtils.xmlToMap(sb.toString());
        SortedMap<String, String> map = WXPayUtils.getSortedMap(payCallBackMap);

        System.out.println(map);
        //校验签名sign
        if(WXPayUtils.isCorrectSign(map, weChatConfig.getKey())){
            if ("SUCCESS".equals(map.get("result_code"))){
                //进行一些相关操作，如可以通过out_trade_no找到订单，将订单的待支付状态改为已支付
                String outTradeNo = map.get("out_trade_no");
                //【注意！！】同样的通知可能会多次发送给商户系统，【商户系统必须能够正确处理重复的通知】。如果已处理过，直接给微信支付返回成功。
                //下面直接返回成功
                boolean paid = true;
                if(paid){
                    //更新成功 通知微信订单支付处理成功
                    Map<String, String> tmp = new HashMap<>(2);
                    tmp.put("return_code", "SUCCESS");
                    tmp.put("return_msg", "OK");
                    String notifyBackStr = WXPayUtils.mapToXml(tmp);
                    response.setContentType("text/xml");
                    response.getWriter().println(notifyBackStr);
                    //通知客户端支付成功：客户端轮询或websocket，这里用websocket去推送
                    payService.notifyClientPaidState(PayConstant.PAID_SUCCESSFUL);
                }

            } else {
                //通知客户端支付失败
                payService.notifyClientPaidState(PayConstant.PAID_FAILED);
            }
        }
        //暂时不作操作
    }

    /**
     * 跳转到扫描二维码页面
     * @return
     */
    @GetMapping("page")
    public ModelAndView toPayPage() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("pay_page");
        return mv;
    }
}
