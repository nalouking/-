package server;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import cilent.TcpChatClient;


public class TcpChatServer extends JFrame {
	private MasterSock masterSock; // 建立监听套接字
	private ClientChat clientChat;
	private JTextArea txtRecv;
	private JTextArea txtUser;
	private TcpChatServer main;
	private User users;
	private Msg msgs;
	private final int LEFT_POS = 60;
	private final int REC_WIDTH = 320;
	private final int USER_WIDTH = 100;

    TcpChatServer() {      // 构造器
		masterSock = new MasterSock();
		clientChat = new ClientChat(this);
		DataConnect dataConn=new DataConnect();
		setWindows();
		users = new User(dataConn);
		msgs = new Msg(dataConn);
	}
    boolean setAndConn(int port) {
    	masterSock.setPort(port);
    	return masterSock.conn();
    }
	void accept() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
			       Socket socket = masterSock.accept();
			       HashMap<String, Object> hm = null;
			       if (socket != null) {
			    	  hm = clientChat.addSocket(socket);
				      if (hm != null)
			    	    clientChat.readFromClient(hm);
			       }
		        }			
			}
		}.start();
	}
	
	void close() {
		this.setTitle("服务器: 未绑定");
		txtUser.setText("");
		txtRecv.setText("");
		clientChat.sendToAll("0");
		clientChat.removeAll();
		masterSock.close();
	}
	void setWindows() {
		this.setTitle("服务器: 未绑定");
		this.setSize(600, 500); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		main = this;
		
		//消息显示文本框界面
		txtRecv = new JTextArea();
		JScrollPane rjsp = new JScrollPane(txtRecv);
		rjsp.setBounds(LEFT_POS , 60, REC_WIDTH, 300); 		
		rjsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);// 默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
		rjsp.setAutoscrolls(true);
		this.add(rjsp);
		
		//用户显示文本框界面
		txtUser = new JTextArea();
		JScrollPane ujsp = new JScrollPane(txtUser);
		ujsp.setBounds(LEFT_POS + 20 +REC_WIDTH, 60, USER_WIDTH, 300); 
		ujsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		ujsp.setAutoscrolls(true);
		this.add(ujsp);
		
		//菜单界面
		JMenuBar jmenu = new JMenuBar();   // 创建菜单
		setJMenuBar(jmenu);    // 不能设定位置，会自动放在最上部
		
		JMenu fileMenu = new JMenu(" 文件");
		JMenu  helpMenu= new JMenu(" 帮助");
		JMenuItem item1 = new JMenuItem("绑定端口");
		JMenuItem item2 = new JMenuItem("解除绑定");
		JMenuItem item3 = new JMenuItem("退出");
		
		fileMenu.add(item1);
		fileMenu.add(item2);
		fileMenu.add(item3);
		
		jmenu.add(fileMenu);
		jmenu.add(helpMenu);
		
		//菜单事件
		ActionListener menuListener = new ActionListener() { // 定义菜单点击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				if ("绑定端口".equals(str)) {
					new JBindPort(main);
				} else if ("解除绑定".equals(str)) {
					close();
				} else if ("退出".equals(str)) {
					System.exit(0);
				}
			}
		};
		item1.addActionListener(menuListener);   // 菜单项加上点击事件
		item2.addActionListener(menuListener);
		item3.addActionListener(menuListener);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	void insertMsg(String user,String msg,String time) {
		String values = String.format("'%s','%s','%s'", user,msg, time);
		msgs.insert(values);
	}
	void addUser(String u,String p) {
		String values = String.format("'%s','%s'", u,p);
		users.addUser(values);
	}
	boolean checkUser(String u) {
		return users.userExist(u.trim());
	}
	
	boolean checkPass(String u, String p) {
		return users.passMach(u.trim(), p.trim());
	}
	
	String getLatest() {
		return msgs.getLatest();
	}
	
	void refreshUser() {
		ArrayList<String> ulist=clientChat.getUserlist();
//		System.out.println(ulist.size());
		txtUser.setText("");
		for(String name:ulist) {
//			System.out.println(name);
			txtUser.append(name+"\r\n");
		}
	}
	
	void addMessage(String s) {
		txtRecv.append(s);
	}
	
	public static void main(String[] args) {
		   new TcpChatServer();
	   }
}
