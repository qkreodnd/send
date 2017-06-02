package test.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import test.util.DBConnect;

public class MemberFrame extends JFrame implements ActionListener {
	//멤버필드 정의하기
	private JTextField inputNum, inputName, inputAddr;
	DefaultTableModel model;
	JButton saveBtn, deleteBtn, updateBtn;
	JTable table;
	
	List<Map<String, Object>> members=new ArrayList<>();
	
	//생성자
	public MemberFrame(){
		initUI();
	}
	//method
	public void initUI(){
		//레이아웃 설정
		setLayout(new BorderLayout());
		//상단 페널 객체 생성
		JPanel topPanel=new JPanel();
		
		//레이블 객체 생성
		JLabel label1=new JLabel("번호");
		JLabel label2=new JLabel("이름");
		JLabel label3=new JLabel("주소");
		
		//텍스트 필드 객체 생성
		inputNum=new JTextField(10);
		inputName=new JTextField(10);
		inputAddr=new JTextField(10);
		
		saveBtn=new JButton("저장");
		deleteBtn=new JButton("삭제");
		updateBtn=new JButton("수정");
		saveBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		
		//페널에 컴포넌트 추가 하기 
		topPanel.add(label1);
		topPanel.add(inputNum);
		topPanel.add(label2);
		topPanel.add(inputName);
		topPanel.add(label3);
		topPanel.add(inputAddr);
		topPanel.add(saveBtn);
		topPanel.add(deleteBtn);
		topPanel.add(updateBtn);
		
		//프레임의 상단에 페널 배치하기
		add(topPanel, BorderLayout.NORTH);
		
		//테이블 칼럼명
		String[] colNames={"번호","이름","주소"};
		//테이블에 연결할 모델 객체
		model=new DefaultTableModel(colNames, 0);
		
		
		
		//테이블 객체 생성
		table=new JTable();
		//테이블에 모델 연결
		table.setModel(model);
		
		//스크롤할 수 있는 UI 로 테이블을 감싼다.
		JScrollPane tablePanel=new JScrollPane(table);
		
		add(tablePanel, BorderLayout.CENTER);
		
		//가로 세로 크기 지정하기
		setSize(800, 500);
		//화면의 가운데 배치 되도록
		setLocationRelativeTo(null);
		setVisible(true);
		//창 닫을 때 프로세스 종료되도록
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//파일에 있는 정보 로딩하기
		//loadFromFile();
		
		//DB 에 있는 정보 로딩하기
		loadFromOracle();
		
		//로딩된 정보 출력하기
		displayMember();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//눌러진 버튼의 참조값을 비교해서 분기한다.
		if(e.getSource()==saveBtn){
			//TextField 에 입력한 내용을 읽어온다.
			int num=Integer.parseInt(inputNum.getText());
			String name=inputName.getText();
			String addr=inputAddr.getText();
			//HashMap 객체에 회원의 정보담기
			Map<String, Object> map=new HashMap<>();
			map.put("num", num);
			map.put("name", name);
			map.put("addr", addr);
			//DB 에 저장하는 작업하기
			insert(map);
			
			//알람창 띄우기
			JOptionPane.showMessageDialog(this, "저장했습니다.");
			
		}else if(e.getSource()==deleteBtn){
			//선택된 table row 의 인덱스를 읽어온다.
			int selectedIndex=table.getSelectedRow();
			if(selectedIndex==-1){// 선택된 row 가 없다면
				JOptionPane.showMessageDialog(this, "삭제할 row를 선택하세요");
				return;
			}
			//List 에서 해당 인덱스 삭제
			members.remove(selectedIndex);

			JOptionPane.showMessageDialog(this, "행이 삭제되었습니다.");
		}else if(e.getSource()==updateBtn){
			int selectedIndex=table.getSelectedRow();
			if(selectedIndex==-1){
				JOptionPane.showMessageDialog(this, "수정할 row 를 선택하세요");
				return;
			}
			//수정할 이름과 주소를 읽어온다
			String name=(String)table
					.getValueAt(selectedIndex, 1); //1은 column 번호
			String addr=(String)table
					.getValueAt(selectedIndex, 2);
			//List 의 데이터를 수정한다.
			members.get(selectedIndex).put("name", name);
			members.get(selectedIndex).put("addr", addr);

			JOptionPane.showMessageDialog(this, "수정되었습니다.");
		}
		
		//멤버필드의 회원정보 clear()
		members.clear();
		//DB 에 있는 정보 로딩하기
		loadFromOracle();
		//로딩된 정보 출력하기
		displayMember();
	}
	
