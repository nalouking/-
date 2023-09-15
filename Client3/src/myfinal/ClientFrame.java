package myfinal;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class ClientFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private final int LEFT_POS = 40;
	private final int INPUT_WIDTH = 360;
	private String userName = "张三";
	private ClientFrame main;
	private JTextField txtSend;
	private JTextArea txtaReceive;
	
	public ClientFrame() {
		this.setSize(600, 500); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		main = this;

		txtSend = new JTextField("Hello, world!", 30); // 初值，列数
		JLabel lblSendLab = new JLabel("输入:");
		lblSendLab.setBounds(LEFT_POS, 40, 40, 20);   // 设置矩形大小(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)  
		txtSend.setBounds(LEFT_POS + 60, 40, INPUT_WIDTH, 20); //left,top,width,height
		txtSend.setBorder(BorderFactory.createLineBorder(new Color(128, 128, 128)));// 设置边界颜色：RedGreenBlue(0~255)
		txtSend.setBackground(Color.BLUE);// 设置背景色
		txtSend.setForeground(Color.WHITE); // 设置前景色
	    this.add(lblSendLab);   // 在当前窗口(JFrame)中增加控件
		this.add(txtSend);		

		// 在文本框上添加滚动条
		txtaReceive = new JTextArea();
		JScrollPane jsp = new JScrollPane(txtaReceive);
		jsp.setBounds(LEFT_POS + 60, 90, INPUT_WIDTH, 300); 		
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);// 默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
		jsp.setAutoscrolls(true);
		this.add(jsp);

		//通过登录按钮设置用户名
		JButton btnTest = new JButton("登录");
		btnTest.setBounds(LEFT_POS + 60 + INPUT_WIDTH + 20, 70, 60, 20);
		this.add(btnTest);

		ActionListener al = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				new LoginFrame(main);
			}
		};
		btnTest.addActionListener(al);
		
		//输出按钮
		
		JButton sendButton= new JButton("发送");
		sendButton.setBounds(LEFT_POS + 60 + INPUT_WIDTH + 20, 40, 60, 20);
		this.add(sendButton);
		
		ActionListener a2 = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Socket socket = new Socket("localhost", 8000); // 与服务器 建立连接。
					DataInputStream fromServer = new DataInputStream(socket.getInputStream());
					DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
					String s1=getUserName()+"#"+txtSend.getText()+"\r\n";
					toServer.writeUTF(s1);
					String s2 = fromServer.readUTF();   // 从输入缓冲区读入字符，没有则阻塞***
					addResponse(s2);
					socket.close(); // 关闭连接
				}catch (Exception e1) {
					e1.getStackTrace();
				}
			}
		};
		sendButton.addActionListener(a2);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	void setUserName(String userName) {
		this.userName = userName;
	}
	
	void addResponse(String response) {
		txtaReceive.append(response+"\r\n");
	}
	
	String getUserName() {
		return this.userName;
	}
}
