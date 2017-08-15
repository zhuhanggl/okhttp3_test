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



// @ServerEndpointע��,��ʾ������һ��WebSocket��Server�� , value���Ա�ʾ�������server��url  
@ServerEndpoint(value="/websocket/{userJSON}")  
public class WebSocketServerDemo {  
  
    private static Set<Session> peers = new HashSet<Session>();  
	//private static final Set<WebSocketServerDemo> connections =
            //new CopyOnWriteArraySet<WebSocketServerDemo>();
    private Session session;  
    private static ArrayList<user>userList=new ArrayList<user>();
    //ע���������Ҫע����static������ÿ�������userList�������³�ʼ��
    private user oneUser; 
  
//  open������ע�⣬��һ���ͻ���������ʱ������ÿ���ͻ��˻ᱻ����һ��session,���session�ɲ���httpsession.  
//  open�������и�����user��������ע��@PathParam(value = "user")String user,������Ǵ�url�л�ȡuser�ķ�ʽ  
    @OnOpen  
    public void open(Session session , @PathParam(value="userJSON") String userJSON){  
        //session.
    	this.session = session;  
//      �洢�����session  
        peers.add(session);
    	//connections.add(this);//ÿ�����û����ӣ����WebSocketServerDemo��ᴴ���µ�һ��
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
        //System.out.println(oneUser.getFriendId());//���������û���ã����ڴ����
        //System.out.println(oneUser.getFriendAccount());
        System.out.println("�����û�����"+userList.size());
    }  
      
//  �յ��ͻ��˷��͵���Ϣʱ����  
    @OnMessage  
    public void inMessage(String message){  
        System.out.println("WebSocketServerDemo inmessage from sessionId:"+this.session.getId()+  
                    ":"+message);
        JSONObject jsonObject=new JSONObject(message);
        sendMessage(jsonObject.getString("From"),jsonObject.getString("To")
        		,jsonObject.getString("ToId"),jsonObject.getString("Message"));      
    }  
      
//  �ͻ��˶Ͽ�ʱ����  
    @OnClose  
    public void end() throws IOException{  
        System.out.println("WebSocketServerDemo end from sessionId:"+this.session.getId());
        this.session.close();
        peers.remove(this.session);
        //connections.remove(this);
        userList.remove(oneUser);
    }  
  
  
//  �����пͻ��˷���һ����Ϣ  
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
    
    private void sendMessage(String from,String to,String toId,String msg){//����JSON
    	//String userSessionId=null;
    	String friendSessionId = null;
    	//boolean userFinded=false;
    	//boolean friendFinded=false;
    	String[] TableFields= {"fromAccount","toAccount","message"};
    	String[] data= {from,to,msg};
    	AddFriendTable.insert(TableFields,"message_table_"+oneUser.getId(), data);
    	AddFriendTable.insert(TableFields,"message_table_"+toId, data);
    	//������û�ҵ��û�����Ȼ��Ҫ�����ݿ��д洢����
        for(int i=0;i<userList.size();i++) {//��Ҫ����û�ҵ������
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