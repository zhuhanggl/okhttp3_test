import java.io.IOException;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;

import java.util.concurrent.CopyOnWriteArraySet;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.mero.test.AddFriendTable;

import net.sf.json.*;



// @ServerEndpoint注解,标示出这是一个WebSocket的Server端 , value属性表示访问这个server的url  
@ServerEndpoint(value="/websocket/{userJSON}")  
public class WebSocketServerDemo {  
  
    private static Set<Session> peers = new HashSet<Session>();  
	//private static final Set<WebSocketServerDemo> connections =
            //new CopyOnWriteArraySet<WebSocketServerDemo>();
    private Session session;  
    private static ArrayList<user>userList=new ArrayList<user>();
    //注意这里，必须要注明是static，否则每次请求后，userList都会重新初始化
    private user oneUser; 
  
//  open方法的注解，当一个客户端连上来时触发，每个客户端会被分配一个session,这个session可不是httpsession.  
//  open方法里有个参数user被加上了注解@PathParam(value = "user")String user,这个就是从url中获取user的方式  
    @OnOpen  
    public void open(Session session , @PathParam(value="userJSON") String userJSON){  
        //session.
    	this.session = session;  
//      存储这个新session  
        peers.add(session);
    	//connections.add(this);//每次有用户连接，则该WebSocketServerDemo类会创建新的一个
        JSONObject jsonObject=new JSONObject(userJSON);
        String id=jsonObject.getString("Id");
        String account=jsonObject.getString("Account");
        String name=jsonObject.getString("Name");
        //String friendId=jsonObject.getString("FriendId");
        //String friendAccount=jsonObject.getString("FriendAccount");
        oneUser=new user(this.session.getId(),id,account,name);
        userList.add(oneUser);
        //System.out.println(userList.size());
        System.out.println("WebSocketServerDemo open from sessionId:"+this.session.getId()+" "+userJSON);  
        //System.out.println(oneUser.getId());
        //System.out.println(oneUser.getAccount());
        //System.out.println(oneUser.getName());
        //System.out.println(oneUser.getFriendId());//这个量可能没有用，后期处理掉
        //System.out.println(oneUser.getFriendAccount());
        System.out.println("在线用户数："+userList.size());
    }  
      
//  收到客户端发送的消息时触发  
    @OnMessage  
    public void inMessage(String message){  
        System.out.println("WebSocketServerDemo inmessage from sessionId:"+this.session.getId()+  
                    ":"+message);
        JSONObject jsonObject=new JSONObject(message);
        sendMessage(jsonObject.getString("From"),jsonObject.getString("To")
        		,jsonObject.getString("ToId"),jsonObject.getString("Message"));      
    }  
      
//  客户端断开时触发  
    @OnClose  
    public void end() throws IOException{  
        System.out.println("WebSocketServerDemo end from sessionId:"+this.session.getId());
        this.session.close();
        peers.remove(this.session);
        //connections.remove(this);
        userList.remove(oneUser);
    }  
  
  
//  向所有客户端发送一条消息  
    public static void broadcase(String msg){      
        System.out.println("WebSocketserverDemo broadcase ");  
        for(Session session : peers){  
            try {  
                session.getBasicRemote().sendText(msg);  
            } catch (IOException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
    }
    
    private void sendMessage(String from,String to,String toId,String msg){//采用JSON
    	//String userSessionId=null;
    	String friendSessionId = null;
    	//boolean userFinded=false;
    	//boolean friendFinded=false;
    	String[] TableFields= {"fromAccount","toAccount","message"};
    	String[] data= {from,to,msg};
    	AddFriendTable.insert(TableFields,"message_table_"+oneUser.getId(), data);
    	AddFriendTable.insert(TableFields,"message_table_"+toId, data);
    	//无论找没找到用户，仍然需要在数据库中存储数据
        for(int i=0;i<userList.size();i++) {//需要考虑没找到的情况
        	/*if(userList.get(i).getAccount().equals(from)) {
        		userSessionId=userList.get(i).getSessionId();
        		userFinded=true;
        	}*/
        	if(userList.get(i).getAccount().equals(to)) {
        		friendSessionId=userList.get(i).getSessionId();
        		//friendFinded=true;
        		//String[] TableFields= {"fromAccount","toAccount","message"};
            	//String[] data= {from,to,msg};
            	//AddFriendTable.insert(TableFields,"message_table_"+oneUser.getId(), data);
            	//AddFriendTable.insert(TableFields,"message_table_"+toId, data);
        		break;
        	}
        	/*if(userFinded&&friendFinded)
        		break;*/
        }
        //System.out.println("userSessionId:"+userSessionId);
        System.out.println("friendSessionId:"+friendSessionId);
        for(Session session : peers) {
        	if(session.getId().equals(friendSessionId)) {
        		try {
        			JSONObject jsonObject=new JSONObject();
        			jsonObject.put("FromAccount",from);
        			jsonObject.put("Message", msg);
                    session.getBasicRemote().sendText(jsonObject.toString());  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }
        		break;
        	}
        }
        
        
    }
}