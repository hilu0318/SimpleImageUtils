package kr.hilu0318.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import kr.hilu0318.domain.ArrayDTO;

public class ImageFX {

	private static final int V_MODE = 1;
	private static final int H_MODE = 0;
	
	private int procSize;	
	private BufferedImage img;
	private long pixNum;
	private int width;
	private int height;
	private int[] pix;
	private int[][] result;
	private String[][] strpix;
	private float[][] matrix;
	
	private boolean check = true;
	
	public ImageFX(File in) throws IOException{
		this.img = ImageUtils.changeRotate(in);
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.pixNum = squareNumber(2, 32);
		this.result = new int[height][width];
		this.pix = img.getRGB(0, 0, width, height, pix, 0, width);
		this.strpix = checkPix(width, height, pix);
	}
	
	private String[][] checkPix(int width, int height, int[] result) {
		String[][] pix = new String[height][width];
		int checkVal;
		for(int i = 0; i<width; i++){
			for(int j = 0;j <height; j++){
				checkVal = result[width*j + i];
				String str = Integer.toHexString(checkVal).trim();
				pix[j][i] = str;
			}
		}
		return pix;
	}
	
	public BufferedImage imgBlur(int value) throws InterruptedException{
		ArrayDTO dto = BlurMatrix.getBlurMatrix(value);
		this.matrix = dto.getList();
		int size = dto.getCount();
		long startT = System.currentTimeMillis();
		int maxSize = width > height ? width : height;
		int mode = width > height ? H_MODE : V_MODE;
		
		if(0 < maxSize && maxSize <= 100)
			procSize = maxSize;
		else if(100 < maxSize && maxSize <= 500)
			procSize = 100;
		else if(500 < maxSize && maxSize <= 1000)
			procSize = 300;
		else if(1000 < maxSize && maxSize <= 3000)
			procSize = 500;
		else if(3000 < maxSize && maxSize <= 6000)
			procSize = 1000;
		else if(6000 < maxSize)
			procSize = 2000;
		
		int mok = maxSize/procSize;
		int nam = maxSize%procSize;
		int count = nam > 0 ? mok+1 : mok;
		int end;
		
		ImageThread[] t = new ImageThread[count];
		
		for(int i = 0; i < count; i++){
			end = (i+1)*procSize;
			if(mode == H_MODE)
				t[i] = new ImageThread(i*procSize, 0, end > maxSize ? maxSize : end, height, width, height, size);
			else if(mode == V_MODE)
				t[i] = new ImageThread(0, i*procSize, width, end > maxSize ? maxSize : end, width, height, size);
			t[i].start();
		}
		
		while(true){
			Thread.sleep(500);
			
			for(int i = 0; i<count; i++){
				this.check = this.check && t[i].check;
			}
			
			if(this.check){
				break;
			}
			this.check = true;
		}
		long endT = System.currentTimeMillis();
		System.out.println((endT-startT)/1000f + "m/s");
		System.out.println("While Out");
		for(int i = 0; i < width; i++){
			for(int j= 0; j < height; j++){
				pix[this.width*j + i] = result[j][i];
			}
		}
		this.img.setRGB(0, 0, this.width, this.height, pix, 0, this.width);
		
		return this.img;
	}
	
	/*
	public BufferedImage imgBlur(int value) throws InterruptedException{
		ArrayDTO dto = BlurMatrix.getBlurMatrix(value);
		this.matrix = dto.getList();
		int size = dto.getCount();
		
		ImageThread t1 = new ImageThread(0		, 0			, width/2	, height/2	, width, height, size);
		ImageThread t2 = new ImageThread(width/2, 0			, width		, height/2	, width, height, size);
		ImageThread t3 = new ImageThread(0		, height/2	, width/2	, height	, width, height, size);
		ImageThread t4 = new ImageThread(width/2, height/2	, width		, height	, width, height, size);
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		while(!(t1.threadCheck() && t2.threadCheck() && t3.threadCheck() && t4.threadCheck())){
			Thread.sleep(100);
		}
		System.out.println("While Out");
		for(int i = 0; i < width; i++){
			for(int j= 0; j < height; j++){
				pix[this.width*j + i] = result[j][i];
			}
		}
		this.img.setRGB(0, 0, this.width, this.height, pix, 0, this.width);
		return this.img;
	}
	*/
	private class ImageThread extends Thread{
		
		private int startW;
		private int startH;
		private int endW;
		private int endH;
		private int width;
		private int height;
		private int size;
		boolean check = false;
		
		public ImageThread(int startW, int startH, int endW, int endH, int width, int height, int size){
			this.startW = startW;
			this.startH = startH;
			this.endW = endW;
			this.endH = endH;
			this.width = width;
			this.height = height;
			this.size = size;
			
		}

		@Override
		public void run() {
			System.out.println("start Thread");
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			carBlur(startW, startH, endW, endH, width, height, size);
			this.check = true;
			System.out.println(this.check);
		}
	}
	
	private void carBlur(int startW, int startH, int endW, int endH, int width, int height, int size){
		int pixleng = size/2;
		long sumR = 0;
		long sumG = 0;
		long sumB = 0;
		
		int nk;
		int nl;
		int choiceW;
		int choiceH;
		
		String strR = "";
		String strG = "";
		String strB = "";
		String rgb = "";
		
		float testcount = 0f;
		
		for(int i = startW ; i < endW; i++){
			for(int j = startH ; j < endH; j++){
				nk = i - pixleng;
				nl = j - pixleng;				
				
				for(int k = 0 ; k < size; k++){
					for(int l = 0 ; l < size; l++){
						choiceW = nk + k;
						choiceH = nl + l;
						if(!(choiceW < 0) && !(choiceH < 0) && (choiceW < width) && (choiceH < height)){
							sumR += (int)(Long.parseLong(strpix[choiceH][choiceW].substring(2, 4), 16) * matrix[l][k]);
							sumG += (int)(Long.parseLong(strpix[choiceH][choiceW].substring(4, 6), 16) * matrix[l][k]);
							sumB += (int)(Long.parseLong(strpix[choiceH][choiceW].substring(6), 16) * matrix[l][k]);
							testcount += matrix[l][k];
						}
					}
				}
				
				if(sumR != 0) sumR /= testcount;
				if(sumG != 0) sumG /= testcount;
				if(sumB != 0) sumB /= testcount;
				testcount = 0f;
				
				if(sumR < 16) strR = "0"+Long.toHexString(sumR).trim();
				else strR = Long.toHexString(sumR).trim();
				if(sumG < 16) strG = "0"+Long.toHexString(sumG).trim();
				else strG = Long.toHexString(sumG).trim();
				if(sumB < 16) strB = "0"+Long.toHexString(sumB).trim();
				else strB = Long.toHexString(sumB).trim();
				
				rgb = "ff" + strR + strG + strB;
				result[j][i] = (int)(Long.parseLong(rgb, 16) - pixNum);
				sumR = sumG = sumB = 0;
			}
		}
	}
	
	private long squareNumber(int number, int n) {
		long result = 1;
		if(n == 0)
			return result;
		else{
			for(int i = 0; i< n ; i++)
				result *= (long)number;
			return result;
		}
	}
}
