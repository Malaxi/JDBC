package JDBC_test;

import utils.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 @ utils包下工具类:DBUtil的使用
 */
public class JDBC_test05 {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet result = null;
        try {
            conn= DBUtil.getConnection();
            String sql="select * from emp where job=?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,"manager");
            result=ps.executeQuery();
            System.out.println("姓名"+"      "+"部门"+"     "+"入职日期");
            while(result.next()){
                String ename=result.getString("ename");
                String deptno=result.getString("deptno");
                String hiredate=result.getString("hiredate");
                System.out.println(ename+"     "+deptno+"     "+hiredate);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DBUtil.close(conn,ps,result);
        }
    }
}
