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
		//static中的变量必须是static
		try {
			conn=DbDao.getConnection();
			String sql="create table "+TableName+"(id int identity(1,1) "
					+ "primary key not null"; 
			if(TableFields!=null&&TableFields.length>0) {
				sql+=",";
				int length=TableFields.length;
				for(int i=0;i<length;i++) {
					sql+=TableFields[i].trim()+" varchar(50)";
					//.trim()把字符串首尾的空格都去掉了
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
            System.out.println("建表失败" + e.getMessage());  
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
                //防止最后一个,  
                if(i<length-1){  
                    sql+=",";  
                }  
            }
            sql+=") values(";  
            for(int i=0;i<length;i++){  
                sql+="?";  
                //防止最后一个,  
                if(i<length-1){  
                    sql+=",";  
                }  
            }  
            sql+=");";  
            System.out.println("添加数据的sql:"+sql);  
            //预处理SQL 防止注入  
            excutePs(sql,length,data);  
            //执行  
            statement.executeUpdate();  
            //关闭流  
            //statement.close();  
            //conn.close();    //关闭数据库连接 
            //DbDao.conn=null;
            //conn=null;
			//statement=null;
            //DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
		}catch (SQLException e) {  
            System.out.println("添加数据失败" + e.getMessage());  
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	/** 
     * 查询表  【查询结果的顺序要和数据库字段的顺序一致】 
     * @param tabName 表名 
     * @param fields 参数字段  
     * @param data  参数字段数据 
     * @param tab_fields 数据库的字段 
     */  
	public static  String[] query(String TableName,String[] Fields,String[] data,String[] Table_Fields){  
        String[] result = null;
        boolean state=false;
        try {  
        	conn = DbDao.getConnection();// 首先要获取连接，即连接到数据库
            String sql = "select * from "+TableName+" where ";  
             int length = Fields.length;  
             for(int i=0;i<length;i++){  
                    sql+=Fields[i]+" = ? ";//由于有预处理，所有这里为？
                    //防止最后一个,  
                    if(i<length-1){  
                        sql+=" and ";  
                    }  
             }  
             sql+=";";  
             System.out.println("查询sql:"+sql);  
            //预处理SQL 防止注入  
            excutePs(sql,length,data);  
            //查询结果集  
            ResultSet set=statement.executeQuery();  
            //存放结果集  
            result = new String[Table_Fields.length];  
            while(set.next()){//如果没查到的话，set.next()=false!!
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
             System.out.println("查询失败" + e.getMessage());  
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
        	conn = DbDao.getConnection();// 首先要获取连接，即连接到数据库
            String sql = "select * from "+TableName+" where ";  
             int length = Fields.length;  
             for(int i=0;i<length;i++){  
                    sql+=Fields[i]+" = ? ";//由于有预处理，所有这里为？
                    //防止最后一个,  
                    if(i<length-1){  
                        sql+=" and ";  
                    }  
             }  
             sql+=";";  
             System.out.println("查询sql:"+sql);  
            //预处理SQL 防止注入  
            excutePs(sql,length,data);  
            //查询结果集  
            ResultSet set=statement.executeQuery();  
            //存放结果集  
            
            arrayList=new ArrayList<String[]>();
            while(set.next()){//如果没查到的话，set.next()=false!!
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
             System.out.println("查询失败" + e.getMessage());  
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
        	conn = DbDao.getConnection();// 首先要获取连接，即连接到数据库
            String sql = "select * from "+TableName; 
            System.out.println("查询sql:"+sql); 
            statement=conn.prepareStatement(sql);
            //查询结果集  
            ResultSet set=statement.executeQuery();  
            //存放结果集
            arrayList=new ArrayList<String[]>();  
            while(set.next()){//如果没查到的话，set.next()=false!!
            	state=true;
            	result = new String[Table_Fields.length];
                    for (int i = 0; i < Table_Fields.length; i++) {  
                        result[i] = set.getString(Table_Fields[i]);//这句很重要！！
                        //Table_Fields代表要查询的键值！！
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
             System.out.println("查询失败" + e.getMessage());  
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
            //conn.close();    //关闭数据库连接
			//DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
			//statement=null;
            return id;
		}catch (SQLException e) {  
            System.out.println("找不到" + e.getMessage());  
        }catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return 0;	
	}
	
	private static void excutePs(String sql,int length,String[] data) throws SQLException{  
        //预处理SQL 防止注入  
        statement = conn.prepareStatement(sql);  
        //注入参数  
        for(int i=0;i<length;i++){  
             statement.setString(i+1,data[i]);  
        }  
    }
	
	public static void userIPUpdate(String account,String ip) {
		try {
			
			conn=DbDao.getConnection();
			String sql = "update users set ip='"+ip+
					"' where account='"+account+"'";
			System.out.println("IP更新的sql:"+sql);  
			statement = conn.prepareStatement(sql);
			statement.executeUpdate();
		
            //statement.close();  
			//DbDao.closeConnection(DbDao.conn);
            //DbDao.conn=null;
            //conn=null;
            
		}catch (SQLException e) {  
            System.out.println("IP更新失败" + e.getMessage());  
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
