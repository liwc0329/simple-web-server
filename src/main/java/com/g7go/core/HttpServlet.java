package com.g7go.core;

import java.io.File;

import com.g7go.common.HttpContext;
import com.g7go.http.HttpResponse;

/**
 * Http
 *
 * @author Mr_Lee
 */
public class HttpServlet {
    public void forward(String uri, HttpResponse response) throws Exception {
        File file = new File("webapp" + uri);
        response.setStatus(HttpContext.STATUS_CODE_OK);
        response.setContentType("text/html");
        response.setContentLength((int) file.length());
        response.setEntity(file);
        response.flush();
    }
}
