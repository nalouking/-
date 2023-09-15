import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class JSubFrameTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final int LEFT_POS = 40;
	private final int INPUT_WIDTH = 360;
	private String userName = "张三";
	private JSubFrameTest main;
	private JTextField txtSend;
	private JTextArea txtaReceive;
	private static int count = 0;

	JSubFrameTest() {
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


		JButton btnTest = new JButton("测试");
		btnTest.setBounds(LEFT_POS + 60 + INPUT_WIDTH + 20, 40, 60, 20);
		this.add(btnTest);

		ActionListener al = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				new FrameLogin(main);
			}
		};
		btnTest.addActionListener(al);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}


	void setUserName(String userName) {
		this.userName = userName;
		txtSend.setText(userName);
		//txtaReceive.insert(count++ +". "+ getNow()+" "+userName+"\r\n", 0);  //加到最前面 
		txtaReceive.append(count++ +". "+ getNow()+" "+userName+"\r\n");  // 加到末尾
		txtaReceive.append(count++ +". "+ getNow()+" "+userName+"\r\n");  // 加到末尾
		txtaReceive.append(count++ +". "+ getNow()+" "+userName+"\r\n");  // 加到末尾
		txtaReceive.append(count++ +". "+ getNow()+" "+userName+"\r\n");  // 加到末尾
	}
	
	String getNow() {
		   Calendar cal= Calendar.getInstance();
		   return String.format("%tT", cal) + "   ";
	}
	
	String getUserName() {
		return this.userName;
	}
	

	public static void main(String[] args) throws Exception {
		new JSubFrameTest();
	}
}