import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class JTcpServerEcho {
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(8000); // 建立监听套接字
		Socket socket = serverSocket.accept(); // 建立连接套接字. 从请求队列中取客户端的连接请求，没有则阻塞***
		DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());// 建立连接输入字节流
		DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());// 建立连接输出字节流
		String s1 = inputFromClient.readUTF(); // 读入UTF-8编码的字符. 从输入缓冲区读入字符，没有则阻塞***
		outputToClient.writeUTF(getNow()+":" + s1); // 输出UTF-8编码的字符
		socket.close(); // 关闭连接
		serverSocket.close();
	}
	
	static String getNow() {
		   Calendar cal= Calendar.getInstance();
		   SimpleDateFormat sformat  =   new  SimpleDateFormat("HH:mm:ss");
		   return sformat.format(cal.getTime());
	}
}