package server;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


class ClientChat {
    private TcpChatServer main;
    private ArrayList<HashMap<String, Object>> sockList;

	ClientChat(TcpChatServer main) {
		this.main = main; 
		sockList = new ArrayList<HashMap<String, Object>>();
	}
	
	HashMap<String, Object> addSocket(Socket socket){
		HashMap<String, Object> hm=new HashMap<String, Object>();
		hm.put("sock", new SlaveSock(socket));
		hm.put("user", null);
		sockList.add(hm);
		return hm;
	}
	
	void removeUser(HashMap<String, Object> hmc) {
		SlaveSock sock = (SlaveSock) hmc.get("sock");
		sock.close();
		int count=0;
		for(HashMap<String, Object> hm:sockList) {
			if(hm.get("user")==hmc.get("user")) {
				sockList.remove(count);
				break;
			}
			count++;
		}
		main.refreshUser();
	}
	
	void removeAll() {
		int count=0;
		for(HashMap<String, Object> hm:sockList) {
			SlaveSock sock = (SlaveSock)hm.get("sock");
			sock.close();
			sockList.remove(count);
			count++;
		}
	}
	
	void readFromClient(HashMap<String, Object> hm) {
		 new Thread() {
			@Override
			public void run() {
				HashMap<String, Object> hmc = hm;
				SlaveSock sock = (SlaveSock) hmc.get("sock");
				while (true){
					String msg = sock.read();
					if(msg == null) {
						sock.write("0U\r\n");  // 发送关闭连接消息
						break;
					}
					String type = msg.substring(0, 1);						
					if (type.equals("0")) {   // 收到关闭连接消息
						break;
					}
					if (type.equals("1")) {			//收到登录信息
						String[] message=msg.substring(1).split("#");
						if(!main.checkUser(message[0])) {//若用户不存在
							sock.write("0");
							break;
						}
						if(!main.checkPass(message[0], message[1])) {//若密码错误
							sock.write("1");
							break;
						}
						//登录成功
						hm.put("user", message[0]);
						sock.write("3"+main.getLatest());
						main.refreshUser();
					}
					if (type.equals("2")) {
						String[] message=msg.substring(1).split("#");						//收到注册信息
						if(main.checkUser(message[0])) {//若用户已存在
							sock.write("0");
							break;
						}
						//注册成功
						main.addUser(message[0], message[1]);
						sock.write("1");
						break;
					}
					if (type.equals("3")) {   // 收到普通消息，转发给所有已登录用户
						sendToAll("3"+ getNow() + "   "+ hmc.get("user") + "：" + msg.substring(1));
						main.insertMsg(hmc.get("user").toString(),msg.substring(1),getNow());
						main.addMessage(getNow()+" "+hmc.get("user").toString()+": "+msg.substring(1));
					}
//					else if (type.equals("4"))   // 收到修改密码的消息(未实现)
//						changePass(msg.substring(1));
				}
				removeUser(hmc);				
			}  // run
		}.start();
	} // readFrom
	
	String getNow() {
		   Calendar cal= Calendar.getInstance();
		   SimpleDateFormat sformat  =   new  SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   return sformat.format(cal.getTime());
	}
	
	void sendToAll(String msg) {
		for(HashMap<String, Object> hm:sockList) {
			SlaveSock sock = (SlaveSock)hm.get("sock");
			sock.write(msg);
		}
	}
	ArrayList<String> getUserlist(){
		ArrayList<String> uList=new ArrayList<String>();
		for(HashMap<String, Object> hm:sockList) {
			uList.add((String)hm.get("user"));
		}
		return uList;
	}
}
