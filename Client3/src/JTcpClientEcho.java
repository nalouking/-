import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class JTcpClientEcho {
	public static void main(String[] args) throws Exception { //throws 出错后把错误处理抛给上层
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a string: ");
		String s1 = scanner.nextLine();
		Socket socket = new Socket("localhost", 8000); // 与服务器 建立连接。
		DataInputStream fromServer = new DataInputStream(socket.getInputStream());
		DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
		toServer.writeUTF(s1);
		String s2 = fromServer.readUTF();   // 从输入缓冲区读入字符，没有则阻塞***
		System.out.println(s2);
		socket.close(); // 关闭连接
		scanner.close();
	}
}
