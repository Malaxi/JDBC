package JDBC_test;

import java.sql.*;
/**
 @ 实现java与数据库的连接
 @ JDBC的本质是一组JDBC接口
 @ 准备工作：向模块中导入数据库驱动的jar包，否则第1步会出现找不到类的错误
 */
public class JDBC_test01 {
    public static void main(String[] args){
        Connection conn=null;
        Statement stat=null;
        ResultSet result=null;
        try {
            //1.注册驱动,使JVM知道要连接哪种数据库
            //加载jar包中的Driver类到JVM虚拟机时，会执行其中的静态代码块：注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接，打开JVM与数据库之间的数据通道
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/neuq?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=GMT","root","@Zhujianlin311");
            //3.获取数据库操作对象
            stat = conn.createStatement();
            //4.执行select sql语句，返回一个查询结果集
            String sql="select ename,deptno,hiredate from emp";
            result=stat.executeQuery(sql);
            //5.处理查询结果集，输出查询结果,如果第四步不是select查询语句,那么不需要这步
            while(result.next()){
                String ename=result.getString("ename");
                String deptno=result.getString("deptno");
                String hiredate=result.getString("hiredate");
                System.out.println(ename+"     "+deptno+"     "+hiredate);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //6.释放资源
            if(result!=null) {
                try {
                    result.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if(stat!=null) {
                try {
                    stat.close();
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
