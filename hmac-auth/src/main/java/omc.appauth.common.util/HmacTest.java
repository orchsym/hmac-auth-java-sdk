package omc.appauth.common.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


/**
   * @Author Orchsym Dev Team
   * @Time 2020/2/10
   * @Desc HMac加密签名验证代码示例.
 * */
public class HmacTest {
   public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
       test();
   }

   private static void test() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
       String urlStr = "http://172.18.28.240/env-101/por-1/test/api/users/2";
       OrchsymHmacAuthGenerator hmacGen = new OrchsymHmacAuthGenerator("tom", "password", "GET", urlStr);

       System.out.println("X-Date: " + hmacGen.getDate());
       System.out.println("Content-md5: " + hmacGen.getContentMd5());
       System.out.println("Authorization: " + hmacGen.getAuthorization());

       HashMap<String, String> headerMap = hmacGen.getHmacAuthHeaders();

//       OkHttpClient client = new OkHttpClient();
//       Request request = new Request.Builder()
//               .url(urlStr)
//               .addHeader("X-Date", headerMap.get("X-Date"))
//               .addHeader("Content-md5", headerMap.get("Content-md5"))
//               .addHeader("Authorization", headerMap.get("Authorization"))
//               .build();
//
//       Call call = client.newCall(request);
//       Response response = call.execute();
//       System.out.println("Response Code: " + response.code());
//
//       ResponseBody body = response.body();
//       if (body != null){
//           System.out.println("Response Body: " + body.string());
//       }
//
//       response.close();
   }
}