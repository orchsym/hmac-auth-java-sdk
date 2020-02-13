# HMAC认证SDK使用说明

hmac认证 implemented by Java.

### 简介  
* hmac是Hashing for Message Authentication的简写，可以用来保证数据的完整，客户端把内容通过散列/哈希算法算出一个摘要，并把算法和内容以及摘要传送给服务端，服务端按照这个算法也算一遍，和摘要比一下如果一样就认为内容是完整的，如果不一样就认为内容被篡改了。

### 计算签名
1). 计算queryParam Content-md5.  
2). 构建待签名报文String content = stb.append("X-Date: ").append(hdate).append("\n").append("Content-md5: ").append(contentMD5).append("\n").append("GET").append(" ").append(queryParam).toString();  
3). 构建签名String signature = new String(Base64.getEncoder().encode(HMacSha256Util.signatureReturnBytes(content, secret)));

### 构建request报文：
设置header：  

```   
  Authorization:hmac username="userhmac", algorithm="hmac-sha256", headers="X-Date Content-md5 request-line", signature="p4sGy3B+J/Zqt7gaLJVZCzVY5/Y="
  X-Date:Mon, 31 Jul 2017 07:25:07 GMT
  Content-md5:IgWlVHazOsGgHGVlcKvQDA==
```
### 方法说明：
请求path md5计算:  
`class omc.appauth.common.util.MD5Util`  
方法：`public static String md5(String param)`
|  参数名称  | 说明  |
|  ----  | ----  |  
| param  | 请求path |   

获取日期：  
`omc.appauth.common.util.HMacSha256Util`   
方法：`public static String getDate()`  
  
构建待签名报文：  
`omc.appauth.common.util.HMacSha256Util`  
方法：`public static String getContent(String... contents)`  
|  参数名称  | 说明  |  
|  ----  | ----  |  
| contents  | 拼接报文参数 | 