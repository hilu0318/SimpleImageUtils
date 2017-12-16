package process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import domain.WriteObject;
import utils.Flag;
import utils.Log;
import view.MainView;

public class ProcessService {

	final UUID uuid = new UUID("0000000300001000800000805F9B34FB", false);
	final String CONNECTION_URL_FOR_SPP = "btspp://localhost:"+uuid+";name=SPP Server";
	
	private int count = 0;
		
	private Date today;
	private String outPath;
	
	private MainView mainView;

	private ConnectThread connectThread;
	private Writer writer;
	private boolean disconnected = false;
	
	
	public ProcessService(String defaultPath) {
		outPath = defaultPath;
	}

	public void setFilePath(String path){
		Log.i("File Path Setting - " + path);
		this.outPath = path;
	}
	
	public void connect(){
		if(connectThread != null)
			connectThread.close();
		 connectThread = new ConnectThread();
		 connectThread.start();
	}
	
	public void writer(StreamConnection mStreamConnection){
		if(writer != null)
			writer.close();
		writer = new Writer(mStreamConnection);
		writer.start();
	}
	
	public void closeAll(){
		writer.close();		
	}	
	
	public void setMainView(MainView mainView){
		this.mainView = mainView;
	}
	
	private class ConnectThread extends Thread{
		
		private StreamConnectionNotifier mStreamConnectionNotifier;
		private StreamConnection mStreamConnection;
		
		public ConnectThread(){
			try{
				mStreamConnectionNotifier = (StreamConnectionNotifier)Connector.open(CONNECTION_URL_FOR_SPP);
				Log.i("Write Server - Opened connection successful.");
			}catch(IOException e){
				Log.i("Write Server - Could not open connection : "+e.getMessage());
				return;
			}
		}
		
		@Override
		public void run(){
			Log.i("Write Server - Server is Running");
			Log.i("Write Server - Wait for client request...");
			try{
				mStreamConnection = mStreamConnectionNotifier.acceptAndOpen();		//여기가 새로운 접속여부 판단부분.
			}catch(IOException e){
				Log.i("Write Server - Could not open connection : "+e.getMessage());
			}
			count++;
			Log.i("Write Server - 현재 접속중인 클라이언트 수 : "+count);
			writer(mStreamConnection);
		}
		
		public void close(){
			try {
				mStreamConnection.close();
				mStreamConnectionNotifier.close();
			} catch (IOException e) {
				Log.i("Write Server - ConnectThread Close Exception");
				e.printStackTrace();
			}
		}
	}
	
	private class Writer extends Thread {
		
		private DataInputStream dis;
		private DataOutputStream dos;
		
		private String remoteDeviceString;
		private StreamConnection mStreamConnection;
		

		public Writer(StreamConnection mStreamConnection) {
			this.mStreamConnection = mStreamConnection;
			try{
				dis = new DataInputStream(new BufferedInputStream(this.mStreamConnection.openInputStream(), 1024*10));
				dos = new DataOutputStream(new BufferedOutputStream(this.mStreamConnection.openOutputStream(), 1024*10));
				Log.i("Write Server - Writer : < Open IOStream.. >");
			}catch(IOException e){
				Log.i("Write Server - Writer : < Could not open connection : "+e.getMessage()+" >");
				Thread.currentThread().interrupt();
				return;
			}
			
			try{
				RemoteDevice remoteDevice = RemoteDevice.getRemoteDevice(mStreamConnection);
				remoteDeviceString = remoteDevice.getBluetoothAddress();
				Log.i("Write Server - Writer : < Remote device >");
				Log.i("Write Server - Writer : < address : "+remoteDeviceString+" >");
			}catch(IOException e){
				Log.i("Write Server - Writer : < Could not open connection : "+e.getMessage()+" >");
				return;
			}
			Log.i("Write Server - Writer : < Client is connected >");
		}
		

