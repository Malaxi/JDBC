package utils;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/*
   @ 这是基于配置文件和Threadlocal的数据库工具类
*/
public class DbUtil1 {
    //用于存储配置文件内容的properties集合
    private static final Properties properties=new Properties();
    //为线程创建一个共享的对象(Connection对象)
    private static ThreadLocal<Connection> threadlocal=new ThreadLocal<>();
    //当类被加载时自动执行静态代码块中的内容
    static{
        //创建一个指向配置文件的输入流
        InputStream input= DbUtil1.class.getResourceAsStream("/db1.properties");
        try {
            //将指向配置文件的输入流中的内容加载到集合之中
            properties.load(input);
            //通过关键字获取驱动的完整类名并加载
            Class.forName(properties.getProperty("driver"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取链接
    public static Connection getConnection(){
        //将当前线程中所绑定Connection对象，赋值给connection
        Connection connection= threadlocal.get();
        try {
            //判断当前线程中是否已经有一个共享的Connection对象
            if(connection==null){
                connection= DriverManager.getConnection(properties.getProperty("url"),properties.getProperty("username"),properties.getProperty("password"));
                //把连接存在当前线程共享中
                threadlocal.set(connection);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    //开启事务
    public static void begin(){
        Connection connection=null;
        try {
            connection=getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            close(null,null,connection);
        }
    }

    //提交事务
    public static void commit(){
        Connection connection=getConnection();
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //回滚事务
    public static void rollback(){
        Connection connection=null;
        try {
            connection=getConnection();
            connection.rollback();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            close(null,null,connection);
        }
    }

    //关闭连接
    public static void close(ResultSet resultset,Statement statement,Connection connection){
        if(resultset!=null){
            try {
                resultset.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
                //关闭连接后，移除threadlocal中共享的Connection对象
                threadlocal.remove();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
