
public class user {
	private String sessionId;
	private String id;
	private String account;
	private String name;//getter和setter生成：快捷键Alt + Shift + S，再按R键
	//private String friendId;
	//private String friendAccount;
	
	public user(String sessionId, String id,String account,String name) {
		this.sessionId=sessionId;
		this.id=id;
		this.account=account;
		this.name=name;
		//this.friendId=friendId;
		//this.friendAccount=friendAccount;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/*public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getFriendAccount() {
		return friendAccount;
	}
	public void setFriendAccount(String friendAccount) {
		this.friendAccount = friendAccount;
	}*/

}
