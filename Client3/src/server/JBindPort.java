package server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import cilent.TcpChatClient;

public class JBindPort extends JFrame{
	private static final long serialVersionUID = 1L;   // 本语句用于序列化，可以不要
	
	private JTextField txtPort;
	private JFrame jf;
	
	JBindPort(TcpChatServer main){
		this.setSize(400, 300); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		this.setTitle("选择端口");
		jf = this;
		//端口输入文本框界面
		txtPort = new JTextField(30); // 初值，列数
		JLabel portLab = new JLabel("端口:");  // 
		portLab.setBounds(100, 60, 40, 20);  // left,top,width,height
		txtPort.setBounds(160, 60, 120, 20);
		this.add(portLab);
		this.add(txtPort);
		//确定按钮
		JButton JB1 = new JButton("确定");
		JB1.setBounds(160, 160, 100, 30);
		this.add(JB1);
		//确定按钮事件
		ActionListener loginListener = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String portString = txtPort.getText().trim();
				if (portString.isEmpty()) {
					JOptionPane.showMessageDialog(null, "端口不能为空！");
					return;
				}
				//建立登录连接
				if(!main.setAndConn(Integer.valueOf(portString))) {
					JOptionPane.showMessageDialog(null, "绑定端口失败");
					return;
				}
				JOptionPane.showMessageDialog(null, "绑定端口成功");
				main.setTitle("服务器： "+portString);
				main.accept();
				jf.setVisible(false);
			}
		};
		JB1.addActionListener(loginListener);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
}
