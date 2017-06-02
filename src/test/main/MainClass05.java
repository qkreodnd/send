package test.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import test.util.DBConnect;

/*
 *  JDBC(Java DataBase Connectivity)
 *  
 *  - Java 와 DataBase 연동하기 
 *  - Java 에서 DB 에 접근하여 데이터 조회, 삽입, 수정, 삭제
 *  를 가능하게 하는것
 */
public class MainClass05 {
	public static void main(String[] args) {
		// oracle 에 scott/tiger 계정으로 접속해서 member 테이블에
		// 회원 한명의 정보를 저장하려고 한다.
		
		//필요한 객체의 참조값을 담을 변수 만들기
		PreparedStatement pstmt=null;
		//Query 의 결과값을 받을 변수 만들기
		ResultSet rs=null;
		//Connection 객체
		Connection conn=new DBConnect().getConn();
		try{
			//실행할 sql 문 구성하기 
			String sql="SELECT num,name,addr FROM member ORDER BY num ASC";
			//PreparedStatement 객체의 참조값 얻어오기
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()){
				int num=rs.getInt("num");
				String name=rs.getString("name");
				String addr=rs.getString("addr");
				System.out.println(num+" | "+name+" | "+addr);
			}
				
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){}
		}
		
		System.out.println("main method returned.");
	}
}




