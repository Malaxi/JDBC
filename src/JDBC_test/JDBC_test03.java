package JDBC_test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 @ 解决JDBC_test02中的SQL注入问题
 @ 导致SQL注入问题的根本原因：用户提供的信息中含有数据库关键字并参与了sql的编译
 @ 解决SQL注入问题的根本方法：即使用户提供的信息中含有sql语句的关键字，但不参与编译,想要
                         用户提供的信息不参与编译，那么必须使用PreparedStatement
                         它继承了Statement,属于预编译的数据库操作对象。它的原理是:
                         预先对SQL语句的框架进行编译，然后再给SQL语句的对应位置传值
 */
public class JDBC_test03 {
    public static void main(String[] args) {
        //初始化界面
        Map<String,String> userLoginInfo=initUI();
        //验证用户名和密码
        boolean loginSuccess=login(userLoginInfo);
        //最后输出结果
        System.out.println(loginSuccess?"登录成功":"登陆失败");
    }
    /**
     *用户登录
     * @param userLoginInfo 用户登录信息
     * @return false表示失败，true表示成功
     */
    private static boolean login(Map<String, String> userLoginInfo) {
        //JDBC代码
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet result=null;
        boolean loginSuccess;
        String loginName=userLoginInfo.get("loginName");
        String loginPwd= userLoginInfo.get("loginPwd");
        try {
            //1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/neuq?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=GMT","root","@Zhujianlin311");
            //3.获取预编译de数据库操作对象
            //此时sql语句要放在创建预编译de数据库操作对象之前，一个？表示一个占位符，将来接收一个值
            String sql="select * from t_user where loginName=? and loginPwd=?";
            //程序执行到此处，会发送sql语句框架给DBMS，然后DBMS进行sql语句的预先编译
            ps = conn.prepareStatement(sql);
            //给占位符复制(第一个问号下标是1，第二个问号下标是2，JDBC中所有下标从1开始)
            ps.setString(1,loginName);
            ps.setString(2,loginPwd);
            //4.执行sql语句,返回一个查询结果集
            result=ps.executeQuery();
            //5.处理查询结果集
            if(result.next()){
                //登录成功
                loginSuccess=true;
                return loginSuccess;
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
        return false;
    }

    /**
     *初始化用户登录界面
     * @return 用户输入的登录名和密码等登录信息
     */
    private static Map<String,String> initUI() {
        Scanner s=new Scanner(System.in);
        System.out.print("用户名：");
        String loginName=s.nextLine();
        System.out.print("密码：");
        String loginPwd=s.nextLine();
        Map<String,String> userLoginInfo=new HashMap<String,String>();
        userLoginInfo.put("loginName",loginName);
        userLoginInfo.put("loginPwd",loginPwd);
        return userLoginInfo;
    }
}
