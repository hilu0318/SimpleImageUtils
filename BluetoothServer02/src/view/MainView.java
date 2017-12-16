package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.awt.FlowLayout;

import process.MainProc;
import process.ProcessService;
import utils.Log;

import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

public class MainView extends JFrame {

	private String savePath;
	
	private Dimension frameSize;
	private Dimension screenSize;
	private JFileChooser jfc;
	
	private SendingThread sendingTr;
	
	//private JFrame frame;
	private ReceiveView receiveView;
	private SenderView senderView;
	
	private JPanel low_pn;
	private GridBagLayout gridBagLayout;
	private GridBagLayout gbl_main_pn;
	private GridBagConstraints gbc_main_pn;
	private JPanel main_pn;
	
	private JButton back_btn;
	private GridBagConstraints gbc_back_btn;
	
	private JPanel top_pn;
	private GridBagConstraints gbc_top_pn;
		
	private JPanel save_path_pn;
	private JLabel savePathlb;
	private JLabel savePathtx;
	
	private JPanel status_pn;
	private JLabel statuslb;
	private JLabel statustx;
	
	private JButton select_path_btn;
	private GridBagConstraints gbc_select_path_btn;
	
	private GridBagConstraints gbc_low_pn;
	
	//private MainProc mainProc;
	private ProcessService pService;
	
	private Map<String, JPanel> pnMap = new HashMap<>();

	private MainProc mainProc;
	
	/***************** 설정 ******************/
	private boolean ifFirstPage = true;
	
	/**
	 * Create the application.
	 * @param defaultPath 
	 */
	public MainView(String defaultPath) {
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.savePath = defaultPath;
		File file = new File(defaultPath);
		if(!file.exists())
			file.mkdirs();
		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.setSelectedFile(file);
		
		receiveView = new ReceiveView(this);
		senderView = new SenderView(this);
		
		pnMap.put("sender", senderView);
		pnMap.put("receive", receiveView);
				
		initialize();
	}
	
