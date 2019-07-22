package com.g7go.common;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTP
 *
 * @author Mr_Lee
 */
public class HttpContext {
    public static final int CR = 13;
    public static final int LF = 10;

    public static final int STATUS_CODE_OK = 200;
    public static final String STATUS_REASON_OK = "OK";

    public static final int STATUS_CODE_NOTFOUND = 404;
    public static final String STATUS_REASON_NOTFOUND = "Not Found";

    public static final int STATUS_CODE_ERROR = 500;
    public static final String STATUS_REASON_ERROR = "Internal Server Error";

    public static final Map<Integer, String> statusMap = new HashMap<Integer, String>();

    public static final Map<String, String> contentTypeMapping = new HashMap<String, String>();

    static {
        initContentTypeMapping();
        initStatus();
    }

    private static void initStatus() {
        statusMap.put(STATUS_CODE_OK, STATUS_REASON_OK);
        statusMap.put(STATUS_CODE_NOTFOUND, STATUS_REASON_NOTFOUND);
        statusMap.put(STATUS_CODE_ERROR, STATUS_REASON_ERROR);
    }


    private static void initContentTypeMapping() {
        System.out.println("ʼContentType");
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File("conf" + File.separator + "web.xml"));
            Element root = doc.getRootElement();
            Element mappingsEle = root.element("type-mappings");
            @SuppressWarnings("unchecked")
            List<Element> mappingList = mappingsEle.elements();
            for (Element mapping : mappingList) {
                String ext = mapping.attribute("ext").getValue();
                String type = mapping.attribute("type").getValue();
                contentTypeMapping.put(ext, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}










