package test.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	public Connection getConn(){
		Connection conn=null;
		try{
			//오라클 드라이버 로딩하기
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//접속할 oracle DB url 정보
			String url="jdbc:oracle:thin:@localhost:1521:xe";
			//Connection 객체의 참조값 얻어오기
			conn=DriverManager.getConnection(url, "scott", "tiger");
			System.out.println("Oracle DB successfully connected");
		}catch(Exception e){
			e.printStackTrace();
		}
		return conn;
	}
}
