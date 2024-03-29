package com.g7go.service;

import com.g7go.core.HttpServlet;
import com.g7go.dao.UserInfoDAO;
import com.g7go.entities.UserInfo;
import com.g7go.http.HttpRequest;
import com.g7go.http.HttpResponse;

/**
 * 用来完成用户注册功能
 *
 * @author Mr_Lee
 */
public class RegServlet extends HttpServlet {


    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("开始处理注册!");
        //获取用户名
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        System.out.println("注册成功!");
        //创建一个UserInfo实例用于表示该注册信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setNickname(nickname);
        userInfo.setAccount(5000);
        //保存用户信息
        UserInfoDAO dao = new UserInfoDAO();
        boolean success = dao.save(userInfo);
        if (success) {
            try {
                /*
                 * 响应注册成功的页面给客户端
                 */
                forward("/reg_ok.html", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //跳转注册失败页面
            System.out.println("注册失败!");
        }
    }


}




