

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Map;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.mero.test.DbDao;
import com.mero.test.AddFriendTable;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;

import net.sf.json.*;
import net.sf.json.regexp.*;
import net.sf.json.util.*;
import net.sf.json.xml.*;
/**
 * Servlet implementation class LoginServlet
 */
//注意@MultipartConfig要放在最后面！！！！没有@MultipartConfig，则getPart为null！！！1
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public Connection conn=null;
	private static final int login=0;
	private static final int sign_up=1;
	private static final int ip_update=2;
	private static final int friends_loading=3;
	private static final int friend_IP=4;
	private static final int add_friend=5;
	private static final int chat_init=6;
	private static final int friend_data=7;
	private String req;
	private String friendsId;
	private String account;
	private String friendAccount;
	private String password;
	private String name;
	private String Avatar;
	private String ip;
	private String userId;
	
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("GET请求成功");
        response.getWriter().append("GET");//用网址get的话，则网页显示的是GETzhuhang
        
        //doPost(request,response);
        //request是客户端发给服务器的
        //response是服务器返回给客户端的
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Post请求成功");
		response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        req=request.getParameter("Req");
        
        if(Integer.parseInt(req)==login) {
        	login(request,response);
        }
        if(Integer.parseInt(req)==sign_up) {
        	sign_up(request,response);
        }
        if(Integer.parseInt(req)==ip_update) {
        	ip_update(request,response);
        }
        if(Integer.parseInt(req)==friends_loading) {
        	friendsLoading(request,response);
        }
        if(Integer.parseInt(req)==friend_IP) {
        	friendIP(request,response);
        }
        if(Integer.parseInt(req)==add_friend) {
        	addFriend(request,response);
        }
        if(Integer.parseInt(req)==chat_init) {
        	chatInit(request,response);
        }
        if(Integer.parseInt(req)==friend_data) {
        	friendData(request,response);
        }
        //AddFriendTable.closeStatement(AddFriendTable.statement);
        DbDao.closeConnection(DbDao.conn);//在这里使用静态方法close connection
        DbDao.conn=null;
	}
	
	
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		account=request.getParameter("Account");
        password=request.getParameter("Password");
        System.out.println(account);
        System.out.println(password);
        //response.getWriter().append("登录请求成功");//有了这句话，返回的就不是纯
        //Json了！！！
     
        String[] Fields= {"account"};
        String[] TableFields= {"id","account","password","name","Avatar","ip"};
    	String[] data= {account};
    	String[] result=AddFriendTable.query("users", Fields, data, TableFields);
    	//System.out.println(result);
    	if(result!=null) {
    		if(result[2].equals(password)) {
    			JSONObject jsonObject = new JSONObject();
            	//Map<String,String> map = new LinkedHashMap<String,String>();
            	jsonObject.put("FriendsId", result[0]);
            	jsonObject.put("account", result[1]);
            	jsonObject.put("password", result[2]);
            	jsonObject.put("name", result[3]);
            	jsonObject.put("Avatar", result[4]);
            	jsonObject.put("ip", result[5]);
            	//LinkedHashMap是有序的
            	//HashMap是无序的
            	//JSONArray array = JSONArray.fromObject(map);//用不了的话看收藏的网址
            	//使用json发生java.lang.NoClassDefFoundError: net/sf/json/JSONObject异常的解决办法
            	//和使用json及异常处理（ava.lang.NoClassDefFoundError: net/sf/json/JSONArray）
            	System.out.println(jsonObject.toString());
            	response.getWriter().append(jsonObject.toString());
    		}else {
    			response.getWriter().append("q");
    		} 			
    	}else {
    		response.getWriter().append("x");
    	}
    	
	}
	
	private void sign_up(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
        account=request.getParameter("Account");
        password=request.getParameter("Password");
        name=request.getParameter("Name");
        Avatar=request.getParameter("Avatar");
        ip=request.getParameter("IP");
        System.out.println(req);
        System.out.println(account);
        System.out.println(password);
        System.out.println(name);
        System.out.println(Avatar);
        System.out.println(ip);
        String[] FieldsForQuery= {"account"};
        String[] TableFieldsForQuery= {"id"};
    	String[] dataForQuery= {account};
    	String[] result=AddFriendTable.query
    			("users", FieldsForQuery, dataForQuery, TableFieldsForQuery);
    	if(result==null) {//改完程序后不需要重新按启动服务器按钮，编译都是动态的！！！！
    		response.getWriter().append("a");
        	String[] TableFields= {"account","password","name","Avatar","ip"};
        	String[] data= {account,password,name,Avatar,ip};
        	AddFriendTable.insert(TableFields,"users", data);
        	int id=AddFriendTable.userIdQuery(account);
        	if(id!=0) {
        		String[] FriendsTableFields= {"friendId","account","name","Avatar","ip"};
        		String FriendsTableName="friend_table_"+Integer.toString(id);
        	    AddFriendTable.createTable(FriendsTableFields, FriendsTableName);
        	    String[] MessageTableFields= {"fromAccount","toAccount","message"};
        	    //数据库中不能用from和to作为键名！！！
        	    String MessageTableName="message_table_"+Integer.toString(id);
        	    AddFriendTable.createTable(MessageTableFields, MessageTableName);   
        	}
        	//DbDao.closeConnection(DbDao.conn);//在这里使用静态方法close connection
        	//之前出现的object错误与statement有关
    	}else {
    		response.getWriter().append("x");
    	}
    	
	}
	
	private void ip_update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		account=request.getParameter("Account");
		ip=request.getParameter("IP");
        System.out.println(req);
        System.out.println(account);
        System.out.println(ip);
    	response.getWriter().append("IP更新请求成功");
    	AddFriendTable.userIPUpdate(account,ip);
    	
    	//之前出现的object错误与statement有关
	}
	
	private void friendsLoading(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        friendsId=request.getParameter("FriendsId");
        System.out.println(friendsId);
        //response.getWriter().append("登录请求成功");//有了这句话，返回的就不是纯
        //Json了！！！
        String[] TableFields= {"friendId","account","name","Avatar"};
        ArrayList<String[]> arrayList=AddFriendTable.queryAll("friend_table_"+friendsId, TableFields);
    	//System.out.println(arrayList);
    	if(arrayList!=null) {
    		JSONArray jsonArray= new JSONArray();
    		for(int i=0;i<arrayList.size();i++) {
    			JSONObject jsonObject = new JSONObject();
    			jsonObject.put("id", arrayList.get(i)[0]);
    			jsonObject.put("account", arrayList.get(i)[1]);//返回的都是最后一个值的时候
    			//string的特性！！！
            	jsonObject.put("name", arrayList.get(i)[2]);
            	jsonObject.put("Avatar", arrayList.get(i)[3]);
            	jsonArray.put(jsonObject);
    		}
    		JSONObject bigJSONObject= new JSONObject();
    		bigJSONObject.put("FriendId",friendsId);
    		bigJSONObject.put("oneFriendList",jsonArray);
    		
        	System.out.println(bigJSONObject.toString());
        	response.getWriter().append(bigJSONObject.toString()); 			
    	}else {
    		response.getWriter().append("x");
    	}
    	//DbDao.closeConnection(DbDao.conn);//在这里使用静态方法close connection
    	
	}
	
	private void friendIP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		account=request.getParameter("Account");
        System.out.println("朋友账号："+account);
        //response.getWriter().append("登录请求成功");//有了这句话，返回的就不是纯
        //Json了！！！
        String[] Fields= {"account"};
        String[] TableFields= {"ip"};
    	String[] data= {account};
    	String[] result=AddFriendTable.query("users", Fields, data, TableFields);
    	//System.out.println(result);
    	if(result!=null) {
    		System.out.println(account);
    		response.getWriter().append(result[0]);
    	}
    	
	}
	
	private void addFriend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		friendsId=request.getParameter("FriendsId");
		friendAccount=request.getParameter("FriendAccount");
		System.out.println("账号："+account);
        System.out.println("朋友账号："+friendAccount);
        //response.getWriter().append("登录请求成功");//有了这句话，返回的就不是纯
        //Json了！！！
        String[] Fields= {"account"};
        String[] TableFields= {"id","account","name","Avatar","ip"};
    	String[] data= {friendAccount};
    	String[] result=AddFriendTable.query
    			("users", Fields, data, TableFields);
    	if(result!=null) {
    		String[] FriendsTableFields= 
    			{"friendId","account","name","Avatar","ip"};
    		AddFriendTable.insert(FriendsTableFields,"friend_table_"+friendsId, result);
    		response.getWriter().append("a");
    	}else {
    		response.getWriter().append("x");
    	}
	}
	
	private void chatInit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        friendsId=request.getParameter("FriendsId");
        friendAccount=request.getParameter("FriendAccount");
        System.out.println("chatInit id"+friendsId);
        System.out.println("chatInit friendAccount"+friendAccount);
        //response.getWriter().append("登录请求成功");//有了这句话，返回的就不是纯
        //Json了！！！
        //String[] Fields= {"fromAccount","toAccount"};
        //String[] data= {account,password,name,Avatar,ip};
        String[] TableFields= {"fromAccount","toAccount","message","imagePath"};
        ArrayList<String[]> arrayList=AddFriendTable.queryAll("message_table_"+friendsId, TableFields);
    	//System.out.println(arrayList);
    	if(arrayList!=null) {
    		JSONArray jsonArray= new JSONArray();
    		for(int i=0;i<arrayList.size();i++) {
    			String fromAccount=arrayList.get(i)[0];
    			String toAccount=arrayList.get(i)[1];
    			String message=arrayList.get(i)[2];
    			String imagePath=arrayList.get(i)[3];
    			if(fromAccount.equals(this.friendAccount)||
    					toAccount.equals(this.friendAccount)) {
    				JSONObject jsonObject = new JSONObject();
        			jsonObject.put("FromAccount", fromAccount);
        			jsonObject.put("ToAccount", toAccount);//返回的都是最后一个值的时候
        			//string的特性！！！
        			if(imagePath==null) {
        				jsonObject.put("Message", message);
        				jsonObject.put("ImagePath", "");
        			}else {
        				String imagePath1=null;
        				try {
        		    		imagePath1="http://"+InetAddress.getLocalHost().getHostAddress()
        		    				+":8000/hang/upload"+imagePath;
        		    	}catch(IOException e) {
        		    		e.printStackTrace();
        		    	}
        				System.out.println(imagePath1);
        				jsonObject.put("Message", "");
                    	jsonObject.put("ImagePath", imagePath1);
        			}
                	
                	jsonArray.put(jsonObject);
    			}
    		}
        	System.out.println(jsonArray.toString());
        	response.getWriter().append(jsonArray.toString()); 			
    	}else {
    		response.getWriter().append("x");
    	}
    	//DbDao.closeConnection(DbDao.conn);//在这里使用静态方法close connection
    	
	}
    private void friendData(HttpServletRequest request, 
    		HttpServletResponse response) throws ServletException, IOException{
		userId=request.getParameter("UserId");
        friendAccount=request.getParameter("FriendAccount");
        String[] Fields= {"account"};
        String[] TableFields= {"friendId","account","name","Avatar"};
    	String[] data= {friendAccount};
    	String[] result=AddFriendTable.query
    			("friend_table_"+userId, Fields, data, TableFields);
    	if(result!=null) {//改完程序后不需要重新按启动服务器按钮，编译都是动态的！！！！
        	JSONObject jsonObject=new JSONObject();
        	jsonObject.put("FriendId",result[0]);
        	jsonObject.put("Account",result[1]);
        	jsonObject.put("Name",result[2]);
        	jsonObject.put("Avatar",result[3]);
        	System.out.println(jsonObject.toString());
        	response.getWriter().append(jsonObject.toString());
        	//DbDao.closeConnection(DbDao.conn);//在这里使用静态方法close connection
        	//之前出现的object错误与statement有关
    	}else {
    		response.getWriter().append("x");
    	}
    	
	}
    
    
	
	
}




/*try {
	String[] FriendFields={"account","password","name","Avatar"};
    AddFriendTable.createTable(FriendFields, "zzz");
    conn=DbDao.getConnection();
    Statement statement=conn.createStatement();
    //得到Statement对象
    String sql="insert into users"+
               "(account,password)"+
    		   "values('zzz','zzz')";
    //这里字符需要加单引号！！！！如果是纯数字的话则不用加引号
    
    statement.executeUpdate(sql);
    String sqlQuery="select account,password from "
    		+ "users where account='zzz'";
    ResultSet set=statement.executeQuery(sqlQuery);
    while(set.next()) {
    	account=set.getString(1);
    	password=set.getString(2);
    	response.getWriter().append(account);
    	response.getWriter().append(password);
    }
    
    
} catch (ClassNotFoundException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
} catch (SQLException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}*/