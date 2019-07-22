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
 * ���߳��������ڴ���ÿ���ͻ��˵�����
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
			//������Ӧ���������
			HttpRequest request = new HttpRequest(in);
			OutputStream out = socket.getOutputStream();
			//������Ӧ����Ӧ����
			HttpResponse response = new HttpResponse(out);
			
			/*
			 * �����û�����
			 */
			//��ȡ�û�������Դ·��
			/*
			 *  /index.html
			 *  /reg
			 */
			String uri = request.getUri();
			System.out.println("uri:"+uri);
			if("/".equals(uri)){
				//ȥ��ҳ
				File file = new File("webapp/index.html");
				responseFile(HttpContext.STATUS_CODE_OK, file, response);
			}else{
				File file = new File("webapp"+uri);
				if(file.exists()){
					System.out.println("�ҵ�����Ӧ��Դ:"+file.length());
					responseFile(HttpContext.STATUS_CODE_OK,file,response);	
				
				//�鿴�Ƿ�����һ������
				}else if("/reg".equals(uri)){
					RegServlet servlet = new RegServlet();
					servlet.service(request,response);
					
				}else if("/login".equals(uri)){
					LoginServlet servlet = new LoginServlet();
					servlet.service(request,response);
					
				}else{
					System.out.println("û����Դ:404");
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
	 * ���ݸ������ļ����������ֺ�׺�Ի�ȡ
	 * ��Ӧ��ContenType
	 * @param file
	 * @return
	 */
	private String getContentTypeByFile(File file){
		//��ȡ�ļ���
		String name = file.getName();
		
		//��ȡ��׺
		String ext = name.substring(
				name.lastIndexOf(".")+1);
		
		//��ȡ��Ӧ��ContentType
		String contentType 
			= HttpContext.contentTypeMapping.get(ext);
		return contentType;
				
	}
	/**
	 * ��Ӧ�ͻ���ָ����Դ
	 * @param status ��Ӧ״̬��
	 * @param file Ҫ��Ӧ����Դ
	 * @throws Exception 
	 */
	private void responseFile(int status,File file,HttpResponse response) throws Exception{
		try {
			//1 ����״̬����Ϣ
			response.setStatus(status);
			//2 ������Ӧͷ��Ϣ
			//�������ļ���׺�����ݺ�׺��ȡ��Ӧ��ContentType
			response.setContentType(getContentTypeByFile(file));
			response.setContentLength((int)file.length());
			//3 ������Ӧ����
			response.setEntity(file);
			//4 ��Ӧ�ͻ���
			response.flush();	
		} catch (Exception e) {
			throw e;
		}
	}
	
	
}


