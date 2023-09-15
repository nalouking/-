package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SlaveSock {
	private Socket socket=null;
	private DataInputStream inputFromClient;
	private DataOutputStream outputToClient;

	SlaveSock(Socket sock){
		try {
			socket=sock;
			inputFromClient = new DataInputStream(socket.getInputStream());
			outputToClient = new DataOutputStream(socket.getOutputStream());// 建立连接输出字节流
		} catch (IOException e) {
			e.printStackTrace();
		}// 建立连接输入字节流
	}

	boolean isConn() {
		return (socket != null);
	}

	boolean write(String msg) {
		boolean res = true;
		try {
			outputToClient.writeUTF(msg);
		} catch (Exception ex) {			
			res = false;
		}
		if(!res) {
			try {
				socket.close();
			}
			catch(Exception ex) {				
				// ex.printStackTrace();
			}
			finally {
				socket = null;
			}
		}
		return res;
	}

	String read() {
		String msg = "";
		try {
			msg = inputFromClient.readUTF();
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
		return msg;
	}

	boolean close() {		
		try {
			socket.close();
			socket = null;
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
}
