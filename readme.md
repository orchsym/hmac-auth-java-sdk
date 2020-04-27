# HMAC认证SDK使用说明

Java版本的SDK，用于生成Orchsym API Gateway HMAC 认证所需的请求头。

支持 Java 8+。



### HMAC简介

* HMAC是Hashing for Message Authentication的简写，可以用来保证数据的完整，客户端把内容通过散列/哈希算法算出一个摘要，并把算法和内容以及摘要传送给服务端，服务端按照这个算法也算一遍，和摘要比一下如果一样就认为内容是完整的，如果不一样就认为内容被篡改了。

### 计算签名

1). 计算 `X-Date`、`Content-md5` 参数  
2). 构建待签名报文字符串

```
String signatureStr = stb.append("X-Date: ")
.append(hdate)
.append("\n")
.append("Content-md5: ")
.append(contentMD5)
.append("\n")
.append("GET")
.append(" ")
.append(path)
.toString();
```


3). 对签名字符串使用SHA256算法进行HMAC加密

```
SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);
Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
mac.init(signingKey);
byte[] signBytes = mac.doFinal(signatureStr.getBytes());
return new String(Base64.getEncoder().encode(signBytes));
```

### 

### 构建请求头报文：

设置header：  

```
  Authorization:hmac username="userhmac", algorithm="hmac-sha256", headers="X-Date Content-md5 request-line", signature="p4sGy3B+J/Zqt7gaLJVZCzVY5/Y="
  X-Date:Mon, 31 Jul 2017 07:25:07 GMT
  Content-md5:IgWlVHazOsGgHGVlcKvQDA==
```

### 方法说明：

#### OrchsymHmacAuthGenerator(String username, String secret, String method, String url)

描述：生成 `OrchsymHmacAuthGenerator` 实例，用于获取HMAC认证所需的请求头。

参数：

- username: HMAC 认证用户名

- secret: HMAC 认证秘钥

- request_method: 请求方法

- url: 请求完整路径，比如: `http://www.example.com/a/b/c?type=1`



#### OrchsymHmacAuthGenerator.getHmacAuthHeaders()

描述：获取HMAC认证所需的请求头

返回：

```
// (HashMap) headerName -> headerValue

{
    "X-Date": "Mon, 31 Jul 2017 07:25:07 GMT",
    "Content-md5": "IgWlVHazOsGgHGVlcKvQDA==",
    "Authorization": "hmac username=\"userhmac\", algorithm=\"hmac-sha256\", headers=\"X-Date Content-md5 request-line\", signature=\"p4sGy3B+J/Zqt7gaLJVZCzVY5/Y=\""
}
```



### 代码示例：

```
       String urlStr = "http://172.18.28.240/env-101/por-1/test/api/users/2";
       OrchsymHmacAuthGenerator hmacGen = new OrchsymHmacAuthGenerator("tom", "password", "GET", urlStr);

       System.out.println("X-Date: " + hmacGen.getDate());
       System.out.println("Content-md5: " + hmacGen.getContentMd5());
       System.out.println("Authorization: " + hmacGen.getAuthorization());

       HashMap<String, String> headerMap = hmacGen.getHmacAuthHeaders();

       OkHttpClient client = new OkHttpClient();
       Request request = new Request.Builder()
               .url(urlStr)
               .addHeader("X-Date", headerMap.get("X-Date"))
               .addHeader("Content-md5", headerMap.get("Content-md5"))
               .addHeader("Authorization", headerMap.get("Authorization"))
               .build();

       Call call = client.newCall(request);
       Response response = call.execute();
       System.out.println("Response Code: " + response.code());

       ResponseBody body = response.body();
       if (body != null){
           System.out.println("Response Body: " + body.string());
       }

       response.close();
```


