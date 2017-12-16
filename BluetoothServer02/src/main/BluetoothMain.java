package main;

import process.MainProc;
import process.ProcessService;
import view.MainView;

public class BluetoothMain {
	
	private static String defaultPath = "C:/zzz";
	
	public static void main(String[] args){
		MainView mView = new MainView(defaultPath);
		MainProc mProc = new MainProc(defaultPath);
		mView.setMainProc(mProc);
		mProc.setMainView(mView);
		
		mView.showView();
	}
}
