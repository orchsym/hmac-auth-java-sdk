package omc.appauth.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.commons.lang3.StringUtils;
 
public class Md5Util {
    public static String md5(String param) {
        if (StringUtils.isBlank(param)) {
            throw new IllegalArgumentException("param can not be null");
        }
        try {
            byte[] bytes = param.getBytes("utf-8");
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(bytes);
            final byte[] enbytes = Base64.getEncoder().encode(md.digest());
            return new String(enbytes);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("unknown algorithm MD5");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
