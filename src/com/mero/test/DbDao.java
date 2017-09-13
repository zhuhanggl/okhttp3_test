package com.mero.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DbDao {
    public static Connection conn=null;
    public static Connection getConnection() throws ClassNotFoundException{
        //加载驱动
        Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        System.out.println("加载驱动成功");
        try {
                                                                                                                                                                                                                                                                                                                                                                   Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");//加载数据库驱动
            if(null==conn){
                System.out.println("暂时未连接");//服务器启动后的第一次会输出该语句
                //得到数据库驱动连接流

    //下面的参数格式如下:jdbc连接方式:数据库类型:带端口的主机地址;"数据库名称","数据库登陆名","数据库密码";  
                conn=DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=users","zhuhanggl","19930307");
                //采用混合登录方式.
                
                return conn;
            }
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
        	System.out.println("(ClassNotFoundException e) ");
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
        	System.out.println("(SQLException e) ");
            e.printStackTrace();
        }
        return conn;

    }
    public static void closeStatement(Statement statement){
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public static void closeConnection(Connection conn){
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
