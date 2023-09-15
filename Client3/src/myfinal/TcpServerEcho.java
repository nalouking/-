package myfinal;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class TcpServerEcho {
	private final static String DRIVER = "com.hxtt.sql.access.AccessDriver";
	private final static String CONN_STRING = "jdbc:Access:///./data/msg.mdb"; // .为当前目录，即本项目的根目录。
	public static void main(String[] args) throws Exception {
		DataConn dataConn = new DataConn();
		dataConn.connect(DRIVER,CONN_STRING);
		while(true) {
			ServerSocket serverSocket = new ServerSocket(8000); // 建立监听套接字
			Socket socket = serverSocket.accept(); // 建立连接套接字. 从请求队列中取客户端的连接请求，没有则阻塞***
			DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());// 建立连接输入字节流
			DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());// 建立连接输出字节流
			String recv = inputFromClient.readUTF(); // 读入UTF-8编码的字符. 从输入缓冲区读入字符，没有则阻塞***
			String[] message=recv.split("#");
			//返回信息
			ResultSet rs=dataConn.executeQuery("select * From Msg order by ID");
			String response ="";
			while(rs.next()) {
				response=rs.getString("WriteTime")+" "+rs.getString("User_Cde")+" "+rs.getString("Msg");
			}
			outputToClient.writeUTF(response); // 输出UTF-8编码的字符
			//插入新数据
			String t=getNow();
			String values = String.format("'%s','%s','%s'", message[0],message[1], t);
//			System.out.println(values);
			dataConn.executeUpdate("insert into Msg(User_Cde,Msg,WriteTime) values(" + values +")");
			socket.close(); // 关闭连接
			serverSocket.close();
		}
	}
	
	static String getNow() {
		   Calendar cal= Calendar.getInstance();
		   SimpleDateFormat sformat  =   new  SimpleDateFormat("HH:mm:ss");
		   return sformat.format(cal.getTime());
	}
}
