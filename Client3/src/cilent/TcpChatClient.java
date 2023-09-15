package cilent;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class TcpChatClient extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final int LEFT_POS = 40;
	private final int INPUT_WIDTH = 360;
	private String userName=null;  // JLogin可通过getUserName()和setUserName()读写
	private TcpChatClient main;
	private JTextField txtSend;
	private JTextArea txtReceive;
	private Sock sock = null;       // 代理模式：代理Sock实例
  
	TcpChatClient(){
		sock=new Sock();
		this.setTitle("未登陆");
		this.setSize(600, 500); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		main = this;
		
		//输入文本框界面
		txtSend = new JTextField("Hello, world!", 30); // 初值，列数
		JLabel lblSendLab = new JLabel("输入:");
		lblSendLab.setBounds(LEFT_POS, 40, 40, 20);   // 设置矩形大小(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)  
		txtSend.setBounds(LEFT_POS + 60, 40, INPUT_WIDTH, 20); //left,top,width,height
		txtSend.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128)));// 设置边界颜色：RedGreenBlue(0~255)
		txtSend.setBackground(Color.BLUE);// 设置背景色
		txtSend.setForeground(Color.WHITE); // 设置前景色
	    this.add(lblSendLab);   // 在当前窗口(JFrame)中增加控件
		this.add(txtSend);
		
		//发送信息按钮
		JButton sendButton= new JButton("发送");
		sendButton.setBounds(LEFT_POS + 60 + INPUT_WIDTH + 20, 40, 60, 20);
		this.add(sendButton);
		
		//发送信息事件
		ActionListener sendListener = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(!sock.isConn()) {
						JOptionPane.showMessageDialog(null, "当前还没有登录");
						return;
					}
					String msg=txtSend.getText();
					write("3"+msg+"\r\n");
				}catch (Exception e1) {
					e1.getStackTrace();
				}
			}
		};
		sendButton.addActionListener(sendListener);
		
		//消息显示文本框界面
		txtReceive = new JTextArea();
		JScrollPane jsp = new JScrollPane(txtReceive);
		jsp.setBounds(LEFT_POS + 60, 90, INPUT_WIDTH, 300); 		
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);// 默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
		jsp.setAutoscrolls(true);
		this.add(jsp);
		JLabel lblrecLab = new JLabel("收到:");
		lblrecLab.setBounds(LEFT_POS, 90, 40, 20);
		this.add(lblrecLab);
		
		//菜单界面
		JMenuBar jmenu = new JMenuBar();   // 创建菜单
		setJMenuBar(jmenu);    // 不能设定位置，会自动放在最上部
		
		JMenu fileMenu = new JMenu(" 文件");
		JMenu  helpMenu= new JMenu(" 帮助");
		JMenuItem item1 = new JMenuItem("用户登录");
		JMenuItem item2 = new JMenuItem("取消登录");
		JMenuItem item3 = new JMenuItem("用户注册");
		JMenuItem item4 = new JMenuItem("设置");
		JMenuItem item5 = new JMenuItem("退出");
		
		fileMenu.add(item1);
		fileMenu.add(item2);
		fileMenu.add(item3);
		fileMenu.add(item4);
		fileMenu.add(item5);
		
		jmenu.add(fileMenu);
		jmenu.add(helpMenu);
		
		//菜单事件
		ActionListener menuListener = new ActionListener() { // 定义菜单点击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				if ("用户登录".equals(str)) {
					new Login(main);
				} 
				else if ("取消登录".equals(str)) {
					if(sock.isConn()) {
						main.setTitle("未连接");
						disconn();
					}
				} 
				else if ("用户注册".equals(str)) {
					new Register(main);
				}else if("设置".equals(str)) {
					new Setting(main);
				}else if("退出".equals(str)) {
					exit();
				}
			}
		};
		item1.addActionListener(menuListener);   // 菜单项加上点击事件
		item2.addActionListener(menuListener);
		item3.addActionListener(menuListener);
		item4.addActionListener(menuListener);
		item5.addActionListener(menuListener);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
	}
	void setUserName(String userName) {
		this.userName = userName;
		setTitle("已登录 -- "+sock.getServerName()+":"+sock.getPort()+":"+userName);
	}

	String getUserName() {
		return this.userName;
	}

    boolean conn() {
		return sock.conn();
	}
	

	String read() {
		return sock.read();		
	}
	
	boolean write(String msg) {
		return sock.write(msg);
	};	

	boolean disconn() {
		write("0U\r\n");  //要求服务器关闭连接
		try {
		   Thread.sleep(200);
		} catch(Exception ex) {
			
		}		
		return close();		
	}
	
	boolean close() {
		return sock.close();		
	}
	
	void setSock(String ip,int port) {
		sock.setServerName(ip);
		sock.setPort(port);
	}
	void exit() {		
		System.exit(0);  // 退出系统。不会触发windowClosing事件
	}
	
	void addMessage(String msg) {
		txtReceive.append(msg);
	}

    

    // 在登录成功后客户端接收消息
   void receiveFromServer() {
	    new Thread() {
		@Override
			public void run() {
				String msg;
				while (true) {
					if (sock == null || !sock.isConn()) {
						break;
					}
					try {
						msg = read();
						String type = msg.substring(0,1);
						if(type.equals("3"))  // 3-收到普通消息. 其他：1-登录成功消息
							txtReceive.append(msg.substring(1));  
						else if(type.equals("0")) {    // 0-关闭连接消息
								if(close()) {
									userName=null;
									setTitle("未连接");        // 设置窗口标题
								}
						}
					} catch (Exception ex) {
						// sock = null;
					}
				}
			}   // run()
	    }.start();
	}  // receiveFrom()
   
   public static void main(String[] args) {
	   new TcpChatClient();
   }
}
