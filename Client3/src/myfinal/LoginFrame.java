package myfinal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
public class LoginFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;   // 本语句用于序列化，可以不要
	
	private JTextField txtName;
	private JFrame jf;
	LoginFrame(ClientFrame main){
		this.setSize(400, 300); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小
		this.setTitle("用户登录");
		jf = this;
		// 注意：JPasswordField用于密码，取值方法String.valueOf(pass.getPassword())
		txtName = new JTextField(main.getUserName(), 30); // 初值，列数
		JLabel nameLab = new JLabel("用户:");  // 
		nameLab.setBounds(100, 60, 40, 20);  // left,top,width,height
		txtName.setBounds(160, 60, 120, 20);
		this.add(nameLab);
		this.add(txtName);
		
		JButton JB1 = new JButton("登录");
		JB1.setBounds(160, 160, 100, 30);
		this.add(JB1);
		ActionListener al = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String nam = txtName.getText().trim();		
				if (nam.isEmpty()) {
					JOptionPane.showMessageDialog(null, "用户名不能为空！");
					return;
				}
				main.setUserName(nam);
				jf.setVisible(false);
			}
		};
		JB1.addActionListener(al);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
}
