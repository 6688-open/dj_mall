package com.dj.mall.util;

import com.alipay.api.*;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AliPayUtils {

	// 支付宝公钥
	static final String ALIPAYPUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA48LMwCYtnTbcAH1C0d2g990uk/Tic0kMAuVBIrdIwOSTnroa1QfGTwH7lvCRLpuk28cfXlfPxXOuunNGwdN9N9S+MQZh7JtZno3II02+uf5+qkFfH2QApM8NMASQ4WTknxhCsYiKtzbZavW3tIBKCA1mN3zzCuDMcNlECt6rp0oe/J4BglUudsDk3LuDl0Wq+moElP5XGtYXRmG3R74DT+MC6Ox35XhtFQ0lTC+T7BWUY91e6EUgaJYkdlTC99O2ghnexZ9uDYqWy0pcBLJfN0M5FP471VjGoEkfSRANyjDbVWkEr9ob/93aCA9EwD4TxlvI3jkWh9XC5IvWDUBG7QIDAQAB";
	// 应用私钥
	static final String ALIPAYAPPPRIVATEKEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDrc3hFDiGolBOGuR2+CSScz60rwUHHEC9FY3k2ffVaBv+e7a/70lLU3vG5bBnOWIFGyvh3bUvV9jLBGcCxPk4ySn9ubQVTD2TEYDsyIrWh0q6KaPRs7sQ2xRqyU2GqMH1fbVJeBw/XfcGNGYOxqqjcN/mNY1rNLjvDH0Zp3p4j85mAK1/3KSlsRDQRiFxP6mYjUGbhnDrxuHcxyV7Woi5T7J3UqpONZ4n/Znv+MtViY2qmLwu3wGMPWniZKyic5CncdvAlhv++lTkUiA8YZ0quUECuxomlCASBraG8VD6rzR55dSzQx4DGkMx0El30X3EJKXAyCV5CdckAMDtWThpxAgMBAAECggEBANg/lZbXA9LxLX3vR4Hio93iYApv1Rofo0r3sbx6s32cL+qoI5oF8k3Q9fJ3drwuTY3XnZ0SpTQ+ix8PqAsiTm+iPKgPOXSwDrzOJZBGkMuv1N8KxnPGWDB1l69xJaJKJtwebCgwFDy3I62UYYAsXirkvi89sYgBjqIHd4SFEnmw30rwoYfcZhDTAaAMGnn6rl4OVe6tMrrf3jYHRSERzZ3hLHQABMl7G37jWaHirM2wXQMzpf21H2aoFuCUioaF26Eb5FZ84Yq2dYgFSCiyHZHFclqAQAIReKV7PSEqxVFwDxiruUUoZYLQFLyJdZsIdFmCWzHc98pvYxiOnUBhAAECgYEA//qYMQxO+aKZZvc0a4Cj5ZLS6eWRmJTsb7HWMTUn2W860Cf6kOWEvF+thLXVT1Z6R9ToCea2mi+JqJPytvU++iFFmjEl3pZwA01jejvA4r3z5DMdaxnHySMijN2grHt1H0BiLGbExoh83SsOw06Y98QWO8AWNr2ToMqGbf0wLwECgYEA63hxGxQ8HizGn23VZJphJFIr4W/7yyhR+p0NMYrJwBJcSkAHR53zhadZUix7IWaQWs58C9Bg5koTgtDpDTxUm322apA0y6ez+cPmCzUjUTGsxwxA5EjCxa7kfJd9GrYJd1w1oViVQm3BoafhxlDbxpCmGjp0itbIpjPRlgdUW3ECgYEA5580OSV/DKmRjzbeauvy4deSG2zL6rnOm17ahponnbrlB9c1Bgl4qw8ULoJuKqUAbqD6+YKW9jtPn4eH1v9g5epXVadlMd0ELCjJe6A377ObzYBm/TNNsPWm3LnqGkh3+ThD3RN8uHxmJ6Y/bIBhL8hxqc0tDIlL3PGE5F92SwECgYAlV2dQbQgPnPgupkNV/elz0hglWxXqJM3DWY6TEbhm3kKGf9rnr5PD2UMf9e/c3x/7Vo1zbis/ufWYJDshHzUnp2r7QT0uIr022asskdO095S3yCiuSx+gK9kOLRPo3X9oz1Wt7ZdV/oPbd/0k7wO7XIsK08RRlDgjq0oMacMl8QKBgH06Sl2vUyIiRzORubG5E9LKcvRvQRTEUil4rBZJhXrp1F2/WiC/7uZ9M1XQiKHW//PGDuoznuAreB1G3XPL44a+vqJwOFx8jcCV3aTx8L6oPnOL5bynCg3al9XwX0m0S+l7wEAZkqml60nw5y+Pj0QE8O3xochbxhv/TER4zvmH";
	// APPID
	static final String APPID = "2016092600599245";
	/**
	 * 支付 阿里
	 * @param orderNum 订单号
	 * @param totalAmount	总金额
	 * @param subject 商品名称
	 * @return
	 * @throws Exception
	 */
	public  static String toAliPay(String orderNum, Double totalAmount,
			String subject, String token) throws Exception {
		String aliPayGateWayUrl = "https://openapi.alipaydev.com/gateway.do";
		AlipayClient alipayClient = new DefaultAlipayClient(aliPayGateWayUrl, APPID, ALIPAYAPPPRIVATEKEY,
				AlipayConstants.FORMAT_JSON, AlipayConstants.CHARSET_UTF8, ALIPAYPUBLICKEY,
				AlipayConstants.SIGN_TYPE_RSA2); 
		AlipayRequest alipayRequest = new AlipayTradeWapPayRequest();
		AlipayTradeWapPayModel alipayTradeWapPayModel = new AlipayTradeWapPayModel();
		alipayTradeWapPayModel.setOutTradeNo(orderNum);// 订单号
		alipayTradeWapPayModel.setTotalAmount(String.valueOf(totalAmount));// 总金额
		alipayTradeWapPayModel.setSubject(URLEncoder.encode(subject,"utf-8"));// 商品名称
		alipayTradeWapPayModel.setProductCode("QUICK_WAP_PAY"); // WAP：手机APP和浏览器
		alipayRequest.setBizModel(alipayTradeWapPayModel);
		alipayRequest.setReturnUrl("http://2t45i18784.qicp.vip:16916/order/aliPaySuccess?token="+token);// 同步url地址 支付宝成功后返回的页面
		alipayRequest.setNotifyUrl("http://2t45i18784.qicp.vip:16916/order/aliPayCallBack"); // 异步url地址  支付宝回调函数 修改数据库内自己的订单状态
		return alipayClient.pageExecute(alipayRequest).getBody();
	}
	/**
	 *	WAIT_BUYER_PAY	交易创建，等待买家付款
	 *	TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款
	 *	TRADE_SUCCESS	交易支付成功
	 *	TRADE_FINISHED	交易结束，不可退款
	 */
	/**
	 * 程序执行完后必须打印输出“success”（不包含引号）。
	 * 如果商户反馈给支付宝的字符不是success这7个字符，
	 * 支付宝服务器会不断重发通知，
	 * 直到超过24小时22分钟。在25小时内完成6~10次通知
	 * （通知频率：5s,2m,10m,15m,1h,2h,6h,15h）。
	 * */
	public synchronized static Map<String, String> aliPayCallBack(HttpServletRequest request) throws AlipayApiException {
		Map<String, String> parameterMap = getParameterMap(request);
		// 该请求是否来源于阿里  验签
		boolean signVerified = AlipaySignature
	          .rsaCheckV1(parameterMap, ALIPAYPUBLICKEY, AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);
		System.out.println(parameterMap);
		// 判断订单状态是否成功
		//	支付时传入的商户订单号 out_trade_no
		//支付宝28位交易号 trade_no
		if(signVerified && "TRADE_SUCCESS".equals(parameterMap.get("trade_status"))) {
			 Map<String,String> map = new HashMap<String,String>();
			 // 商家订单号
			String merchant  = parameterMap.get("out_trade_no");
			// 支付宝的订单号
			String aliPay = parameterMap.get("trade_no");
			map.put("merchant_order_number", merchant);
			map.put("alipay_transaction_serial_number", aliPay);
			map.put("status", parameterMap.get("trade_status"));
			return map;
		}
		// return fail/success;
		return null;
	}
	
	/**
	 * 将request中的数据转换成map
	 * @param request
	 * @return
	 */
	public static Map<String,String> getParameterMap(HttpServletRequest request) {
        Map paramsMap = request.getParameterMap(); 
        Map<String,String> returnMap = new HashMap();
        Iterator entries = paramsMap.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
}
