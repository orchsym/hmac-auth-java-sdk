package omc.appauth.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Orchsym Dev Team
 *
 * A util class for generating required headers for Orchsym Gateway HMAC Authorization.
 */
public class OrchsymHmacAuthGenerator {
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private String username;
    private String secret;
    private String method;
    private String path;
    private String date;
    private String contentMd5;
    private String authHeader;

    /**
     *
     * @param username HMAC username
     * @param secret HMAC secret
     * @param method the method of request to be sent, like "GET", "POST"
     * @param url the URL of the request to be sent
     */
    OrchsymHmacAuthGenerator(String username, String secret, String method, String url) throws MalformedURLException,
            UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        this.username = username;
        this.secret = secret;
        this.method = method.toUpperCase();

        URL urlObj = new URL(url);
        String p = urlObj.getPath();

        // the env prefix of URI, like /env-101, is used for gateway environment proxy,
        // and will not be used in HMAC signature computation.
        p = p.replaceFirst("^/env-\\d+/", "/");

        if (StringUtils.isNoneEmpty(urlObj.getQuery())){
            p = p + "?" + urlObj.getQuery();
        }

        this.path = p;
        this.contentMd5 = str2md5(p);

        Date d = new Date();
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.date = format.format(d);

        this.authHeader = generateAuthorizationHeader();
    }

    private static String str2md5(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (StringUtils.isBlank(input)) {
            throw new IllegalArgumentException("input can not be null");
        }

        byte[] bytes = input.getBytes("utf-8");
        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(bytes);

        final byte[] enbytes = Base64.getEncoder().encode(md.digest());
        return new String(enbytes);
    }

    private String getSignature() throws NoSuchAlgorithmException, InvalidKeyException {
        String signatureStr = String.format("X-Date: %s\nContent-md5: %s\n%s %s", date, contentMd5, method, path);

        SecretKeySpec signingKey = new SecretKeySpec(this.secret.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] signBytes = mac.doFinal(signatureStr.getBytes());

        return new String(Base64.getEncoder().encode(signBytes));
    }

    private String generateAuthorizationHeader() throws InvalidKeyException, NoSuchAlgorithmException {
        String signature = this.getSignature();
        String algorithm = "hmac-sha256";
        return String.format("hmac username=\"%s\",algorithm=\"%s\",headers=\"X-Date Content-md5 request-line\"," +
                "signature=\"%s\"", username, algorithm, signature);
    }

    public String getAuthorization() {
        return this.authHeader;
    }

    public String getDate() {
        return this.date;
    }

    public String getContentMd5(){
        return this.contentMd5;
    }

    public HashMap<String, String> getHmacAuthHeaders() {
        HashMap<String, String> map = new HashMap<>();
        map.put("X-Date", date);
        map.put("Content-md5", contentMd5);
        map.put("Authorization", authHeader);
        return map;
    }

}