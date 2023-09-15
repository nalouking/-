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

import com.hxtt.b.n;

import myfinal.ClientFrame;

public class Login extends JFrame{
	private static final long serialVersionUID = 1L;   // 本语句用于序列化，可以不要
	
	private JTextField txtName;
	private JPasswordField txtPassword;
	private JFrame jf;
	
	Login(TcpChatClient main){
		this.setSize(400, 300); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		this.setTitle("用户登录");
		jf = this;
		// 注意：JPasswordField用于密码，取值方法String.valueOf(pass.getPassword())
		
		//用户名界面
		txtName = new JTextField(main.getUserName(), 30); // 初值，列数
		JLabel nameLab = new JLabel("用户:");  // 
		nameLab.setBounds(100, 60, 40, 20);  // left,top,width,height
		txtName.setBounds(160, 60, 120, 20);
		this.add(nameLab);
		this.add(txtName);
		
		//密码界面
		txtPassword= new JPasswordField(30);
		JLabel passwordLab=new JLabel("密码：");
		passwordLab.setBounds(100, 100, 40, 20);  // left,top,width,height
		txtPassword.setBounds(160, 100, 120, 20);
		this.add(passwordLab);
		this.add(txtPassword);
		
		//登录按钮
		JButton JB1 = new JButton("登录");
		JB1.setBounds(160, 160, 100, 30);
		this.add(JB1);
		ActionListener loginListener = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText().trim();
				String password = String.valueOf(txtPassword.getPassword());
				if (name.isEmpty()) {
					JOptionPane.showMessageDialog(null, "用户名不能为空！");
					return;
				}
				if (password.isEmpty()) {
					JOptionPane.showMessageDialog(null, "密码不能为空！");
					return;
				}
				//建立登录连接
				if(!main.conn()) {
					System.out.println("登录连接失败");
					return;
				}
				main.write("1"+name+"#"+password);
				String loginMsg=main.read();
				if(loginMsg.equals("0")) {//用户名错误
					main.close();
					JOptionPane.showMessageDialog(null, "用户不存在");
					return;
				}
				if(loginMsg.equals("1")) {//密码错误
					main.close();
					JOptionPane.showMessageDialog(null, "密码错误");
					return;
				}
				if(loginMsg.equals("2")) {//重复登录
					main.close();
					JOptionPane.showMessageDialog(null, "请勿重复登录");
					return;
				}
				//登录成功
				main.setUserName(name);
				main.addMessage(loginMsg.substring(1));
				main.receiveFromServer();
				jf.setVisible(false);
				return;
			}
		};
		JB1.addActionListener(loginListener);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
}
