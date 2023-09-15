package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MasterSock {
	private int port = 8000;
	private ServerSocket socket;
	
	void setPort(int port) {
		this.port = port;
	}

	int getPort() {
		return this.port;
	}

	boolean isConn() {
		return (socket != null);
	}


	boolean conn() {
		try {
			socket = new ServerSocket(port); // 与服务器 建立连接。
		} catch (Exception ex) {
			// ex.printStackTrace();
		}		
		return (socket != null);
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
	
	Socket accept(){
		try {
			return socket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
