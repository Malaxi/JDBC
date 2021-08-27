package JDBC_test;/*
  @ 通过配置文件(db.properties)加载驱动
  @ 使用工具类：DBUtil_Properties
*/
import utils.DBUtil;
import java.sql.*;
public class JDBC_test06 {
    public static void main(String[] args) throws SQLException {
        //1
        Connection conn = DBUtil.getConnection();
        System.out.println(conn);
        //2
        String sql="select * from emp where empno=?";
        PreparedStatement pre=conn.prepareStatement(sql);
        pre.setString(1,"7369");
        //3
        ResultSet res=pre.executeQuery();
        //4
        while(res.next()){
            String ename=res.getString("ename");
            String deptno=res.getString("deptno");
            String hiredate=res.getString("hiredate");
            System.out.println(ename+"     "+deptno+"     "+hiredate);
        }
        //5
        DBUtil.close(res,pre,conn);
    }
}

