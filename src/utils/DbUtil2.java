package utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*
   @ 这是基于配置文件和连接池和Threadlocal的数据库工具类
*/
public class DbUtil2 {
    //声明指向连接池对象的引用
    private static DruidDataSource ds;
    private static ThreadLocal<Connection> threadlocal=new ThreadLocal<>();
    //当类被加载时自动执行静态代码块中的内容
    static {
        Properties properties=new Properties();
        InputStream is=DbUtil2.class.getResourceAsStream("/db2.properties");
        try {
            properties.load(is);
            //创建连接池
            ds=(DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取链接
    public static Connection getConnection(){
        Connection connection=threadlocal.get();
        try {
            if(connection==null){
                connection= ds.getConnection();
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
        }
    }

    //提交事务
    public static void commit(){
        Connection connection=getConnection();
        try {
            connection.commit();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            close(null,null,connection);
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
    public static void close(ResultSet resultset, Statement statement, Connection connection){
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

    //获取连接池对象
    public static DataSource getDatasource() {
        //向上转型
        return (DataSource)ds;
    }

}