		public void close() {
			try {
				dis.close();
				dos.close();
				mStreamConnection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try{
				
				Log.i("Receive Server - Receiver : Ready");
				
				Date startDate;
				Date endDate;
									
				Log.i("Receive Server - Receiver : Data Copy Ready!!");
				
				mainView.setStatus("연결 완료-전송 대기중");
				
				startDate = new Date();
				
				//1차 클라이언트 연결확인. 강제 종료시 동작.
				
				while(true){
					int tempInt = dis.read();
					if(tempInt == -1){
						break;
					}
					
					int mode = dis.readInt();
					if(mode == -1){
						break;
					}
					
					switch(mode){
						case Flag.FILE_RECEIVE_MODE:{
							Log.i("FILE_RECEIVE_MODE : Start");
							mainView.changPanel("receive");
							mainView.startSendingThread();
							
							String _filename = dis.readUTF();
							String _mimetype = dis.readUTF();
							int _filesize = dis.readInt();
							Log.i("FILE_RECEIVE_MODE : Get String Data");
							mainView.setFileName(_filename);
							mainView.setFileSize(_filesize);
							mainView.setMIMEType(_mimetype);
							mainView.setProgressMax(_filesize);
							Log.i("FILE_RECEIVE_MODE : Set String Data");
							//가변바이트를 생성할 수 있도록 바이트 배열 아웃스트림 생성.
							ByteArrayOutputStream dos = new ByteArrayOutputStream(_filesize);
						
							//실제 파일 바이트배열로 받기.
							int data = 0;
							int copySize = 0;
							while(true){
								byte[] buffer = new byte[1024*10];
								data = dis.read(buffer);
								dos.write(buffer, 0, data);
								copySize += data;
								mainView.setProgressVal(copySize);
								if(copySize == _filesize)
									break;
							}
							
							Log.i("FILE_RECEIVE_MODE : Get Byte[] Data");
							
							//바이트배열 스트림에서 바이트배열을 추출.
							byte[] filedata = dos.toByteArray();
							Log.i("FileSize : " + _filesize + " , byte[] Size : " +filedata.length);
							
							//실제로 저장할 위치의 아웃스트림 생성.
							DataOutputStream fdos = new DataOutputStream(new FileOutputStream(new File(outPath + "/" + _filename)));
							fdos.write(filedata);
						
							mainView.setStatus("연결 완료-전송 완료");
						
							SenderMsg(Flag.FILE_WRITE_FINISH);
							dos.close();
							fdos.close();
						
							endDate = new Date();
							float sec = ((float)(endDate.getTime()-startDate.getTime())/1000f);
							Log.i("Receive Server - Receiver : Data Copy End!!!");
							Log.i("Receive Server - Receiver : Date Copy Time : "+sec+" Seconds, BPS : "+Math.round((_filesize/sec)/10000f)/100f);
							disconnected = true;
							mainView.changPanel("default");		//데이터 전송이 끝나면 다시 디폴트 페이지로.
							mainView.stopSendingThread();
							mainView.setStatus("전송 완료.");
							break;
						}
						case Flag.FILE_SEND_MODE:{
							mainView.changPanel("sender");	//연결되면 페이지 전환.
							break;
						}
						case Flag.FILE_WRITE_FINISH:{
							//폰으로 파일전송. 페이지 전환 없음.
							Log.i("File Write Finish In");
							mainView.getSenderView().defaultSetting();
							mainView.stopSendingThread();
							mainView.setStatus("전송 완료.");
							break;
						}
					}
					if(disconnected){
						disconnected = false;
						break;
					}
				}
				
				Thread.currentThread().interrupt();
				count--;
				Log.i("Receive Server - Receiver : Client has been disconnected");
				Log.i("Receive Server - Receiver : 현재 접속중인 클라이언트 수 : "+count);
            }catch (IOException e){
                e.printStackTrace();
            }
			connect();
		}
		
		public void write(WriteObject obj){
			try {
				mainView.startSendingThread();
                dos.write("1".getBytes());
                dos.writeInt(obj.getStart());
                if(obj.getStart() == Flag.FILE_RECEIVE_MODE){
                    dos.writeUTF(obj.getFilename());
                    dos.writeUTF(obj.getMimetype());
                    
                    dos.writeInt(obj.getFilesize());
                    
                    dos.write(obj.getData());
                }
                this.dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
		
		private void SenderMsg(int code) {
			try {
				dos.write("1".getBytes());
				dos.writeInt(code);
				dos.flush();
			} catch (IOException e) {
				Log.i("Write Server - Writer : Sender Exception!!");
				e.printStackTrace();
			}
			Log.i("Write Server - Writer : < Sender : Me = "+code+" >");
		}
	}
	
	//오직 파일 쓰기에만 사용하기.
	public void writeOnlyData(WriteObject obj){
		WriteThread writeTr = new WriteThread(obj);
		writeTr.start();
	}
	
	private class WriteThread extends Thread{
        private WriteObject obj;
        public WriteThread(WriteObject obj){
            this.setName("Write Server - Writer : Data Write Thread");
            this.obj = obj;
        }

        @Override
        public void run() {
        	//rePn.startSendingTr();
        	writer.write(obj);
        }
    }
}