	//Map 에 담긴 정보를 DB 에 저장하는 메소드
	public void insert(Map<String, Object> map){
		Connection conn=new DBConnect().getConn();
		PreparedStatement pstmt=null;
		try {
			String sql="INSERT INTO member (num,name,addr) VALUES(?,?,?)";
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, (int)map.get("num"));
			pstmt.setString(2, (String)map.get("name"));
			pstmt.setString(3, (String)map.get("addr"));
			pstmt.executeUpdate();
		}catch(SQLException se){
			se.printStackTrace();
		}finally{
			try{
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){}
		}
	}
	
	//Oracle DB에 저장된 회원목록을 읽어들이는 메소드
	public void loadFromOracle(){
		//Connection 객체 참조값 얻어오기
		Connection conn=new DBConnect().getConn();
		//필요한 객체의 참조값을 담을 지역변수 만들기
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try {
			String sql="SELECT num,name,addr FROM member ORDER BY num ASC";
			pstmt=conn.prepareStatement(sql);
			//쿼리문을 수행하고 결과값을 ResultSet 객체로 받아오기
			rs=pstmt.executeQuery();
			while(rs.next()){
				int num=rs.getInt("num");
				String name=rs.getString("name");
				String addr=rs.getString("addr");
				//회원정보를 HashMap 객체에 담는다.
				Map<String, Object> map=new HashMap<>();
				map.put("num", num);
				map.put("name", name);
				map.put("addr", addr);
				//HashMap 객체의 참조값을 ArrayList 에 누적시킨다.
				members.add(map);
			}
				
		} catch(SQLException se) {
			se.printStackTrace();	
		} finally {
			try{
				if(rs!=null)rs.close();
				if(pstmt!=null)pstmt.close();
				if(conn!=null)conn.close();
			}catch(Exception e){}
		}
	}
	
	//파일에 저장된 회원목록을 읽어들이는 메소드
	public void loadFromFile(){
		FileInputStream fis=null;
		ObjectInputStream ois=null;
		try {
			fis=new FileInputStream("c:/myFolder/MyMembers.dat");
			ois=new ObjectInputStream(fis);
			//원래 type 으로 casting 해서 필드에 저장한다.
			members=(List<Map<String, Object>>)ois.readObject();
		} catch (FileNotFoundException e) {
			//파일을 만든적이 없다면 FileNotFoundException 발생
			members=new ArrayList<>();
			System.out.println("MyMembers.dat 파일이 없음");
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				if(fis!=null)fis.close();
				if(ois!=null)ois.close();
			} catch (Exception e) {}
		}
	}
	//멤버필드에 있는 회원정보를 출력하는 메소드
	public void displayMember(){
		//table 에 출력된 내용 삭제 되도록
		model.setRowCount(0);
		for(Map<String, Object> tmp:members){
			int num=(int)tmp.get("num");
			String name=(String)tmp.get("name");
			String addr=(String)tmp.get("addr");
			//System.out.println(num+"/"+name+"/"+addr);
			
			//회원정보를 Object[] 에 순서대로 담은 다음
			Object[] row=new Object[3];
			row[0]=num;
			row[1]=name;
			row[2]=addr;
			//테이블에 연결된 모델에 전달한다.
			model.addRow(row);
			
		}
	}
	
	public static void main(String[] args) {
		new MemberFrame();
	}
}
