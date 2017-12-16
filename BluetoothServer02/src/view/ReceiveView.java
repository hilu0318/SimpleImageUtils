package view;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;

import process.MainProc;
import process.ProcessService;

public class ReceiveView extends JPanel {

	private MainProc mainProc;
	private MainView mainView;
	private ProcessService pService;
	
	private GridBagLayout gridBagLayout;
	private JPanel file_name_pn;
	private GridBagConstraints gbc_file_name_pn;
	
	private JLabel file_name_lb;
	private JLabel file_name_tx;
	
	private JPanel file_size_pn;
	private GridBagConstraints gbc_file_size_pn;
	
	private JLabel file_size_lb;
	private JLabel file_size_tx;
	
	private JPanel mime_type_pn;
	private GridBagConstraints gbc_mime_type_pn;
	
	private JLabel mime_type_lb;
	private JLabel mime_type_tx;
	
	private JPanel panel;
	private GridBagConstraints gbc_panel;
	private JProgressBar progressBar;
	/**
	 * Create the panel.
	 * @param mainView 
	 */
	public ReceiveView(MainView mainView) {
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0};
		gridBagLayout.rowHeights = new int[] {50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		file_name_pn = new JPanel();
		file_name_pn.setBorder(new EmptyBorder(20, 20, 0, 0));
		
		gbc_file_name_pn = new GridBagConstraints();
		gbc_file_name_pn.insets = new Insets(0, 0, 5, 0);
		gbc_file_name_pn.fill = GridBagConstraints.BOTH;
		gbc_file_name_pn.gridx = 0;
		gbc_file_name_pn.gridy = 2;
		add(file_name_pn, gbc_file_name_pn);
		file_name_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		file_name_lb = new JLabel("파일 이름 : ");
		file_name_lb.setFont(new Font("굴림", Font.PLAIN, 28));
		file_name_pn.add(file_name_lb);
		
		file_name_tx = new JLabel("");
		file_name_tx.setFont(new Font("굴림", Font.PLAIN, 28));
		file_name_pn.add(file_name_tx);
		
		file_size_pn = new JPanel();
		file_size_pn.setBorder(new EmptyBorder(20, 20, 0, 0));
		
		gbc_file_size_pn = new GridBagConstraints();
		gbc_file_size_pn.insets = new Insets(0, 0, 5, 0);
		gbc_file_size_pn.fill = GridBagConstraints.BOTH;
		gbc_file_size_pn.gridx = 0;
		gbc_file_size_pn.gridy = 3;
		add(file_size_pn, gbc_file_size_pn);
		file_size_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		file_size_lb = new JLabel("파일 크기 : ");
		file_size_lb.setFont(new Font("굴림", Font.PLAIN, 28));
		file_size_pn.add(file_size_lb);
		
		file_size_tx = new JLabel("");
		file_size_tx.setFont(new Font("굴림", Font.PLAIN, 28));
		file_size_pn.add(file_size_tx);
		
		mime_type_pn = new JPanel();
		mime_type_pn.setBorder(new EmptyBorder(20, 20, 0, 0));
		
		gbc_mime_type_pn = new GridBagConstraints();
		gbc_mime_type_pn.insets = new Insets(0, 0, 5, 0);
		gbc_mime_type_pn.fill = GridBagConstraints.BOTH;
		gbc_mime_type_pn.gridx = 0;
		gbc_mime_type_pn.gridy = 4;
		add(mime_type_pn, gbc_mime_type_pn);
		mime_type_pn.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		mime_type_lb = new JLabel("MIME 타입 : ");
		mime_type_lb.setFont(new Font("굴림", Font.PLAIN, 28));
		mime_type_pn.add(mime_type_lb);
		
		mime_type_tx = new JLabel("");
		mime_type_tx.setFont(new Font("굴림", Font.PLAIN, 28));
		mime_type_pn.add(mime_type_tx);
		
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(30, 30, 30, 30));
		
		gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 7;
		add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		progressBar = new JProgressBar();
		panel.add(progressBar);
	}
	
	public void setMainProc(MainProc mainProc, MainView mainView){
		this.mainProc = mainProc;
		this.mainView = mainView;
	}
	public void setProcessService(ProcessService pService, MainView mainView){
		this.pService = pService;
		this.mainView = mainView;
	}
	
	public void setFileName(String filename){ this.file_name_tx.setText(filename); }
	public void setFileSize(String filesize){ this.file_size_tx.setText(filesize); }
	public void setMIMEType(String mimetype){ this.mime_type_tx.setText(mimetype); }
	
	public void setProgressMax(int max){ this.progressBar.setMaximum(max); }
	public void setProgressVal(int prog){ this.progressBar.setValue(prog); }
}
