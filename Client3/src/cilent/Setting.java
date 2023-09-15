package cilent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Setting extends JFrame{
	private static final long serialVersionUID = 1L;   // 本语句用于序列化，可以不要
	
	private JTextField serverIp;
	private JTextField serverPort;
	private JFrame jf;
	
	Setting(TcpChatClient main){
		this.setSize(400, 300); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		this.setTitle("设置");
		jf = this;
		// 注意：JPasswordField用于密码，取值方法String.valueOf(pass.getPassword())
		
		//服务器ip界面
		serverIp = new JTextField(main.getUserName(), 30); // 初值，列数
		JLabel ipLab = new JLabel("服务器:");  // 
		ipLab.setBounds(100, 60, 40, 20);  // left,top,width,height
		serverIp.setBounds(160, 60, 120, 20);
		this.add(ipLab);
		this.add(serverIp);
		
		//端口界面
		serverPort= new JPasswordField(30);
		JLabel portLab=new JLabel("端口：");
		portLab.setBounds(100, 100, 40, 20);  // left,top,width,height
		serverPort.setBounds(160, 100, 120, 20);
		this.add(portLab);
		this.add(serverPort);
		
		//确定按钮
		JButton JB1 = new JButton("确定");
		JB1.setBounds(160, 160, 100, 30);
		this.add(JB1);
		ActionListener loginListener = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip = serverIp.getText().trim();
				String port = serverPort.getText().trim();
				if (ip.isEmpty()) {
					JOptionPane.showMessageDialog(null, "服务器ip不能为空！");
					return;
				}
				if (port.isEmpty()) {
					JOptionPane.showMessageDialog(null, "服务器端口不能为空！");
					return;
				}
				main.setSock(ip, Integer.valueOf(port));
				jf.setVisible(false);
			}
		};
		JB1.addActionListener(loginListener);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
}
