package com.rainbow.bridge.core.base.result;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class JsonResult<T> {
    private static Logger logger = LoggerFactory.getLogger(JsonResult.class);
    private static Map<String, Map<Integer, String>> localMap = new HashMap<>();
    private static String language = "english";

    public static void init(String defaultLocal, String localName, File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        init(defaultLocal, localName, inputStream);
    }

    public static void init(String localName, File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        init(null, localName, inputStream);
    }

    public static void init(String localName, InputStream inputStream) throws IOException {
        init(null, localName, inputStream);
    }

    public static void init(String defaultLocal, String localName, InputStream inputStream) throws IOException {
        if (StringUtils.isNotBlank(defaultLocal)) {
            language = defaultLocal;
        }
        if (inputStream == null) {
            logger.error("language={} ,inputStream={}", localName, inputStream);
            return;
        }
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            String name = properties.getProperty("language");
            if (StringUtils.isNotBlank(name)) {
                localName = name;
            }
            if (StringUtils.isBlank(localName)) {
                logger.error("language==null ");
                return;
            }
            Enumeration<?> keys = properties.propertyNames();
            Map<Integer, String> map = new HashMap<>();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                if (StringUtils.equals(name, key)) {
                    continue;
                }
                boolean digits = NumberUtils.isDigits(key);
                if (!digits) {
                    continue;
                }
                int code = NumberUtils.toInt(key);
                String message = properties.getProperty("message");
                map.put(code, message);
            }
            if (MapUtils.isEmpty(map)) {
                return;
            }
            localMap.put(localName, map);
        } catch (IOException e) {
            logger.error("load language fail", e);
        } finally {
            inputStream.close();
        }
    }

    private int code;
    private String message;
    private T data;

    public JsonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> JsonResult success(T data) {
        return success(data, language);
    }

    public static <T> JsonResult success(T data, String language) {
        ResultType resultType = ResultType.SUCCESS;

        return new JsonResult(resultType.getCode(), getMessage(resultType, language), data);
    }

    private static String getMessage(ResultType resultType, String language) {
        int code = resultType.getCode();
        String message = resultType.getMessage();
        if (StringUtils.isNotBlank(language)) {
            Map<Integer, String> map = localMap.get(language);
            if (MapUtils.isEmpty(map)) {
                return message;
            }
            String localMess = map.get(code);
            if (StringUtils.isNotBlank(localMess)) {
                message = localMess;
            }
        }
        return message;
    }


    public static JsonResult fail(ResultType resultType) {
        if (resultType == null) {
            return null;
        }
        return new JsonResult(resultType.getCode(), resultType.getMessage(), null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
