package com.g7go.db;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 
 * ���ݿ����ӵĹ�����
 * @author liwc
 */
public class DBUtil {
	private static BasicDataSource ds;

	static{
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream("config.properties"));
			String driverclass = prop.getProperty("driverclass");
			String url = prop.getProperty("url");
			String username = prop.getProperty("username");
			String password = prop.getProperty("password");
			int maxActive = Integer.parseInt(
				prop.getProperty("maxactive")
			);
			int maxWait = Integer.parseInt(
				prop.getProperty("maxwait")	
			);
			
			System.out.println("��ʼ�����ݿ�����...");
			System.out.println(driverclass);
			System.out.println(url);
			System.out.println(username);
			System.out.println(password);
			
			ds = new BasicDataSource();
			
			//Class.forName(...)
			ds.setDriverClassName(driverclass);
			
			//DriverManager.getConnection(...)
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			
			//�������������
			ds.setMaxActive(maxActive);
			//�������ȴ�ʱ��
			ds.setMaxWait(maxWait);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ȡһ�����ݿ�����
	 * @return
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static Connection getConnection() throws SQLException{
		try {
			/*
			 * �����ӳػ�ȡ����
			 * �����ӳ���û�п�������ʱ���÷�����
			 * ������ǰ�̣߳�����ʱ�������ӳ�����
			 * ��maxWait���������������������ӳ�
			 * ���˿�������ʱ�����������ӷ��ء���
			 * ��ʱ��Ȼû�п�������ʱ���÷������׳�
			 * �쳣��
			 */
			return ds.getConnection();			
			
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public static void closeConnection(Connection conn){
		try {
			if(conn != null){
				conn.setAutoCommit(true);
				/*
				 * ���ӳص����Ӷ���close������
				 * �����ǽ����ӵ������ӳ��е�״̬����Ϊ����
				 * ������Ľ���رա�
				 */
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}




