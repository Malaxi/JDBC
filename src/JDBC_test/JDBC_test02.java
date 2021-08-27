package JDBC_test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 @ 实现一个用户的登录功能
 */
public class JDBC_test02 {
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
        Statement stat=null;
        ResultSet result=null;
        boolean loginSuccess;
        String loginName=userLoginInfo.get("loginName");
        String loginPwd= userLoginInfo.get("loginPwd");
        try {
            //1.注册驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //2.获取连接
            conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/neuq?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false&serverTimezone=GMT","root","@Zhujianlin311");
            //3.获取数据库操作对象
            stat = conn.createStatement();
            //4.执行sql语句,返回一个查询结果集
            String sql="select * from t_user where loginName='"+loginName+"' and loginPwd='"+loginPwd+"'";
            result=stat.executeQuery(sql);
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


