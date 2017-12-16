package view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.tika.Tika;

import domain.WriteObject;
import process.MainProc;
import process.ProcessService;
import utils.Flag;
import utils.Log;

import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;

public class SenderView extends JPanel {

	private JFileChooser jfc;
	private WriteObject writeData;
	private boolean fileset = false;
	private String[] mimetypeFilter = {"jpg", "png", "mp3"};
	
	private MainProc mainProc;
	private MainView mainView;
	private ProcessService pService;
	
	private JPanel file_select_pn;
	private JButton select_file_btn;
	
	private JPanel file_name_pn;
	private JLabel file_name_lb;
	private JLabel file_name_tx;
	
	private JPanel file_size_pn;
	private JLabel file_size_lb;
	private JLabel file_size_tx;
	
	private JPanel mime_type_pn;
	private JLabel mime_type_lb;
	private JLabel mime_type_tx;
	
	private JPanel send_file_pn;
	private JButton send_file_btn;
	
	/**
	 * Create the panel.
	 * @param mainView 
	 */
	public SenderView(MainView mainView) {
		setBackground(Color.WHITE);
		setLayout(new GridLayout(5, 1, 0, 0));
		
		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(new FileNameExtensionFilter("File Select", mimetypeFilter));
		
		file_select_pn = new JPanel();
		file_select_pn.setBackground(SystemColor.control);
		file_select_pn.setBorder(new EmptyBorder(20, 50, 20, 50));
		add(file_select_pn);
		file_select_pn.setLayout(new GridLayout(0, 1, 0, 0));
		
		select_file_btn = new JButton("파일 선택");
		select_file_btn.addActionListener(listener);
		file_select_pn.add(select_file_btn);
		
		file_name_pn = new JPanel();
		file_name_pn.setBackground(SystemColor.control);
		file_name_pn.setBorder(new EmptyBorder(20, 20, 20, 20));
		add(file_name_pn);
		file_name_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		file_name_lb = new JLabel("파일 이름 : ");
		file_name_lb.setFont(new Font("굴림", Font.PLAIN, 28));
		file_name_pn.add(file_name_lb);
		
		file_name_tx = new JLabel("");
		file_name_tx.setFont(new Font("굴림", Font.PLAIN, 28));
		file_name_pn.add(file_name_tx);
		
		file_size_pn = new JPanel();
		file_size_pn.setBackground(SystemColor.control);
		file_size_pn.setBorder(new EmptyBorder(20, 20, 20, 20));
		add(file_size_pn);
		file_size_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		file_size_lb = new JLabel("파일 크기 : ");
		file_size_lb.setFont(new Font("굴림", Font.PLAIN, 28));
		file_size_pn.add(file_size_lb);
		
		file_size_tx = new JLabel("");
		file_size_tx.setFont(new Font("굴림", Font.PLAIN, 28));
		file_size_pn.add(file_size_tx);
		
		mime_type_pn = new JPanel();
		mime_type_pn.setBackground(SystemColor.control);
		mime_type_pn.setBorder(new EmptyBorder(20, 20, 20, 20));
		add(mime_type_pn);
		mime_type_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		mime_type_lb = new JLabel("MIME 타입 : ");
		mime_type_lb.setFont(new Font("굴림", Font.PLAIN, 28));
		mime_type_pn.add(mime_type_lb);
		
		mime_type_tx = new JLabel("");
		mime_type_tx.setFont(new Font("굴림", Font.PLAIN, 28));
		mime_type_pn.add(mime_type_tx);
		
		send_file_pn = new JPanel();
		send_file_pn.setBackground(SystemColor.control);
		send_file_pn.setBorder(new EmptyBorder(20, 50, 20, 50));
		add(send_file_pn);
		send_file_pn.setLayout(new GridLayout(1, 0, 0, 0));
		
		send_file_btn = new JButton("파일 전송");
		send_file_btn.addActionListener(listener);
		send_file_pn.add(send_file_btn);
	}
	
	public void setMainProc(MainProc mainProc, MainView mainView){
		this.mainProc = mainProc;
		this.mainView = mainView;
	}
	
	public void setProcessService(ProcessService pService, MainView mainView){
		this.pService = pService;
		this.mainView = mainView;
	}
	
	private ActionListener listener = new ActionListener(){

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == select_file_btn){
				int result = jfc.showOpenDialog(mainView); 
				
				if(result == JFileChooser.APPROVE_OPTION){
					File file = jfc.getSelectedFile();
					writeData = makeWriteObject(file);
					fileset = true;
				}else{
					Log.i("Cancel Select");
				}
				
				//아래부터 데이터 전송 코드 추가.
			}else if(e.getSource() == send_file_btn){
				if(fileset){
					fileset = false;
					mainProc.writeData(writeData);
				}else{
					JOptionPane.showMessageDialog(mainView, "파일이 선택되지 않았습니다.");
				}
			}
		}
	};
	
	private WriteObject makeWriteObject(File file) {
		WriteObject obj = null;
		Tika tika = new Tika();
		try {
			obj = new WriteObject();
			String filepath = file.getAbsolutePath();
			String filename = file.getName();
			String mimetype = tika.detect(file);
			int filesize = (int) file.length();
			byte[] data = new byte[filesize];
			DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			dis.read(data);
			
			file_name_tx.setText(filename);
			file_size_tx.setText(filesize+" byte");
			mime_type_tx.setText(mimetype);
			
			obj.setStart(Flag.FILE_RECEIVE_MODE);
			obj.setFilename(filename);
			obj.setFilesize(filesize);
			obj.setMimetype(mimetype);
			obj.setData(data);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return obj;
	}
	
	public void defaultSetting(){
		fileset = false;
		file_name_tx.setText("");
		file_size_tx.setText("");
		mime_type_tx.setText("");
	}
}
