package com.mero.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
import java.util.ArrayList;

import com.mero.test.DbDao;

public class AddFriendTable {
	private static Connection conn=null;
	public static PreparedStatement statement=null;
	public static void createTable(String[] TableFields,String TableName) {
		//static�еı���������static
		try {
			conn=DbDao.getConnection();
			String sql="create table "+TableName+"(id int identity(1,1) "
					+ "primary key not null"; 
			if(TableFields!=null&&TableFields.length>0) {
				sql+=",";
				int length=TableFields.length;
				for(int i=0;i<length;i++) {
					sql+=TableFields[i].trim()+" varchar(50)";
					//.trim()���ַ�����β�Ŀո�ȥ����
					if(i<length-1) {
						sql+=",";
					}
				}
			}
			sql+=");";
			System.out.println(sql);
			statement=conn.prepareStatement(sql);
			statement.executeUpdate();
			//statement.close();
			//conn.close();
			//DbDao.conn=null;
			//conn=null;
			//statement=null;
			//DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
		}catch (SQLException e) {  
            System.out.println("����ʧ��" + e.getMessage());  
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	public static void insert(String[] TableFields,String TableName,String[] data) {	
		try {
			
			conn=DbDao.getConnection();
			String sql = "insert into "+TableName+"(";
			int length = TableFields.length;  
            for(int i=0;i<length;i++){  
                sql+=TableFields[i];  
                //��ֹ���һ��,  
                if(i<length-1){  
                    sql+=",";  
                }  
            }
            sql+=") values(";  
            for(int i=0;i<length;i++){  
                sql+="?";  
                //��ֹ���һ��,  
                if(i<length-1){  
                    sql+=",";  
                }  
            }  
            sql+=");";  
            System.out.println("������ݵ�sql:"+sql);  
            //Ԥ����SQL ��ֹע��  
            excutePs(sql,length,data);  
            //ִ��  
            statement.executeUpdate();  
            //�ر���  
            //statement.close();  
            //conn.close();    //�ر����ݿ����� 
            //DbDao.conn=null;
            //conn=null;
			//statement=null;
            //DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
		}catch (SQLException e) {  
            System.out.println("�������ʧ��" + e.getMessage());  
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	/** 
     * ��ѯ��  ����ѯ�����˳��Ҫ�����ݿ��ֶε�˳��һ�¡� 
     * @param tabName ���� 
     * @param fields �����ֶ�  
     * @param data  �����ֶ����� 
     * @param tab_fields ���ݿ���ֶ� 
     */  
	public static  String[] query(String TableName,String[] Fields,String[] data,String[] Table_Fields){  
        String[] result = null;
        boolean state=false;
        try {  
        	conn = DbDao.getConnection();// ����Ҫ��ȡ���ӣ������ӵ����ݿ�
            String sql = "select * from "+TableName+" where ";  
             int length = Fields.length;  
             for(int i=0;i<length;i++){  
                    sql+=Fields[i]+" = ? ";//������Ԥ������������Ϊ��
                    //��ֹ���һ��,  
                    if(i<length-1){  
                        sql+=" and ";  
                    }  
             }  
             sql+=";";  
             System.out.println("��ѯsql:"+sql);  
            //Ԥ����SQL ��ֹע��  
            excutePs(sql,length,data);  
            //��ѯ�����  
            ResultSet set=statement.executeQuery();  
            //��Ž����  
            result = new String[Table_Fields.length];  
            while(set.next()){//���û�鵽�Ļ���set.next()=false!!
            	state=true;
                    for (int i = 0; i < Table_Fields.length; i++) {  
                        result[i] = set.getString(Table_Fields[i]);
                    }  
                }
            //statement.close();
           // DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
            if(state) {
            	return result;
            }else {
            	return null;
            }
        } catch (SQLException e) {  
             System.out.println("��ѯʧ��" + e.getMessage());  
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;  
    }
	
	public static  ArrayList<String[]> queryForChatInit(String TableName,String[] Fields,String[] data,String[] Table_Fields){  
		ArrayList<String[]> arrayList=null;
		String[] result = null;
        boolean state=false;
        try {  
        	conn = DbDao.getConnection();// ����Ҫ��ȡ���ӣ������ӵ����ݿ�
            String sql = "select * from "+TableName+" where ";  
             int length = Fields.length;  
             for(int i=0;i<length;i++){  
                    sql+=Fields[i]+" = ? ";//������Ԥ������������Ϊ��
                    //��ֹ���һ��,  
                    if(i<length-1){  
                        sql+=" and ";  
                    }  
             }  
             sql+=";";  
             System.out.println("��ѯsql:"+sql);  
            //Ԥ����SQL ��ֹע��  
            excutePs(sql,length,data);  
            //��ѯ�����  
            ResultSet set=statement.executeQuery();  
            //��Ž����  
            
            arrayList=new ArrayList<String[]>();
            while(set.next()){//���û�鵽�Ļ���set.next()=false!!
            	state=true;
            	result = new String[Table_Fields.length];
                    for (int i = 0; i < Table_Fields.length; i++) {  
                        result[i] = set.getString(Table_Fields[i]);
                    }
                    arrayList.add(result);
                }
            //statement.close();
           // DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
            if(state) {
            	return arrayList;
            }else {
            	return null;
            }
        } catch (SQLException e) {  
             System.out.println("��ѯʧ��" + e.getMessage());  
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;  
    }
	
	public static ArrayList<String[]> queryAll(String TableName,String[] Table_Fields){  
		ArrayList<String[]> arrayList=null;
		String[] result = null;
        boolean state=false;
        try {  
        	conn = DbDao.getConnection();// ����Ҫ��ȡ���ӣ������ӵ����ݿ�
            String sql = "select * from "+TableName; 
            System.out.println("��ѯsql:"+sql); 
            statement=conn.prepareStatement(sql);
            //��ѯ�����  
            ResultSet set=statement.executeQuery();  
            //��Ž����
            arrayList=new ArrayList<String[]>();  
            while(set.next()){//���û�鵽�Ļ���set.next()=false!!
            	state=true;
            	result = new String[Table_Fields.length];
                    for (int i = 0; i < Table_Fields.length; i++) {  
                        result[i] = set.getString(Table_Fields[i]);//������Ҫ����
                        //Table_Fields����Ҫ��ѯ�ļ�ֵ����
                    }
                    arrayList.add(result);
                }
            //statement.close();
            //DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
            if(state) {
            	return arrayList;
            }else {
            	return null;
            }
        } catch (SQLException e) {  
             System.out.println("��ѯʧ��" + e.getMessage());  
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;  
    }
	
	public static int userIdQuery(String account) {
		try {
			
			conn=DbDao.getConnection();
			String sql = "select id from "
		    		+ "users where account='"+account+"'";
			statement = conn.prepareStatement(sql);
			ResultSet set=statement.executeQuery();
			int id = 0;
			while(set.next()) {
				id=set.getInt(1);
			}
            //statement.close();  
            //conn.close();    //�ر����ݿ�����
			//DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
			//statement=null;
            return id;
		}catch (SQLException e) {  
            System.out.println("�Ҳ���" + e.getMessage());  
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return 0;	
	}
	
	private static void excutePs(String sql,int length,String[] data) throws SQLException{  
        //Ԥ����SQL ��ֹע��  
        statement = conn.prepareStatement(sql);  
        //ע�����  
        for(int i=0;i<length;i++){  
             statement.setString(i+1,data[i]);  
        }  
    }
	
	public static void userIPUpdate(String account,String ip) {
		try {
			
			conn=DbDao.getConnection();
			String sql = "update users set ip='"+ip+
					"' where account='"+account+"'";
			System.out.println("IP���µ�sql:"+sql);  
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
		
            //statement.close();  
			//DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
            
		}catch (SQLException e) {  
            System.out.println("IP����ʧ��" + e.getMessage());  
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }	
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
}
