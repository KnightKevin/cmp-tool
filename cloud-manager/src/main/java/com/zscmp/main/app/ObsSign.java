package com.zscmp.main.app;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class ObsSign {
    private static final String SIGN_SEP = "\n";
    private static final String OBS_PREFIX = "x-obs-";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final List<String> SUB_RESOURCES = Collections.unmodifiableList(Arrays.asList(
            "CDNNotifyConfiguration", "acl", "append", "attname", "cors", "customdomain", "delete",
            "deletebucket", "encryption", "inventory", "length", "lifecycle", "location", "logging",
            "metadata", "mirrorBackToSource", "modify", "name",
            "partNumber", "policy", "position", "quota", "rename", "replication", "response-cache-control",
            "response-content-disposition", "response-content-encoding", "response-content-language",
            "response-content-type",
            "response-expires", "storagePolicy", "storageinfo", "tagging", "torrent", "truncate",
            "uploadId", "uploads", "versionId", "versioning", "versions", "website",
            "x-obs-security-token", "object-lock", "retention"));
    // private static String ak;
    // private static String sk;

    private static String urlEncode(String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, DEFAULT_ENCODING)
                .replaceAll("%7E", "~") // for browser
                .replaceAll("%2F", "/")
                .replaceAll("%20", "+");
    }

    private static String join(List<?> items, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            String item = items.get(i).toString();
            sb.append(item);
            if (i < items.size() - 1) {
                sb.append(delimiter);
            }
        }
        return sb.toString();

    }

    private static boolean isValid(String input) {
        return input != null && !input.equals("");
    }

    public static String hamcSha1(String sk, String input) throws NoSuchAlgorithmException, InvalidKeyException,
            UnsupportedEncodingException {
        SecretKeySpec signingKey = new SecretKeySpec(sk.getBytes(DEFAULT_ENCODING), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return Base64.getEncoder().encodeToString(mac.doFinal(input.getBytes(DEFAULT_ENCODING)));
    }

    private static String stringToSign(String httpMethod, Map<String, String[]> headers, Map<String, String> queries,
            String bucketName, String objectName) throws Exception {
        String contentMd5 = "";
        String contentType = "";
        String date = "";
        TreeMap<String, String> canonicalizedHeaders = new TreeMap<String, String>();
        String key;
        List<String> temp = new ArrayList<String>();
        for (Map.Entry<String, String[]> entry : headers.entrySet()) {
            key = entry.getKey();
            if (key == null || entry.getValue() == null || entry.getValue().length == 0) {
                continue;
            }
            key = key.trim().toLowerCase(Locale.ENGLISH);
            if (key.equals("content-md5")) {
                contentMd5 = entry.getValue()[0];
                continue;
            }
            if (key.equals("content-type")) {
                contentType = entry.getValue()[0];
                continue;
            }
            if (key.equals("date")) {
                date = entry.getValue()[0];
                continue;
            }
            if (key.startsWith(OBS_PREFIX)) {
                for (String value : entry.getValue()) {
                    if (value != null) {
                        temp.add(value.trim());
                    }
                }
                canonicalizedHeaders.put(key, join(temp, ","));
                temp.clear();
            }
        }
        if (canonicalizedHeaders.containsKey("x-obs-date")) {
            date = "";
        }
        // handle method/content-md5/content-type/date
        StringBuilder stringToSign = new StringBuilder();
        stringToSign.append(httpMethod).append(SIGN_SEP)
                .append(contentMd5).append(SIGN_SEP)
                .append(contentType).append(SIGN_SEP)
                .append(date).append(SIGN_SEP);
        // handle canonicalizedHeaders
        for (Map.Entry<String, String> entry : canonicalizedHeaders.entrySet()) {
            stringToSign.append(entry.getKey()).append(":").append(entry.getValue()).append(SIGN_SEP);
        }
        // handle CanonicalizedResource
        stringToSign.append("/");
        if (isValid(bucketName)) {
            stringToSign.append(bucketName).append("/");
            if (isValid(objectName)) {
                stringToSign.append(urlEncode(objectName));
            }
        }
        TreeMap<String, String> canonicalizedResource = new TreeMap<String, String>();
        for (Map.Entry<String, String> entry : queries.entrySet()) {
            key = entry.getKey();
            if (key == null) {
                continue;
            }
            if (SUB_RESOURCES.contains(key)) {
                canonicalizedResource.put(key, entry.getValue());
            }
        }
        if (canonicalizedResource.size() > 0) {
            stringToSign.append("?");
            for (Map.Entry<String, String> entry : canonicalizedResource.entrySet()) {
                stringToSign.append(entry.getKey());
                if (isValid(entry.getValue())) {
                    stringToSign.append("=").append(entry.getValue());
                }
                stringToSign.append("&");
            }
            stringToSign.deleteCharAt(stringToSign.length() - 1);
        }
        // System.out.println(String.format("StringToSign:%s%s", SIGN_SEP,
        // stringToSign.toString()));
        return stringToSign.toString();
    }

    public static String headerSignature(String ak, String sk, String httpMethod, Map<String, String[]> headers,
            Map<String, String> queries,
            String bucketName, String objectName) throws Exception {
        // 1. stringToSign
        String stringToSign = stringToSign(httpMethod, headers, queries, bucketName, objectName);
        // 2. signature
        return String.format("OBS %s:%s", ak, hamcSha1(sk, stringToSign));
    }

    public static String querySignature(String ak, String sk, String httpMethod, Map<String, String[]> headers,
            Map<String, String> queries,
            String bucketName, String objectName, long expires) throws Exception {
        if (headers.containsKey("x-obs-date")) {
            headers.put("x-obs-date", new String[] { String.valueOf(expires) });
        } else {
            headers.put("date", new String[] { String.valueOf(expires) });
        }
        // 1. stringToSign
        String stringToSign = stringToSign(httpMethod, headers, queries, bucketName, objectName);
        // 2. signature
        return urlEncode(hamcSha1(sk, stringToSign));
    }

    public static String formatDate(long time) {
        DateFormat serverDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",
                Locale.ENGLISH);
        serverDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return serverDateFormat.format(time);
    }

    public static void main(String[] args) throws Exception {

        String requestTime = formatDate(System.currentTimeMillis());

        String contentMD5 = "";
        String contentType = "";
        String canonicalizedHeaders = "";
        String canonicalizedResource = "/fffff/rr";
        // Content-MD5 、Content-Type 没有直接换行， data格式为RFC 1123，和请求中的时间一致
        String canonicalString = "PUT" + "\n" + contentMD5 + "\n" + contentType + "\n" + requestTime + "\n"
                + canonicalizedHeaders + canonicalizedResource;
        System.out.println("StringToSign:[" + canonicalString + "]");

        String ak = "VUEHLSJF7AWAMYZAL4HC";
        String sk = "juv3j2d0AI0o2jF4Ijy3lSxxPAvlCsqIGO0RfIjY";


        Map<String, String[]> headers = new HashMap<>();
        headers.put("x-obs-date", new String[]{requestTime});
        // headers.put("x-obs-grant-full-control", new String[]{"id=7a6d5e18a8e4448aacfdaaadf732c9d1"});

        // String signature = signWithHmacSha1(sk, canonicalString);
        String signature = headerSignature(ak, sk, "PUT", headers, Collections.emptyMap(), "fffff", "rr/dd");



        // Map<String, String[]> headers = new HashMap();
        // signature = headerSignature(ak, sk, "PUT", headers, Collections.emptyList(), "fffff", "ccc")

        System.out.println(signature);
    }

    public static String signWithHmacSha1(String sk, String canonicalString) throws UnsupportedEncodingException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(sk.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return Base64.getEncoder().encodeToString(mac.doFinal(canonicalString.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}