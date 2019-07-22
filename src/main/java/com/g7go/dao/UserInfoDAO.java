package com.g7go.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.g7go.db.DBUtil;
import com.g7go.entities.UserInfo;

public class UserInfoDAO {

    public boolean save(UserInfo userInfo) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            String sql = "INSERT INTO userinfo "
                    + "(id,username,password,nickname,account) "
                    + "VALUES "
                    + "(seq_userinfo_id.NEXTVAL,?,?,?,?)";
            PreparedStatement ps
                    = conn.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, userInfo.getUsername());
            ps.setString(2, userInfo.getPassword());
            ps.setString(3, userInfo.getNickname());
            ps.setInt(4, userInfo.getAccount());
            int d = ps.executeUpdate();
            if (d > 0) {
                //ݳɹ
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);
                userInfo.setId(id);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.closeConnection(conn);
        }
        return false;
    }
}









