package server;

import java.sql.ResultSet;

public class Msg {
	private final static String DRIVER = "com.hxtt.sql.access.AccessDriver";
	private final static String CONN_STRING = "jdbc:Access:///./data/msg.mdb"; // .为当前目录，即本项目的根目录
	private DataConnect connect;
	
	public Msg(DataConnect con) {
		connect=con;
		connect.connect(DRIVER, CONN_STRING);
	}
	
	void insert(String msg) {
		connect.executeUpdate("insert into Msg(User_Cde,Msg,WriteTime) values(" + msg +")");
	}
	
	String getLatest() {
		String res="";
		try {
			ResultSet rs=connect.executeQuery("select * from(select top 10 *  from Msg order by ID DESC) as a order by a.ID");
			while(rs.next()) {
				res+=rs.getString("WriteTime")+" "+rs.getString("User_Cde")+" "+rs.getString("Msg");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
//			System.out.println(res);
			return res;
		}
	}
}
