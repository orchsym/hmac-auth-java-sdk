package omc.appauth.common.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

public class HMacSha256Util {
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    
    private static String toHexString(byte[] bytes) {
        String formatStr = null;
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        formatStr = formatter.toString();
        formatter.close();
        return formatStr;
    }
 
    public static String signature(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(data.getBytes()));
    }
 
    public static byte[] signatureReturnBytes(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        return mac.doFinal(data.getBytes());
    }
    
    public static String getDate() {
        Date d = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(d);
    }

    public static String getContent(String date) {
        StringBuilder stb = new StringBuilder();
        String content = stb.append("X-Date: ").append(date).toString();
        return content;
    }

    public static String getContent(String... contents) {
        StringBuffer stb = new StringBuffer();
        for (String content:contents) {
            stb.append(content);
        }
        return stb.toString();
    }
 
    public static void main(String[] args) throws Exception {
        
//        String queryParam = "/testHmac/qryParam=test1&pageNo=1";
        String queryParam = "/por-801/test01/mocky/v2/5e4101d22f00004b0058313f";
        String method = "GET";

        String contentMD5 = Md5Util.md5(queryParam);
        System.out.println("Content-md5: "+contentMD5);
 
        String hdate = getDate();
        System.out.println("X-Date: "+hdate);
 
        String content = getContent("X-Date: ", hdate, "\n", "Content-md5: ", contentMD5, "\n", method, " ", queryParam);
        System.out.println("签名前内容: "+content);
        String password = "WMsETBIrtPfjAlbqUVsSxf198SAEUOkh";  //用户userhmac的密钥
        try {
            String base64Singature = new String(Base64.getEncoder().encode(signatureReturnBytes(content, password)));
            System.out.println("显示指定编码64[推荐]:" + base64Singature);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}