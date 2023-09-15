package server;

import java.sql.ResultSet;

public class User {
	private final static String DRIVER = "com.hxtt.sql.access.AccessDriver";
	private final static String CONN_STRING = "jdbc:Access:///./data/msg.mdb"; // .为当前目录，即本项目的根目录
	private DataConnect connect;
	
	public User(DataConnect con) {
		connect=con;
		connect.connect(DRIVER, CONN_STRING);
	}
	
	void addUser(String msg) {
		connect.executeUpdate("insert into Users(User_Cde,User_Pass) values(" + msg +")");
	}
	
	boolean userExist(String name) {
		return connect.existQuery("select User_Cde From Users where User_Cde='"+name+"'");
	}
	
	boolean passMach(String name,String pass) {
		try {
			ResultSet rs=connect.executeQuery("select User_Pass From Users where User_Cde='"+name+"'");
			String resString=null;
			while(rs.next()) {
				resString=rs.getString("User_Pass");
			}
//			System.out.println(pass);
//			System.out.println(resString);
			if(pass.trim().equals(resString.trim())) {
				return true;
			}
		} catch (Exception e) {
//			System.out.println(e.getMessage());
		}
		return false;
	}
}
