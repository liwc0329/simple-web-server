package com.g7go.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.g7go.common.HttpContext;

/**
 * Http�������
 * ��װһ��Http���������Ϣ
 * @author Mr_Lee
 *
 */
public class HttpRequest {
	/*
	 * �����������Ϣ
	 */
	//���󷽷�
	private String method;
	//������ԴURI  (URIͳһ��Դ��λ)
	private String uri;
	//����Э��汾
	private String protocol;
	
	//��Ϣ��ͷ��Ϣ
	private Map<String,String> header;
	
	
	//�洢�ͻ��˴��ݹ����Ĳ���
	private Map<String,String> parameters;
	
	
	public HttpRequest(InputStream in) throws Exception{
		try {
			System.out.println("http���췽��!");
			//1����������
			parseRequestLine(in);
			//2������Ϣͷ
			parseRequestHeader(in);
			//3������Ϣ����(��)
			
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * ������������Ϣ
	 * @param in
	 * @throws IOException 
	 */
	private void parseRequestLine(InputStream in) throws IOException{
		/*
		 * ʵ�ֲ���:
		 * 1:�ȶ�ȡһ���ַ���(CRLF��β)
		 * 2:���ݿո���(\s)
		 * 3:���������������������õ�HttpRequest
		 *   ��Ӧ������
		 *   
		 * GET /index.html HTTP/1.1  
		 * GET /reg?username=fancq&password=123456&nickname=fanfan HTTP/1.1  
		 */
		try{
			//1
			String line = readLine(in);
			System.out.println("requestLine:"+line);
			/*
			 * �������и�ʽ��һЩ��Ҫ��֤
			 */
			if(line.length()==0){
				throw new RuntimeException("�յ�������!");
			}
			//2
			String[] data = line.split("\\s");
			
			//3
			this.method = data[0];
//			this.uri = data[1];
			parseUri(data[1]);
			this.protocol = data[2];
			System.out.println("�������������!");
		}catch(IOException e){
			throw e;
		}
	}
	/**
	 * ����URI
	 * @param uri
	 */
	private void parseUri(String uri){
		/*
		 * /index.html
		 * /reg?username=fancq&password=123456&nickname=fanfan
		 * ��GET�����У�URI���ܻ����������������
		 * HTTPЭ���й涨��GET�����е�URI���Դ���
		 * ���������������������Դ������"?"�ָ
		 * ֮����Ϊ����Ҫ���ݵĲ�����ÿ��������:
		 * name=value�ĸ�ʽ���棬ÿ������֮��ʹ��
		 * "&"�ָ
		 * ����Ĵ���Ҫ��:
		 * ��"?"֮ǰ�����ݱ��浽����uri�ϡ�
		 * ֮���ÿ����������������parameters��
		 * ����keyΪ��������valueΪ������ֵ��
		 * 
		 * 1:ʵ����HashMap���ڳ�ʼ������parameters
		 * 
		 */
		//1
		this.parameters = new HashMap<String,String>();
		
		//�ȷ���uri���Ƿ���"?"
		int index = -1;
		if((index = uri.indexOf("?"))>=0){
			//�Ȱ���"?"���
			this.uri = uri.substring(0,index);
			
			/*
			 * ��ȡ�����в���
			 * paras:username=fancq&password=123456&nickname=fanfan
			 */
			String paras = uri.substring(index+1);
			/*
			 * ���ÿһ������
			 * [username=fancq,password=123456,nickname=fanfan]
			 */
			String[] paraArray = paras.split("&");
			//����ÿһ������
			for(String para : paraArray){
				//����"="���
				String[] paraDate = para.split("=");
				this.parameters.put(paraDate[0], paraDate[1]);
			}
		}else{
			this.uri = uri;
		}
		
		
	}
	
	/**
	 * ������Ϣͷ
	 * @param in
	 * @throws Exception 
	 */
	private void parseRequestHeader(InputStream in) throws Exception{
		/*
		 * ��Ϣͷ�����������
		 * ÿ�и�ʽ:
		 * name:valueCRLF
		 * ��������Ϣͷȫ�����͹������������
		 * ��������һ��CRLF����
		 * 
		 * ʵ��˼·:
		 * 1:��ѭ�����沽��
		 * 2:��ȡһ���ַ���
		 * 3:�жϸ��ַ����Ƿ�Ϊ���ַ���
		 *   ���ǿ��ַ���˵��������󵥶���CRLF
		 *   ��ô�Ϳ���ֹͣѭ���������ٽ�����Ϣͷ
		 *   ��Ϣ
		 * 4:�����ǿմ������ա�:����ȡ��������
		 *   ��Ӧ��ֵ������header���Map��
		 */
		try {
			header = new HashMap<String,String>();
			String line = null;
			while(true){
				line = readLine(in);
				if(line.length()==0){
					break;
				}
				int index = line.indexOf(":");
				String name = line.substring(0,index);
				String value = line.substring(index+1).trim();
				header.put(name, value);
			}
		} catch (Exception e) {
			throw e;
		}
		System.out.println("header:"+header);
		System.out.println("������Ϣͷ���!");
	}
	
	
	/**
	 * ������������ȡһ���ַ���
	 * ����HTTPЭ���ȡ�����е�һ������,
	 * ��CRLF��β��һ���ַ���
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private String readLine(InputStream in)
							throws IOException{
		//�����е�һ���ַ���(����������)
		StringBuilder builder 
			= new StringBuilder();
		/*
		 * ������ȡ�����ַ���ֱ��������ȡ����CRLFΪֹ
		 */
		//c1�Ǳ��ζ������ַ���c2���ϴζ������ַ�
		int c1 = -1, c2 = -1;
		while((c1 = in.read())!=-1){
			if(c1==HttpContext.LF&&c2==HttpContext.CR){
				break;
			}
			builder.append((char)c1);
			c2 = c1;
		}
		return builder.toString().trim();
	}
	
	public String getMethod() {
		return method;
	}
	public String getUri() {
		return uri;
	}
	public String getProtocol() {
		return protocol;
	}
	public Map<String, String> getHeader() {
		return header;
	}
	/**
	 * ���ݲ�������ȡ����ֵ
	 * @param name
	 * @return
	 */
	public String getParameter(String name){
		return parameters.get(name);
	}
	
	
	public HttpRequest(){
		
	}
	public static void main(String[] args) {
		HttpRequest r = new HttpRequest();
		r.parseUri("/reg?username=fancq&password=123456&nickname=fanfan");
		System.out.println("uri:"+r.uri);
		System.out.println("param:"+r.parameters);
	}
	
}








