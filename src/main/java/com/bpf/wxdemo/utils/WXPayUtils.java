package com.bpf.wxdemo.utils;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信支付工具类 XML转MAP MAP转XML 生成签名 校验签名等
 * @author baipengfei
 */
public class WXPayUtils {
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            throw ex;
        }

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }

    /**
     * 生成微信支付签名
     * @param params 参数
     * @param key 商户平台设置的密钥key
     * @return
     */
    public static String createSign(SortedMap<String, String> params, String key){
        StringBuilder sb = new StringBuilder();

        //1.对参数按照key=value的格式，并按照参数名ASCII字典序排序
        for (Map.Entry<String, String> entry : params.entrySet()){
            String k = entry.getKey();
            String v = entry.getValue();
            //参数value不能为空,且key不能与特定名称重名
            if(StringUtils.isNotBlank(v) && !"sign".equals(k) && !"key".equals(k)){
                sb.append(k).append("=").append(v).append("&");
            }
        }
        //2.拼接API密钥
        sb.append("key=").append(key);
        return CommonUtils.getMD5(sb.toString());
    }

    /**
     * 校验签名
     * @param params
     * @param key
     * @return
     */
    public static boolean isCorrectSign(SortedMap<String, String> params, String key){
        return createSign(params, key).equals(params.get("sign").toUpperCase());
    }

    public static SortedMap<String, String> getSortedMap(Map<String, String> map){
        SortedMap<String, String> resultMap = new TreeMap<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getValue() != null){
                resultMap.put(entry.getKey(), entry.getValue().trim());
            }
        }
        return resultMap;
    }
}
