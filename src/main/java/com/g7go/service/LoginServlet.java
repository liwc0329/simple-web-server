package com.g7go.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.g7go.core.HttpServlet;
import com.g7go.db.DBUtil;
import com.g7go.http.HttpRequest;
import com.g7go.http.HttpResponse;

public class LoginServlet extends HttpServlet {
    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("��ʼ��¼...");
        //��ȡ�û���������
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + "," + password);
        /*
         * �������ݿ⣬�����û��������ѯ���û�
         * �Ƿ���ڣ���������ʾ��¼�ɹ�������
         * ��ʾ��¼ʧ�ܡ�
         */
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT id,username,password,nickname,account "
                    + "FROM userinfo_fancq "
                    + "WHERE username=? AND password=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("��¼�ɹ�!");
                /*
                 * ��Ӧ��¼�ɹ���ҳ����ͻ���
                 */
                forward("/login_ok.html", response);
            } else {
                System.out.println("��¼ʧ��!");
                forward("/login_error.html", response);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
    }
}






