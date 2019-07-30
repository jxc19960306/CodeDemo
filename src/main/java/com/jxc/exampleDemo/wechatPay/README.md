
# 微信支付 Demo
## 微信扫码支付申请
    1. 注册公众号（类型须为：服务号）<br/>
        请根据营业执照类型选择以下主体注册：个体工商户| 企业/公司| 政府| 媒体| 其他类型
    2. 认证公众号 <br/>
        公众号认证后才可申请微信支付，认证费：300元/次
    3. 提交资料申请微信支付 <br/>
        登录公众平台，点击左侧菜单【微信支付】，开始填写资料等待审核，审核时间为1-5个工作日内
    4. 开户成功，登录商户平台进行验证 <br/>
        资料审核通过后，请登录联系人邮箱查收商户号和密码，并登录商户平台填写财付通备付金打的小额资金数额，完成账户验证
    5. 在线签署协议
        本协议为线上电子协议，签署后方可进行交易及资金结算，签署完立即生效
        
## 开发文档
    * appid：微信公众账号或开放平台APP的唯一标识【微信接口方提供】
    * mch_id：商户号(配置文件中的partner)【微信接口方提供】
    * partnerkey：商户密钥【微信接口方提供】
    * ign: 数字签名, 根据微信官方提供的密钥和一套算法生成的一个加密信息, 就是为了保证交易的安全性【partnerkey + 请求参数】
    
### 微信支付 SDK 依赖
~~~ 
    <dependency>
   	<groupId>com.github.wxpay</groupId>
   	<artifactId>wxpay-sdk</artifactId>
   	<version>0.0.3</version>
   </dependency>
~~~
### WXPayUtil.java工具类 常用方法
* 生成随机字符串
    WXPayUtil.generateNonceStr()
* map 集合转换为 XML 字符串 （自动添加签名）
    WXPayUtil.generateSignedXml(param, partnerkey)
* XML 字符串转换为 Map 集合
    WXPayUtil.xmlToMap(result)