package JDBC_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 @ 实现手动提交事务:要求处于该事务中的sql语句同时执行成功或失败
 @ conn.setAutoCommit(false):关闭事务自动提交功能
 @ conn.commit():手动提交事务
 @ conn.rollback():事务回滚
 */
public class JDBC_test04 {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement ps=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/neuq?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=GMT","root","@Zhujianlin311");
            conn.setAutoCommit(false);//设置为手动提交事务
            String sql="update t_user set loginPwd=? where loginName=?";
            ps = conn.prepareStatement(sql);
            //----------------------------------------
            ps.setString(1,"123456");
            ps.setString(2, "zhangsan");
            ps.executeUpdate();//(1)
            //----------------------------------------
            ps.setString(1, "123456");
            ps.setString(2, "jack");
            ps.executeUpdate();//(2)
            //----------------------------------------
            conn.commit();//提交事务:(1)和(2)是同时产生对数据库的更新

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //6.释放资源
            if(ps!=null) {
                try {
                    ps.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(conn!=null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }
}
