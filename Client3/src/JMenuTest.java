import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class JMenuTest extends JFrame {
	private static final long serialVersionUID = 1L;   // 本语句用于序列化，可以不要
	
	JMenuTest() {
		this.setSize(600, 500);           // 设置主窗口的width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null);             // 空布局，可以定位组件在容器中的位置和大小		
		this.setJMenuBar(addMenu(this));
		this.setTitle("菜单测试");

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("触发windowClosing事件");	// 点击窗口右上角X关闭时的事件			
			}
		});

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 使窗口右上角X关闭有效
	}

	void exit() {		
		System.exit(0);  // 退出系统。不会触发windowClosing事件
	}
    
	JMenuBar addMenu(JFrame jf) {		
		JMenuBar jmenu = new JMenuBar();   // 创建菜单
		jf.setJMenuBar(jmenu);    // 不能设定位置，会自动放在最上部
		
		// 添加菜单
		JMenu menu1 = new JMenu(" 文件");
		JMenu menu2 = new JMenu(" 帮助");
		JMenuItem item1 = new JMenuItem(" 菜单测试1");
		JMenuItem item2 = new JMenuItem(" 菜单测试2");
		JMenuItem item3 = new JMenuItem(" 退出");
		
		// 添加菜单项至菜单上
		menu1.add(item1);
		menu1.add(item2);
		menu1.add(item3);

		jmenu.add(menu1);
		jmenu.add(menu2);
		
		ActionListener al = new ActionListener() { // 定义菜单点击事件
			@Override
			public void actionPerformed(ActionEvent e) {
				String str = e.getActionCommand();
				if (" 菜单测试1".equals(str)) {
					JOptionPane.showMessageDialog(null, "菜单测试1");
				} else if (" 菜单测试2".equals(str)) {
					JOptionPane.showMessageDialog(null, "菜单测试2");
				} else if (" 退出".equals(str)) {
					exit();
				}
			}
		};
		item1.addActionListener(al);   // 菜单项加上点击事件
		item2.addActionListener(al);
		item3.addActionListener(al);

		return jmenu;
	}

	public static void main(String[] args) throws Exception {
		new JMenuTest(); 
	}
}