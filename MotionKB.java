package example2;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MotionKB extends JFrame implements ActionListener {
	JTextField keyboard;
	JButton[] JButton;
	String[] btnValues= {
            "a", "b", "c", "d", "e", "f", "g", "h", "i",
            "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z"
        };

	public Dimension PanelSize = new Dimension(60,400);
	public Dimension BtnSize = new Dimension(50,30);

	//Constructor
	public MotionKB(){
		initUI();
	}
	//UI 초기화 메소드
	public void initUI(){
		//레이아웃 설정
		this.setLayout(new BorderLayout());
		//페널 객체 생성하기
		JPanel panelN=new JPanel();
		JPanel panelE=new JPanel();
		JPanel panelW=new JPanel();
		JPanel panelS=new JPanel();
		JPanel panelMid=new JPanel();
		
		panelMid.add(new JLabel("google"));
		panelMid.add(new JTextField(15));
		panelMid.setPreferredSize(new Dimension(50,30));
		
		//Panel vartically set
		panelE.setLayout(new FlowLayout());
		panelE.setPreferredSize(PanelSize);
		panelW.setLayout(new FlowLayout());
		panelW.setPreferredSize(PanelSize);
		
		//버튼 객체 생성하기
		JButton =new JButton[26];
		for (int i = 0; i < btnValues.length; i++) {
        	if(i<=4){
        		JButton[i]=new JButton(btnValues[i]);
        		JButton[i].setPreferredSize(BtnSize);
            	panelN.add(JButton[i]);
            	
            	JButton[i].addActionListener(this);
        	}else if(i>4 && i<=12){
        		JButton[i]=new JButton(btnValues[i]);
        		JButton[i].setPreferredSize(BtnSize);
            	panelE.add(JButton[i]);
            	JButton[i].addActionListener(this);
        	}else if(i>12 && i<=17){
        		JButton[i]=new JButton(btnValues[i]);
        		JButton[i].setPreferredSize(BtnSize);
            	panelS.add(JButton[i]);
            	JButton[i].addActionListener(this);
        	}else if(i>17 && i<=25){
        		JButton[i]=new JButton(btnValues[i]);
        		JButton[i].setPreferredSize(BtnSize);
            	panelW.add(JButton[i]);
            	JButton[i].addActionListener(this);
        	}
        }
		
		this.add(panelN, BorderLayout.NORTH);
		this.add(panelE, BorderLayout.EAST);
		this.add(panelS, BorderLayout.SOUTH);
		this.add(panelW, BorderLayout.WEST);
		this.add(panelMid,BorderLayout.CENTER);
		
		setTitle("모션인식 키보드");
		setBounds(200, 200, 350, 410);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new MotionKB();
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//립 모션을 통해 API에 정의된 변수를 이용하여 버튼동작을 구현한다.	
	}
}
