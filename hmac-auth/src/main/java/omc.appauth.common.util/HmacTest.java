package omc.appauth.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import omc.appauth.common.util.HMacSha256Util;

  /**
   * @Author orchsym
   * @Time 2020/2/10
   * @Desc HMac加密签名验证.
   */
   public class HmacTest {
   public static void main(String[] args) {
       test();
   }

   static void test(){
       String queryParam = "/testHmac/qryParam=test1&pageNo=1";
       String contentMD5 = Md5Util.md5(queryParam);
       Date d=new Date();
       DateFormat format=new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
       format.setTimeZone(TimeZone.getTimeZone("GMT"));
       String hdate = format.format(d);

       String content = HMacSha256Util.getContent("X-Date: ", hdate, "\n", "Content-md5: ", contentMD5, "\n", "GET", " ", queryParam);
       String secret = "123456";  //用户userhmac的密钥
       try {
          String base64Singature = new String(Base64.getEncoder().encode(HMacSha256Util.signatureReturnBytes(content, secret)));
          System.out.println(base64Singature);
      } catch (Exception e) {
          e.printStackTrace();
      }
   }
}