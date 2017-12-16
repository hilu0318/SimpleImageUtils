package process;

import domain.WriteObject;
import view.MainView;

public class MainProc {

	private ProcessService pService;
	
	public MainProc(String defaultPath){
		pService = new ProcessService(defaultPath);
		pService.connect();
		
	}
	
	public void setMainView(MainView mainView){
		pService.setMainView(mainView);
		
	}
	
	public void reConnect(){ this.pService.connect(); }
	
	public void writeData(WriteObject obj){ this.pService.writeOnlyData(obj); }
	
	public void setFilePath(String path){ this.pService.setFilePath(path); }
}