	public void showView(){
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//frame = new JFrame();
		this.getContentPane().setBackground(Color.WHITE);
		this.setBounds(0, 0, 1000, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{974, 0};
		gridBagLayout.rowHeights = new int[]{82, 647, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		this.getContentPane().setLayout(gridBagLayout);
		
		frameSize = this.getSize();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
		
		main_pn = new JPanel();
		main_pn.setBackground(Color.WHITE);
		gbc_main_pn = new GridBagConstraints();
		gbc_main_pn.fill = GridBagConstraints.BOTH;
		gbc_main_pn.insets = new Insets(0, 0, 5, 0);
		gbc_main_pn.gridx = 0;
		gbc_main_pn.gridy = 0;
		this.getContentPane().add(main_pn, gbc_main_pn);
		gbl_main_pn = new GridBagLayout();
		gbl_main_pn.columnWidths = new int[]{112, 737, 125, 0};
		gbl_main_pn.rowHeights = new int[]{82, 0};
		gbl_main_pn.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_main_pn.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		main_pn.setLayout(gbl_main_pn);
		
		back_btn = new JButton("Back");
		back_btn.addActionListener(listener);
		gbc_back_btn = new GridBagConstraints();
		gbc_back_btn.fill = GridBagConstraints.BOTH;
		gbc_back_btn.insets = new Insets(0, 0, 0, 5);
		gbc_back_btn.gridx = 0;
		gbc_back_btn.gridy = 0;
		main_pn.add(back_btn, gbc_back_btn);
		
		top_pn = new JPanel();
		top_pn.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		top_pn.setBackground(Color.WHITE);
		gbc_top_pn = new GridBagConstraints();
		gbc_top_pn.fill = GridBagConstraints.BOTH;
		gbc_top_pn.insets = new Insets(0, 0, 0, 5);
		gbc_top_pn.gridx = 1;
		gbc_top_pn.gridy = 0;
		main_pn.add(top_pn, gbc_top_pn);
		top_pn.setLayout(new GridLayout(2, 1, 0, 0));
		
		save_path_pn = new JPanel();
		save_path_pn.setBackground(Color.WHITE);
		save_path_pn.setBorder(new EmptyBorder(0, 15, 0, 0));
		top_pn.add(save_path_pn);
		save_path_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		savePathlb = new JLabel("저장 위치 : ");
		savePathlb.setFont(new Font("굴림", Font.PLAIN, 18));
		save_path_pn.add(savePathlb);
		
		savePathtx = new JLabel(this.savePath);
		savePathtx.setFont(new Font("굴림", Font.PLAIN, 18));
		save_path_pn.add(savePathtx);
		
		status_pn = new JPanel();
		status_pn.setBackground(Color.WHITE);
		status_pn.setBorder(new EmptyBorder(0, 15, 0, 0));
		top_pn.add(status_pn);
		status_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		statuslb = new JLabel("상태 : ");
		statuslb.setFont(new Font("굴림", Font.PLAIN, 18));
		status_pn.add(statuslb);
		
		statustx = new JLabel("연결 대기중");
		statustx.setFont(new Font("굴림", Font.PLAIN, 18));
		status_pn.add(statustx);
		
		select_path_btn = new JButton("위치선택");
		select_path_btn.addActionListener(listener);
		select_path_btn.setFont(new Font("굴림", Font.PLAIN, 20));
		gbc_select_path_btn = new GridBagConstraints();
		gbc_select_path_btn.fill = GridBagConstraints.BOTH;
		gbc_select_path_btn.gridx = 2;
		gbc_select_path_btn.gridy = 0;
		main_pn.add(select_path_btn, gbc_select_path_btn);
		
		low_pn = new JPanel();
		low_pn.setBackground(Color.WHITE);
		low_pn.setLayout(new GridLayout(1, 1));
		
		gbc_low_pn = new GridBagConstraints();
		gbc_low_pn.fill = GridBagConstraints.BOTH;
		gbc_low_pn.gridx = 0;
		gbc_low_pn.gridy = 1;
		this.getContentPane().add(low_pn, gbc_low_pn);
	}
	
	
	public void changPanel(String key){
		Log.i("Change Method In");
		low_pn.removeAll();
		if(key.equals("default")){
			this.ifFirstPage = true;
			statustx.setText("연결 대기중");
		}else{
			low_pn.add(pnMap.get(key));
			this.ifFirstPage = false;
		}
		low_pn.revalidate();
		low_pn.repaint();
	}
	
	/***** 저장위치 설정 및 가져오기 *****/
	public void setSavePath(String path){ savePath = path; }
	public String getSavePath(){ return savePath; }
	public void setResultPath(String path){ this.mainProc.setFilePath(path);}
	
	
	/***** 프로세스 설정 *****/
	public void setMainProc(MainProc mainProc){ 
		this.mainProc = mainProc;
		this.receiveView.setMainProc(mainProc, this);
		this.senderView.setMainProc(mainProc, this);
	}
	
	public void setProcess(ProcessService pService){
		this.pService = pService;
		this.receiveView.setProcessService(pService, this);
		this.senderView.setProcessService(pService, this);
	}
	
	/***** 하위 뷰어 가져오기 *****/
	public ReceiveView getReceiveView(){ return this.receiveView; }
	public SenderView getSenderView(){ return this.senderView; }
	
	/***** 값 수정 함수 *****/
	public void setFilePath(String path){ this.savePathtx.setText(path); }
	public void setStatus(String status){ this.statustx.setText(status); }
	public void setProgressVal(int prog){ this.receiveView.setProgressVal(prog); }
	public void setProgressMax(int max){ this.receiveView.setProgressMax(max); }
	
	public void startSendingThread(){ 
		this.sendingTr = new SendingThread();
		this.sendingTr.start();
	}
	public void stopSendingThread(){ 
		this.sendingTr.close(); 
	}
	
	public void setFileName(String filename){ this.receiveView.setFileName(filename); }
	public void setFileSize(int filesize){ this.receiveView.setFileSize(filesize+""); }
	public void setMIMEType(String mimetype){ this.receiveView.setMIMEType(mimetype); }
	
	private ActionListener listener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == back_btn){
				if(!ifFirstPage){
					stopSendingThread();
					changPanel("default");
					pService.closeAll();
				}
			}else if(e.getSource() == select_path_btn){
				jfc.showOpenDialog(null);
				
				String resultPath = jfc.getSelectedFile().toString();
				savePathtx.setText(resultPath);
				setResultPath(resultPath);
			}
		}		
	};
	
	private class SendingThread extends Thread{

		private boolean stopch = false;
		private String[] list = {"전송중", "전송중>","전송중>>","전송중>>>","전송중>>>>","전송중>>>>>",};
		private int count = 0;
		
		@Override
		public void run() {
			while(true){
				if(stopch)
					break;
				try {
					if(count == list.length)
						count = 0;
					setStatus(list[count]);
					Thread.sleep(500);
					count++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void close(){
			stopch = true;
		}
		
	}
}
