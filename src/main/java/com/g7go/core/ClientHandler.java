package com.g7go.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.g7go.common.HttpContext;
import com.g7go.http.HttpRequest;
import com.g7go.http.HttpResponse;
import com.g7go.service.LoginServlet;
import com.g7go.service.RegServlet;

/**
 * 该线程任务用于处理每个客户端的请求
 * @author Mr_Lee
 *
 */
public class ClientHandler implements Runnable{
	private Socket socket;
	public ClientHandler(Socket socket){
		this.socket = socket;
	}
	
	public void run(){
		try {
			InputStream in = socket.getInputStream();
			//创建对应的请求对象
			HttpRequest request = new HttpRequest(in);
			OutputStream out = socket.getOutputStream();
			//创建对应的响应对象
			HttpResponse response = new HttpResponse(out);
			
			/*
			 * 处理用户请求
			 */
			//获取用户请求资源路径
			/*
			 *  /index.html
			 *  /reg
			 */
			String uri = request.getUri();
			System.out.println("uri:"+uri);
			if("/".equals(uri)){
				//去首页
				File file = new File("webapp/index.html");
				responseFile(HttpContext.STATUS_CODE_OK, file, response);
			}else{
				File file = new File("webapp"+uri);
				if(file.exists()){
					System.out.println("找到了相应资源:"+file.length());
					responseFile(HttpContext.STATUS_CODE_OK,file,response);	
				
				//查看是否请求一个功能
				}else if("/reg".equals(uri)){
					RegServlet servlet = new RegServlet();
					servlet.service(request,response);
					
				}else if("/login".equals(uri)){
					LoginServlet servlet = new LoginServlet();
					servlet.service(request,response);
					
				}else{
					System.out.println("没有资源:404");
					file = new File("webapp/404.html");
					responseFile(HttpContext.STATUS_CODE_NOTFOUND,file,response);	
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 根据给定的文件分析其名字后缀以获取
	 * 对应的ContenType
	 * @param file
	 * @return
	 */
	private String getContentTypeByFile(File file){
		//获取文件名
		String name = file.getName();
		
		//截取后缀
		String ext = name.substring(
				name.lastIndexOf(".")+1);
		
		//获取对应的ContentType
		String contentType 
			= HttpContext.contentTypeMapping.get(ext);
		return contentType;
				
	}
	/**
	 * 响应客户端指定资源
	 * @param status 响应状态码
	 * @param file 要响应的资源
	 * @throws Exception 
	 */
	private void responseFile(int status,File file,HttpResponse response) throws Exception{
		try {
			//1 设置状态行信息
			response.setStatus(status);
			//2 设置响应头信息
			//分析该文件后缀，根据后缀获取对应的ContentType
			response.setContentType(getContentTypeByFile(file));
			response.setContentLength((int)file.length());
			//3 设置响应正文
			response.setEntity(file);
			//4 响应客户端
			response.flush();	
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}


