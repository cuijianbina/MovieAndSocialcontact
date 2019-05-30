package com.zjweu.Movie.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class sendsms {

	private static String Url = "http://106.ihuyi.com/webservice/sms.php?method=Submit";

	public static void main(String[] args) {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);
		client.getParams().setContentCharset("GBK");
		method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=GBK");
		int mobile_code = (int) ((Math.random() * 9 + 1) * 100000);
		String content = new String("您的验证码是：" + mobile_code + "。请不要把验证码泄露给其他人。");
		NameValuePair[] data = { // 提交短信
				new NameValuePair("account", "C24676762"), // 查看用户名
														// 登录用户中心->验证码通知短信>产品总览->API接口信息->APIID
				new NameValuePair("password", "e9039fdb2380f1d6d9ec3ee77bae56c4  "), // 查看密码
														// 登录用户中心->验证码通知短信>产品总览->API接口信息->APIKEY
				// new NameValuePair("password",
				// util.StringUtil.MD5Encode("密码")),
				new NameValuePair("mobile", "18257633119"), new NameValuePair("content", content), };
		method.setRequestBody(data);
		try {
			client.executeMethod(method);
			String SubmitResult = method.getResponseBodyAsString();
			// System.out.println(SubmitResult);
			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();
			String code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");
			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);
			if ("2".equals(code)) {
				System.out.println("短信提交成功");
			}
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}