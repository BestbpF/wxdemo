## 简述
本demo以下功能
- 微信登陆
- 微信支付（Native方式，即商家规定产品价格）
- 微信支付后使用websocket通知客户端支付结果
## 微信官方教程连接
- [微信登陆](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN)
- [微信支付](https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_5)
## 注意
- 微信登陆和微信支付均针对第三方PC网站，且需要用户有真实的网站应用以及商户信息
- 使用内网穿透进行本地测试时，注意下面两点
1. 本地映射外网地址必须与网站应用注册时的redirect_url一致，如果本地无法映射成redirect_url，可以获取到code之后手动调用
2. 微信支付回调地址可以自定义，能映射到本地即可