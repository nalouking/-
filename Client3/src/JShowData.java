import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JShowData extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final int LEFT_POS = 40;
	private final int INPUT_WIDTH = 360;
	private final String DRIVER = "com.hxtt.sql.access.AccessDriver";
	private final String CONN_STRING = "jdbc:Access:///./data/msg.mdb"; // .为当前目录，即本项目的根目录。
	
	static int count = 0;
	JTextArea txtaReceive;
	DataConn  dataConn;

	JShowData() {
		this.setSize(600, 500); // 设置width和height
		this.setLocationRelativeTo(null); // 居屏幕中间
		this.setLayout(null); // 空布局，可以准确的定位组件在容器中的位置和大小

		dataConn = new DataConn();
		dataConn.connect(DRIVER,CONN_STRING);
		
		// 在文本框上添加滚动条
		txtaReceive = new JTextArea();
		JScrollPane jsp = new JScrollPane(txtaReceive);
		jsp.setBounds(LEFT_POS + 60, 90, INPUT_WIDTH, 300); 		
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);// 默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
		jsp.setAutoscrolls(true);
		this.add(jsp);

		showUsers();
		
		JButton btnTest = new JButton("测试");
		btnTest.setBounds(LEFT_POS + 60 + INPUT_WIDTH + 20, 40, 60, 20);
		this.add(btnTest);

		ActionListener al = new ActionListener() { // 加上按键事件
			@Override
			public void actionPerformed(ActionEvent e) {
				addUser();
			}
		};
		btnTest.addActionListener(al);

		this.setVisible(true);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	void showUsers() {
		count = 0;
		txtaReceive.setText("");
		ResultSet rs = dataConn.executeQuery("select User_Cde,User_Pass,User_LogTime From Users order by ID");
		try {
		  while(rs.next()) {
		     txtaReceive.append(count++ +". "+  rs.getString("User_Cde")+ " " + rs.getString("User_Pass") + " " + rs.getDate("User_LogTime") + "\r\n");  // 加到末尾
		  }
		}
		catch(Exception ex) {
			
		}
	}

	// 由于引入的连接包有错，需要判断是否完成可以采用exist
	void addUser() {
		Random rnd =new Random();
		String userName= "user" +rnd.nextInt(10000);
		String values = String.format("'%s','%s','%s'", userName,"pass"+rnd.nextInt(10000), getNow());				
		try {
			dataConn.executeUpdate("insert into Users(User_Cde,User_Pass,User_LogTime) values(" 
                                + values +")");			
		}
		catch(Exception ex) {
			
		}
		if(dataConn.existQuery("select User_Cde from Users where User_Cde='"+userName+"'")) 
		   showUsers();
	}
		
	static String getNow() {
		   Calendar cal= Calendar.getInstance();
		   SimpleDateFormat sformat  =   new  SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		   return sformat.format(cal.getTime());
	}	

	public static void main(String[] args) throws Exception {
		new JShowData();
	}
}