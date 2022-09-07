package com.a243.magicbazaar.util;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class PhoneUtil {
    public static void phoneCode(HttpServletResponse response, String phone) {
        String host = "https://jmsms.market.alicloudapi.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "e6575dba4ddc4d9690f49defb59c8009";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        query.put("mobile", phone);
        query.put("templateId", "M72CB42894");

        String code = RandomUtil.code();
        saveCode(response, code);

        query.put("value", code);
        Map<String, String> boys = new HashMap<>();


        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, path, method, headers, query, boys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerPhoneCode(HttpServletResponse response, String phone) {
        String host = "https://jmsms.market.alicloudapi.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "e6575dba4ddc4d9690f49defb59c8009";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        query.put("mobile", phone);
        query.put("templateId", "M72CB42894");

        String code = RandomUtil.code();
        saveRegisterCode(response, code);

        query.put("value", code);
        Map<String, String> boys = new HashMap<>();


        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, path, method, headers, query, boys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loginPhoneCode(HttpServletResponse response, String phone) {
        String host = "https://jmsms.market.alicloudapi.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "e6575dba4ddc4d9690f49defb59c8009";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        query.put("mobile", phone);
        query.put("templateId", "M72CB42894");

        String code = RandomUtil.code();
        saveLoginCode(response, code);

        query.put("value", code);
        Map<String, String> boys = new HashMap<>();


        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, path, method, headers, query, boys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateStaffPhoneCode(HttpServletResponse response, String phone) {
        String host = "https://jmsms.market.alicloudapi.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "e6575dba4ddc4d9690f49defb59c8009";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        query.put("mobile", phone);
        query.put("templateId", "M72CB42894");

        String code = RandomUtil.code();
        saveUpdateStaffCode(response, code);

        query.put("value", code);
        Map<String, String> boys = new HashMap<>();


        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, path, method, headers, query, boys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePhoneCode(HttpServletResponse response, String phone) {
        String host = "https://jmsms.market.alicloudapi.com";
        String path = "/sms/send";
        String method = "POST";
        String appcode = "e6575dba4ddc4d9690f49defb59c8009";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> query = new HashMap<>();
        query.put("mobile", phone);
        query.put("templateId", "M72CB42894");

        String code = RandomUtil.code();
        saveUpdatePhoneCode(response, code);

        query.put("value", code);
        Map<String, String> boys = new HashMap<>();

        try {
            HttpResponse httpResponse = HttpUtil.doPost(host, path, method, headers, query, boys);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(httpResponse.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveCode(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie("phoneCode", code);
        cookie.setMaxAge(60 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static void saveRegisterCode(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie("registerPhoneCode", code);
        cookie.setMaxAge(60 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static void saveLoginCode(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie("loginPhoneCode", code);
        cookie.setMaxAge(60 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static void saveUpdateStaffCode(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie("updateStaffCode", code);
        cookie.setMaxAge(60 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static void saveUpdatePhoneCode(HttpServletResponse response, String code) {
        Cookie cookie = new Cookie("updatePhoneCode", code);
        cookie.setMaxAge(60 * 30);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
