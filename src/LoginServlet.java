

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
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
	private String req;
	private String friendsId;
	private String account;
	private String friendAccount;
	private String password;
	private String name;
	private String Avatar;
	private String ip;
	
       
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
		System.out.println("GET����ɹ�");
        response.getWriter().append("GET");//����ַget�Ļ�������ҳ��ʾ����GETzhuhang
        
        //doPost(request,response);
        //request�ǿͻ��˷�����������
        //response�Ƿ��������ظ��ͻ��˵�
        
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        System.out.println("Post����ɹ�");
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
        //AddFriendTable.closeStatement(AddFriendTable.statement);
        //DbDao.closeConnection(DbDao.conn);//������ʹ�þ�̬����close connection
	}
	
	
	
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		account=request.getParameter("Account");
        password=request.getParameter("Password");
        System.out.println(account);
        System.out.println(password);
        //response.getWriter().append("��¼����ɹ�");//������仰�����صľͲ��Ǵ�
        //Json�ˣ�����
     
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
            	//LinkedHashMap�������
            	//HashMap�������
            	//JSONArray array = JSONArray.fromObject(map);//�ò��˵Ļ����ղص���ַ
            	//ʹ��json����java.lang.NoClassDefFoundError: net/sf/json/JSONObject�쳣�Ľ���취
            	//��ʹ��json���쳣������ava.lang.NoClassDefFoundError: net/sf/json/JSONArray��
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
    	if(result==null) {//����������Ҫ���°�������������ť�����붼�Ƕ�̬�ģ�������
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
        	    //���ݿ��в�����from��to��Ϊ����������
        	    String MessageTableName="message_table_"+Integer.toString(id);
        	    AddFriendTable.createTable(MessageTableFields, MessageTableName);   
        	}
        	//DbDao.closeConnection(DbDao.conn);//������ʹ�þ�̬����close connection
        	//֮ǰ���ֵ�object������statement�й�
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
    	response.getWriter().append("IP��������ɹ�");
    	AddFriendTable.userIPUpdate(account,ip);
    	
    	//֮ǰ���ֵ�object������statement�й�
	}
	
	private void friendsLoading(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        friendsId=request.getParameter("FriendsId");
        System.out.println(friendsId);
        //response.getWriter().append("��¼����ɹ�");//������仰�����صľͲ��Ǵ�
        //Json�ˣ�����
        String[] TableFields= {"friendId","account","name","Avatar"};
        ArrayList<String[]> arrayList=AddFriendTable.queryAll("friend_table_"+friendsId, TableFields);
    	//System.out.println(arrayList);
    	if(arrayList!=null) {
    		JSONArray jsonArray= new JSONArray();
    		for(int i=0;i<arrayList.size();i++) {
    			JSONObject jsonObject = new JSONObject();
    			jsonObject.put("id", arrayList.get(i)[0]);
    			jsonObject.put("account", arrayList.get(i)[1]);//���صĶ������һ��ֵ��ʱ��
    			//string�����ԣ�����
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
    	//DbDao.closeConnection(DbDao.conn);//������ʹ�þ�̬����close connection
    	
	}
	
	private void friendIP(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		account=request.getParameter("Account");
        System.out.println("�����˺ţ�"+account);
        //response.getWriter().append("��¼����ɹ�");//������仰�����صľͲ��Ǵ�
        //Json�ˣ�����
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
		System.out.println("�˺ţ�"+account);
        System.out.println("�����˺ţ�"+friendAccount);
        //response.getWriter().append("��¼����ɹ�");//������仰�����صľͲ��Ǵ�
        //Json�ˣ�����
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
}




/*try {
	String[] FriendFields={"account","password","name","Avatar"};
    AddFriendTable.createTable(FriendFields, "zzz");
    conn=DbDao.getConnection();
    Statement statement=conn.createStatement();
    //�õ�Statement����
    String sql="insert into users"+
               "(account,password)"+
    		   "values('zzz','zzz')";
    //�����ַ���Ҫ�ӵ����ţ�����������Ǵ����ֵĻ����ü�����
    
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