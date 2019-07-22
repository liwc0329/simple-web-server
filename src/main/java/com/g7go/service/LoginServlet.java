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
        System.out.println("开始登录...");
        //获取用户名，密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + "," + password);
        /*
         * 连接数据库，根据用户名密码查询该用户
         * 是否存在，存在则显示登录成功，否则
         * 显示登录失败。
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
                System.out.println("登录成功!");
                /*
                 * 响应登录成功的页面给客户端
                 */
                forward("/login_ok.html", response);
            } else {
                System.out.println("登录失败!");
                forward("/login_error.html", response);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
    }
}






