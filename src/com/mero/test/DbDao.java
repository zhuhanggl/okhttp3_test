package com.mero.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DbDao {
    public static Connection conn=null;
    public static Connection getConnection() throws ClassNotFoundException{
        //��������
        Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        System.out.println("���������ɹ�");
        try {
                                                                                                                                                                                                                                                                                                                                                                   Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");//�������ݿ�����
            if(null==conn){
                System.out.println("��ʱδ����");//������������ĵ�һ�λ���������
                //�õ����ݿ�����������

    //����Ĳ�����ʽ����:jdbc���ӷ�ʽ:���ݿ�����:���˿ڵ�������ַ;"���ݿ�����","���ݿ��½��","���ݿ�����";  
                conn=DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=users","zhuhanggl","19930307");
                //���û�ϵ�¼��ʽ.
                
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
