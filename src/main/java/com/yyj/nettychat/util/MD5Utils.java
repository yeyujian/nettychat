package com.yyj.nettychat.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;

public class MD5Utils {

    private static String salt = "hello";

    /**
     * @Description: 对字符串进行md5加密
     */
    public static String getMD5Str(String strValue) throws Exception {
        strValue += salt;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newstr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newstr;
    }
}
