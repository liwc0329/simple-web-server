package com.g7go.service;

import com.g7go.core.HttpServlet;
import com.g7go.dao.UserInfoDAO;
import com.g7go.entities.UserInfo;
import com.g7go.http.HttpRequest;
import com.g7go.http.HttpResponse;

/**
 * ��������û�ע�Ṧ��
 *
 * @author Mr_Lee
 */
public class RegServlet extends HttpServlet {


    public void service(HttpRequest request, HttpResponse response) {
        System.out.println("��ʼ����ע��!");
        //��ȡ�û���
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        System.out.println("ע��ɹ�!");
        //����һ��UserInfoʵ�����ڱ�ʾ��ע����Ϣ
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        userInfo.setNickname(nickname);
        userInfo.setAccount(5000);
        //�����û���Ϣ
        UserInfoDAO dao = new UserInfoDAO();
        boolean success = dao.save(userInfo);
        if (success) {
            try {
                /*
                 * ��Ӧע��ɹ���ҳ����ͻ���
                 */
                forward("/reg_ok.html", response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //��תע��ʧ��ҳ��
            System.out.println("ע��ʧ��!");
        }
    }


}




